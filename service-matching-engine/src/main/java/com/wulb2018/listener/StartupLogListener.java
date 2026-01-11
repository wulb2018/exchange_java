package com.wulb2018.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author wulubin
 * @date 2026/1/11
 * @description TODO
 */
@Component
public class StartupLogListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("server.port");
        String ip = getLocalIp();
        String localUrl = String.format(
                "http://%s:%s",
                ip,
                port
        );

        System.out.println();
        System.out.println("============================================================");
        System.out.println("Application started successfully!");
        System.out.println("Local access URL: " + localUrl);
        System.out.println("============================================================");
        System.out.println();
    }
    private String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }
}
