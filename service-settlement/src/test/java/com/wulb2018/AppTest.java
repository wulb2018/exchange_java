package com.wulb2018;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.biz.enums.OrderSide;
import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.settlement.model.entity.Trade;
import com.wulb2018.settlement.service.AccountService;
import com.wulb2018.settlement.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private AccountService accountService;

    @Test
    public void testGetCandlestickInitData() {
        tradeService.getCandlestickInitData(CandlestickType.DAY1);
    }

    @Test
    public void testFrozenAsset() {
        AccountCommonDTO accountCommonDTO = new AccountCommonDTO();
        accountCommonDTO.setOrderId(1L);
        accountCommonDTO.setSide(OrderSide.BUY);
        accountCommonDTO.setQuantity(1);
        //todo 需要重新设计，还不具备开发web3交易所的能力，先开发股票交易，具体看GPT提问记录
        accountService.frozenAsset(accountCommonDTO);
    }

    @Test
    public void testSettlementAccount() {
        Trade trade = new Trade();
        trade.setStockId(1L);
        trade.setBuyOrderId(3L);
        trade.setSellOrderId(4L);
        trade.setBuyUserId(1L);
        trade.setSellUserId(2L);
        trade.setPrice(100.);
        trade.setQuantity(1);
        trade.setAmount(100.);
        trade.setMakerSide(OrderSide.BUY);
        trade.setId(2L);
        trade.setCreateDate(LocalDateTime.now());
        trade.setModifyDate(LocalDateTime.now());
        accountService.settlementAccount(trade);
    }


}
