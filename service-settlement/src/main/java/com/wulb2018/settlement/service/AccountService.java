package com.wulb2018.settlement.service;

import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.client.model.AccountFeignDTO;
import com.wulb2018.common.service.BaseService;
import com.wulb2018.settlement.mapper.AccountMapper;
import com.wulb2018.settlement.model.dto.AccountAddDTO;
import com.wulb2018.settlement.model.dto.AccountUpdateDTO;
import com.wulb2018.settlement.model.entity.Account;
import com.wulb2018.settlement.model.entity.FeeRule;
import com.wulb2018.settlement.model.entity.TradeSymbol;
import com.wulb2018.settlement.model.vo.AccountVO;
import com.wulb2018.settlement.service.convert.AccountConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    /**
     * 冻结资产
     * @param accountFeignDTO
     * @return
     */
    public boolean frozenAsset(AccountFeignDTO accountFeignDTO) {
        TradeSymbol tradeSymbol = tradeSymbolService.getById(accountFeignDTO.getSymbolId());
        if (tradeSymbol == null) {
            return false;
        }
        String baseAsset;
        if (OrderSide.BUY.getCode().equals(accountFeignDTO.getSide())) {
            //买方冻结计价资产
            baseAsset = tradeSymbol.getQuoteAsset();
        } else {
            //卖方冻结基础资产
            baseAsset = tradeSymbol.getBaseAsset();
        }

        FeeRule feeRule = feeRuleService.getOneBySymbolId(accountFeignDTO.getSymbolId());
        if (feeRule == null) {
            return false;
        }

        double maxFeeRate = Math.max(feeRule.getMakerFeeRate(), feeRule.getTakerFeeRate());
        Double frozen = accountFeignDTO.getQuantity() * accountFeignDTO.getPrice() * (1 + maxFeeRate);
        int updateNum = getBaseMapper()
                .frozenAsset(frozen, LocalDateTime.now(), accountFeignDTO.getUserId(), baseAsset);
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

