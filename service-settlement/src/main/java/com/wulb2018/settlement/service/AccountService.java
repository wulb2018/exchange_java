package com.wulb2018.settlement.service;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.biz.model.entity.TradeSymbol;
import com.wulb2018.biz.service.TradeSymbolService;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.common.util.BizAssert;
import com.wulb2018.settlement.mapper.AccountMapper;
import com.wulb2018.settlement.model.dto.AccountAddDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;
import com.wulb2018.settlement.model.entity.Account;
import com.wulb2018.settlement.model.entity.FeeRule;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.model.vo.AccountVO;
import com.wulb2018.settlement.service.convert.AccountConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户主表(t_account)-业务处理类
 *
 * @author makejava
 * @since 2026-01-18 18:11:50
 */
@Service
@RequiredArgsConstructor
public class AccountService extends BaseService<AccountMapper, Account> {

    private final AccountConvert accountConvert;
    private final FeeRuleService feeRuleService;
    private final TradeSymbolService tradeSymbolService;
    private final AccountFrozenLogService accountFrozenLogService;
    private final AccountLedgerService accountLedgerService;

    /**
     * 冻结资产
     * 买方花10u购买一个BTC ,需要冻结 10u和最大手续费U,
     * 卖方卖出一个BTC,冻结1BTC
     * @param accountCommonDTO
     * @return
     */
    public boolean frozenAsset(AccountCommonDTO accountCommonDTO) {
        TradeSymbol tradeSymbol = tradeSymbolService.getById(accountCommonDTO.getSymbolId());
        if (tradeSymbol == null) {
            return false;
        }
        //todo 要做最终一致性
        String asset;
        if (OrderSide.BUY.equals(accountCommonDTO.getSide())) {
            //买方冻结计价资产
            asset = tradeSymbol.getQuoteAsset();
        } else {
            //卖方冻结基础资产
            asset = tradeSymbol.getBaseAsset();
        }

        FeeRule feeRule = feeRuleService.getOneBySymbolId(accountCommonDTO.getSymbolId());
        if (feeRule == null) {
            return false;
        }

        Double frozen = calculateMaxFrozen(accountCommonDTO.getPrice(), accountCommonDTO.getQuantity());

        int updateNum = getBaseMapper()
                .frozenAsset(frozen, LocalDateTime.now(), accountCommonDTO.getUserId(), asset);

        return updateNum > 0;
    }

    private double calculateMaxFrozen(double price, int quantity) {
        BigDecimal orderPriceBigDecimal = BigDecimal.valueOf(price);
        BigDecimal orderQuantityBigDecimal = BigDecimal.valueOf(quantity);
        BigDecimal frozenAmountBigDecimal = orderQuantityBigDecimal.multiply(orderPriceBigDecimal);
        return frozenAmountBigDecimal.doubleValue();
    }

    /**
     * A 花10U买入 1BTC 交易成功后 在U账户减去 10U和手续费(扣除冻结资金字段的数据并将剩余的冻结资金加回可用余额) ，在BTC账户加1
     * B 卖出一个BTC 交易成功后 在U账户加10U 并扣除手续费， 在BTC账户减去1
     *
     * @return
     */
    @Transactional
    public boolean settlementAccount(Trade trade) {
        TradeSymbol tradeSymbol = tradeSymbolService.getById(trade.getSymbolId());
        if (tradeSymbol == null) {
            return false;
        }
        LocalDateTime settlementTime = LocalDateTime.now();
        //处理买方
        settlementBuyAccount(trade, tradeSymbol, settlementTime);
        //处理卖方
        settlementSellAccount(trade, tradeSymbol, settlementTime);
        return true;
    }


    private void settlementBuyAccount(Trade trade, TradeSymbol tradeSymbol, LocalDateTime settlementTime) {
        BigDecimal maxFrozenBigDecimal = BigDecimal.valueOf(calculateMaxFrozen(trade.getPrice(), trade.getQuantity()));
        //更新买方 （计价资产）U 账户资金
        int buyUpdateQuoteAssetAccountNum = getBaseMapper().settlementBuyQuoteAssetAccount(maxFrozenBigDecimal.doubleValue(), trade.getAmount(), settlementTime, trade.getBuyUserId(), tradeSymbol.getQuoteAsset());
        BizAssert.isTrue(buyUpdateQuoteAssetAccountNum > 0, "买方更新计价资产账户失败");
        //查询买方 （计价资产）U 账户资金最新的变动后余额
        Account buyQuoteAssetAccount = lambdaQuery().eq(Account::getUserId, trade.getBuyUserId()).eq(Account::getAsset, tradeSymbol.getQuoteAsset()).last("LIMIT 1").one();
        //保存买方 （计价资产）U 账户资金 变动流水记录
        accountLedgerService.saveTradeBizTypeLedger(buyQuoteAssetAccount, trade.getId(), - trade.getAmount());
        //更新买方 基础资产（BTC）账户资金
        int buyUpdateBaseAssetAccountNum = getBaseMapper().settlementBuyBaseAssetAccount((double)trade.getQuantity(), settlementTime, trade.getBuyUserId(), tradeSymbol.getBaseAsset());
        BizAssert.isTrue(buyUpdateBaseAssetAccountNum > 0, "买方更新基础资产账户失败");
        Account buyBaseAssetAccount = lambdaQuery().eq(Account::getUserId, trade.getBuyUserId()).eq(Account::getAsset, tradeSymbol.getBaseAsset()).last("LIMIT 1").one();
        //保存买方 基础资产（BTC）账户资金 变动流水记录
        accountLedgerService.saveTradeBizTypeLedger(buyBaseAssetAccount, trade.getId(), (double)trade.getQuantity());
    }

    private void settlementSellAccount(Trade trade, TradeSymbol tradeSymbol, LocalDateTime settlementTime) {
        //更新 卖方 基础资产（BTC）账户资金
        int sellUpdateBaseAssetAccountNum = getBaseMapper().settlementSellBaseAssetAccount((double)trade.getQuantity(), settlementTime, trade.getBuyUserId(), tradeSymbol.getBaseAsset());
        BizAssert.isTrue(sellUpdateBaseAssetAccountNum > 0, "卖方更新基础资产账户失败");
        //查询 卖方 基础资产（BTC）账户资金 最新的变动后余额
        Account sellBaseAssetAccount = lambdaQuery().eq(Account::getUserId, trade.getSellUserId()).eq(Account::getAsset, tradeSymbol.getBaseAsset()).last("LIMIT 1").one();
        //保存卖方 基础资产（BTC）账户资金 变动流水记录
        accountLedgerService.saveTradeBizTypeLedger(sellBaseAssetAccount, trade.getId(), -(double)trade.getQuantity());

        //更新 卖方 （计价资产）U 账户资金
        int sellUpdateQuoteAssetAccountNum = getBaseMapper().settlementSellQuoteAssetAccount(trade.getAmount(), settlementTime, trade.getBuyUserId(), tradeSymbol.getQuoteAsset());
        BizAssert.isTrue(sellUpdateQuoteAssetAccountNum > 0, "卖方更新计价资产账户失败");
        //查询 卖方 （计价资产）U账户资金 最新的变动后余额
        Account sellQuoteAssetAccount = lambdaQuery().eq(Account::getUserId, trade.getSellUserId()).eq(Account::getAsset, tradeSymbol.getQuoteAsset()).last("LIMIT 1").one();
        //保存卖方 （计价资产）U账户资金 变动流水记录
        Double sellQuoteChangeAmount = trade.getAmount();
        accountLedgerService.saveTradeBizTypeLedger(sellQuoteAssetAccount, trade.getId(), sellQuoteChangeAmount);
    }

    public boolean unfrozenAsset(Long userId, String asset, double amount) {
        int updateNum = getBaseMapper().unfrozenAsset(amount, LocalDateTime.now(), userId, asset);
        return updateNum > 0;
    }


    public AccountVO getOne(Serializable id) {

        //Account oldAccount = this.lambdaQuery().eq(Account::getUserId, account.getUserId()).eq(Account::getAsset, account.getAsset()).one();
        //oldAccount.setAvailable(oldAccount.getAvailable() - accountAddDTO.getFrozen());
        return accountConvert.toVo(super.getById(id));
    }

    public Boolean save(AccountAddDTO accountAddDTO) {
        return this.save(accountConvert.toEntity(accountAddDTO));
    }

    public Boolean updateById(AccountUpdateDTO accountUpdateDTO) {
        return this.updateById(accountConvert.toEntity(accountUpdateDTO));
    }


}

