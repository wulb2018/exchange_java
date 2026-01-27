package com.wulb2018.client.settlement;

import com.wulb2018.biz.model.dto.AccountCommonDTO;
import com.wulb2018.common.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wulubin
 * @date 2026/1/14
 * @description TODO
 */
@FeignClient(
        name = "service-settlement",
        contextId = "AccountFeignClient"
)
public interface AccountFeignClient {
    @PostMapping("/account/frozen_asset")
    ApiResponse<Boolean> frozenAsset(@Valid @RequestBody AccountCommonDTO accountCommonDTO);
}
