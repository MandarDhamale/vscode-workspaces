package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.model.Account;
import com.mandar.SpringRestApi.model.Album;
import com.mandar.SpringRestApi.model.Photo;
import com.mandar.SpringRestApi.payload.album.AlbumPayloadDTO;
import com.mandar.SpringRestApi.payload.album.AlbumViewDTO;
import com.mandar.SpringRestApi.service.AccountService;
import com.mandar.SpringRestApi.service.AlbumService;
import com.mandar.SpringRestApi.service.PhotoService;
import com.mandar.SpringRestApi.util.AppUtils.AppUtil;
import com.mandar.SpringRestApi.util.constants.album.AlbumError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album Controller", description = "Controller for album & photo management")
@Slf4j
public class AlbumController {

    static final String PHOTOS_FOLDER_NAME = "photos";
    static final String THUMBNAIL_FOLDER_NAME = "thumbnails";
    static final int THUMBNAIL_WIDTH = 300;



    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/add", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "200", description = "Album created")
    @ApiResponse(responseCode = "400", description = "Please add a valid description")
    @Operation(summary = "Add new album")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO, Authentication authentication) {

        try {

            Album album = new Album();
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            album.setOwner(optionalAccount.get());
            album = albumService.save(album);

            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription());
            System.out.println(albumViewDTO.toString());

            return ResponseEntity.ok(albumViewDTO);

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/albums", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "List of albums")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List album")
    @SecurityRequirement(name = "mrd-api")
    public List<AlbumViewDTO> albums(Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        for (Album album : albumService.findByOwnerId(account.getId())) {
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription()));
        }
        return albums;
    }

    @PostMapping(value = "/{album_id}/upload-photos", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload photos in album")
    @ApiResponse(responseCode = "400", description = "Please check the payload or token")
    @ApiResponse(responseCode = "401", description = "Invalid token error")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<Map<String, List<String>>> addPhotos(@RequestPart(required = true) MultipartFile[] files, @PathVariable long album_id, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != album.getOwner().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);git
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<String> fileNamesWithSuccess = new ArrayList<>();
        List<String> fileNamesWithError = new ArrayList<>();

        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();
            if (contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg")) {
                fileNamesWithSuccess.add(file.getOriginalFilename());
                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;

                try {

                    String fileName = file.getOriginalFilename();
                    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                    String finalPhotoName = generatedString + "_" + fileName;
                    String absoluteFileLocation = AppUtil.getPhotoUploadPath(finalPhotoName, PHOTOS_FOLDER_NAME, album_id);
                    Path path = Paths.get(absoluteFileLocation);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    Photo photo = new Photo();
                    photo.setName(fileName);
                    photo.setFileName(finalPhotoName);
                    photo.setOriginalFileName(fileName);
                    photo.setAlbum(album);
                    photoService.save(photo);
                    System.out.println("[DB Operation] " + photo.toString() + " has been saved to DB");

                    BufferedImage thumbnailImg = AppUtil.getThumbnail(file, THUMBNAIL_WIDTH);
                    File thumbnailLocation = new File(AppUtil.getPhotoUploadPath(finalPhotoName, THUMBNAIL_FOLDER_NAME, album_id));
                    ImageIO.write(thumbnailImg, file.getContentType().split("/")[1], thumbnailLocation);


                } catch (Exception e) {
                    log.debug(AlbumError.PHOTO_UPLOAD_ERROR.toString() + " " + e.getMessage());
                    fileNamesWithError.add(file.getOriginalFilename());
                    throw new RuntimeException(e);
                }
            } else {
                fileNamesWithError.add(file.getOriginalFilename());
            }
        });

        if (!fileNamesWithSuccess.isEmpty() || !fileNamesWithError.isEmpty()) {
            Map<String, List<String>> response = new HashMap<>();
            response.put("successFiles", fileNamesWithSuccess);
            response.put("failedFiles", fileNamesWithError);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


    }

}
