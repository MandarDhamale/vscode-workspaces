package com.mandar.SpringRestApi.util.AppUtils;

import org.springframework.core.io.Resource;
import org.imgscalr.Scalr;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppUtil {

    public static String getPhotoUploadPath(String fileName, String folderName, long album_id) throws IOException {
        String path = "src/main/resources/static/uploads/" + album_id + "/" + folderName;
        Files.createDirectories(Paths.get(path));
        return new File(path).getAbsolutePath() + "/" + fileName;
    }

    public static BufferedImage getThumbnail(MultipartFile orignalFile, Integer width) throws IOException{

        BufferedImage thumbnailImg = null;
        BufferedImage img = ImageIO.read(orignalFile.getInputStream());
        thumbnailImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        return thumbnailImg;

    }

    public static Resource getFileAsResource(long album_id, String folder_name, String file_name) throws IOException {
        String location = "src/main/resources/static/uploads/" + album_id + "/" + folder_name + "/" + file_name;
        Path filePath = Paths.get(location).toAbsolutePath().normalize();

        if (!Files.exists(filePath)) {
            throw new IOException("File not found at: " + filePath);
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IOException("File is not readable: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new IOException("Malformed file path: " + filePath, e);
        }
    }



}
