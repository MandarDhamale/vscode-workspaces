package com.mandar.SpringRestApi.util.AppUtils;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

}
