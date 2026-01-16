package com.wulb2018;

import com.wulb2018.service.SimpleMatchingService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for simple App.
 */

@SpringBootTest
public class AppTest
{

    @Resource
    private SimpleMatchingService simpleMatchingService;

    @Test
    public void testSimpleMatching() {

        simpleMatchingService.matching();
    }
}
