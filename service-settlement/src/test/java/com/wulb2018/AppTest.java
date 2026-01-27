package com.wulb2018;

import com.wulb2018.biz.enums.CandlestickType;
import com.wulb2018.settlement.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for simple App.
 */
@SpringBootTest
public class AppTest {
    @Autowired
    private TradeService tradeService;

    @Test
    public void testGetCandlestickInitData() {
        tradeService.getCandlestickInitData(CandlestickType.DAY1);
    }

}
