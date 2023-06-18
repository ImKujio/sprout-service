package me.kujio.sprout.core.conifg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "token")
public class TokenConfig {
    private static String header;
    private static Long validity;
    private static String secret;

    public void setHeader(String header) {
        TokenConfig.header = header;
    }

    public void setValidity(Long validity) {
        TokenConfig.validity = validity;
    }

    public void setSecret(String secret) {
        TokenConfig.secret = secret;
    }

    public static String getHeader() {
        return header;
    }

    public static Long getValidity() {
        return validity;
    }

    public static String getSecret() {
        return secret;
    }
}
