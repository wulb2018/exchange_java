package com.wulb2018.settlement.service;

import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.biz.model.entity.Stock;
import com.wulb2018.biz.service.StockService;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.common.util.BizAssert;
import com.wulb2018.settlement.mapper.AccountMapper;
import com.wulb2018.settlement.model.dto.AccountAddDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;
import com.wulb2018.settlement.model.entity.Account;
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
    private final AccountLedgerService accountLedgerService;
    private final StockService stockService;
    private final UserPositionsService userPositionsService;

    /**
     * 冻结资产
     * @param accountCommonDTO
     * @return
     */
    public boolean frozenBuy(AccountCommonDTO accountCommonDTO) {
        Stock stock = stockService.getById(accountCommonDTO.getStockId());
        if (stock == null) {
            return false;
        }
        //todo 要做最终一致性
        Double frozen = calculateMaxFrozen(accountCommonDTO.getPrice(), accountCommonDTO.getQuantity());
        int updateNum = getBaseMapper()
                .frozenAsset(frozen, LocalDateTime.now(), accountCommonDTO.getUserId());
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
    public void settlementAccount(Trade trade) {
        LocalDateTime settlementTime = LocalDateTime.now();
        //处理买方
        settlementBuyAccount(trade, settlementTime);
        //处理卖方
        settlementSellAccount(trade, settlementTime);
    }


    private void settlementBuyAccount(Trade trade, LocalDateTime settlementTime) {
        //更新买方  账户资金
        int buyUpdateQuoteAssetAccountNum = getBaseMapper().settlementBuyAccount(trade.getAmount(), settlementTime, trade.getBuyUserId());
        BizAssert.isTrue(buyUpdateQuoteAssetAccountNum > 0, "买方更新账户失败");
        //查询买方 资金最新的变动后余额
        Account buyAccount = lambdaQuery().eq(Account::getUserId, trade.getBuyUserId()).last("LIMIT 1").one();
        //保存买方 账户资金 变动流水记录
        boolean saveTradeBizTypeLedgerRet = accountLedgerService.saveTradeBizTypeLedger(buyAccount, trade.getId(), - trade.getAmount());
        BizAssert.isTrue(saveTradeBizTypeLedgerRet, "保存买方账户资金变动流水记录失败");
        //更新买方 持仓
        boolean settlementBuyUserPositionsRet = userPositionsService.settlementBuyUserPositions(trade.getBuyUserId(), trade.getStockId(), trade.getQuantity());
        BizAssert.isTrue(settlementBuyUserPositionsRet, "买方更新持仓失败");
    }

    private void settlementSellAccount(Trade trade, LocalDateTime settlementTime) {
        //卖方更新持仓数量
        boolean settlementSellUserPositionsRet = userPositionsService.settlementSellUserPositions(trade.getSellUserId(), trade.getStockId(), trade.getQuantity());
        BizAssert.isTrue(settlementSellUserPositionsRet, "卖方更新持仓数量失败");
        //更新 卖方 账户资金
        int sellUpdateQuoteAssetAccountNum = getBaseMapper().settlementSellAccount(trade.getAmount(), settlementTime, trade.getSellUserId());
        BizAssert.isTrue(sellUpdateQuoteAssetAccountNum > 0, "卖方更新计价资产账户失败");
        //查询 卖方 资金 最新的变动后余额
        Account sellAccount = lambdaQuery().eq(Account::getUserId, trade.getSellUserId()).last("LIMIT 1").one();
        //保存卖方 资金 变动流水记录
        accountLedgerService.saveTradeBizTypeLedger(sellAccount, trade.getId(), trade.getAmount());
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

