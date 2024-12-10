package com.onezale.bill.listener;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class StartupInfoLogger {

    @Resource
    private Environment environment;

    @Resource
    private ServerProperties serverProperties;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        // 获取服务器端口和协议
        String port = environment.getProperty("server.port");
        String protocol = environment.getProperty("server.servlet.context-path");
        if(StrUtil.isNotEmpty(protocol)){
            port += protocol;
        }
        String host = "localhost";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // 忽略异常，继续使用localhost
        }

        String swaggerUiPath = "/doc.html";

        String swaggerUrl = String.format("http://%s:%s%s", host, port, swaggerUiPath);
        log.info("Swagger 的地址是: " + swaggerUrl);
    }
}
