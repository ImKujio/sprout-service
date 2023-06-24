package me.kujio.sprout.core.conifg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "files")
public class FilesConfig {
    private static String upload;
    private static String download;
    private static String resource;

    public void setUpload(String upload) {
        FilesConfig.upload = upload;
    }

    public void setDownload(String downloag) {
        FilesConfig.download = downloag;
    }

    public void setResource(String resource) {
        FilesConfig.resource = resource;
    }


    public static String getUpload() {
        return upload;
    }

    public static String getDownload() {
        return download;
    }

    public static String getResource() {
        return resource;
    }
}
