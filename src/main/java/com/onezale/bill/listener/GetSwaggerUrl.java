package com.onezale.bill.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class GetSwaggerUrl {

    @Value("${knife4j.enable}")
    private Boolean isKnife4j;

    @Resource
    private Environment environment;


    private Integer port = -1; // 初始化为无效值

    // 监听 WebServerInitializedEvent 以获取实际使用的端口号
    @EventListener(WebServerInitializedEvent.class)
    public void onApplicationWebServerInitialized(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }



    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 获取服务器端口和协议
        String protocol = "http";
        Integer port = this.port;

        if (port == 443 || port == 8443) { // HTTPS默认端口
            protocol = "https";
        }

        // 构建主机名
        String host = "localhost"; // 默认为localhost
        try {
            host = InetAddress.getLocalHost().getHostAddress(); // 尝试获取本地IP地址
        } catch (UnknownHostException e) {
            // 忽略异常，继续使用localhost
        }

        // 获取Swagger UI路径
        String swaggerUiPath = environment.getProperty("springdoc.swagger-ui.path", "/swagger-ui.html");

        if (isKnife4j) {
            swaggerUiPath = "/doc.html";
        }

        // 构建完整的Swagger URL
        String swaggerUrl = String.format("%s://%s:%d%s", protocol, host, port, swaggerUiPath);

        log.info("----------------------------------------------------------");
        log.info("Swagger 的地址是: " + swaggerUrl);
        log.info("----------------------------------------------------------");
    }
}