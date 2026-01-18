package com.wulb2018.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.wulb2018.common.handler.AutoFillMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JdbcAutoConfiguration {

	@Bean
	public MybatisPlusInterceptor paginationInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		// 防全表更新与删除插件
		mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		// 分页插件
		//mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return mybatisPlusInterceptor;
	}

	@Bean
	public AutoFillMetaObjectHandler autoFillMetaObjectHandler() {
		return new AutoFillMetaObjectHandler();
	}

}
