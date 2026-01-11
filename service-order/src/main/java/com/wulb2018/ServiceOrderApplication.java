package com.wulb2018;

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
public class ServiceOrderApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
