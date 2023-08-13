package me.kujio.sprout.utils;

import me.kujio.sprout.core.conifg.FilesConfig;
import me.kujio.sprout.core.exception.SysException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    /**
     * 上传文件处理
     *
     * @param filePath 文件Path
     * @param file     web文件
     * @return 上传之后的文件路径
     */
    public static String upload(String filePath, MultipartFile file) {
        if (file == null) throw new SysException("上传失败");
        String fullPath = FilesConfig.getUploadFull() + filePath;
        try {
            createDirs(fullPath);
            File destFile = new File(fullPath);
            file.transferTo(destFile);
            return FilesConfig.getUpload() + filePath;
        } catch (IOException e) {
            throw new SysException("上传失败：" + e.getMessage());
        }
    }

    // 获取文件扩展名
    private static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    // 创建中间路径文件夹
    private static void createDirs(String path) throws  IOException {
        Path directory = Paths.get(path).getParent();
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }
}
