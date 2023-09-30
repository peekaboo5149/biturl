package org.nerds.biturl.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WebConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        final WebServer webServer = event.getWebServer();
        int port = webServer.getPort();
        log.info("Server port = " + port);
        System.setProperty("server.port", String.valueOf(port));
    }
}
