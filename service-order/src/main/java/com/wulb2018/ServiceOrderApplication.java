package com.wulb2018;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wulb2018.client")
@MapperScan(basePackages = "com.wulb2018.**.mapper")
public class ServiceOrderApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
