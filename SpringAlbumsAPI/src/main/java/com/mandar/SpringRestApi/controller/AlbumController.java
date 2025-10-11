package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.model.Account;
import com.mandar.SpringRestApi.model.Album;
import com.mandar.SpringRestApi.model.Photo;
import com.mandar.SpringRestApi.payload.album.*;
import com.mandar.SpringRestApi.service.AccountService;
import com.mandar.SpringRestApi.service.AlbumService;
import com.mandar.SpringRestApi.service.PhotoService;
import com.mandar.SpringRestApi.util.AppUtils.AppUtil;
import com.mandar.SpringRestApi.util.constants.account.AccountSuccess;
import com.mandar.SpringRestApi.util.constants.album.AlbumError;
import com.mandar.SpringRestApi.util.constants.album.AlbumSuccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album Controller", description = "Controller for album & photo management")
@Slf4j
//@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
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

            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), null);
            System.out.println(albumViewDTO.toString());

            return ResponseEntity.ok(albumViewDTO);

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping(value = "/albums/{album_id}/update", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add a valid description")
    @ApiResponse(responseCode = "204", description = "Album updated")
    @Operation(summary = "Update album")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<AlbumViewDTO> updateAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO, @PathVariable long album_id, Authentication authentication) {

        try {

            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getOwner().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            album = albumService.save(album);

            List<PhotoDTO> photos = new ArrayList<>();

            for (Photo photo : photoService.findByAlbumId(album_id)) {
                String link = link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link));
            }

            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos);
            return ResponseEntity.ok(albumViewDTO);


        } catch (Exception e) {
            log.debug(AlbumError.ALBUM_UPDATE_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PutMapping(value = "/albums/{album_id}/photos/{photo_id}/update", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add a valid description")
    @ApiResponse(responseCode = "204", description = "Photo updated")
    @Operation(summary = "Update photo")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<PhotoViewDTO> updatePhoto(@Valid @RequestBody PhotoPayloadDTO photoPayloadDTO, @PathVariable long album_id, @PathVariable long photo_id, Authentication authentication) {

        try {

            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getOwner().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Optional<Photo> optionalPhoto = photoService.findById(photo_id);
            Photo photo;

            if (optionalPhoto.isPresent()) {
                photo = optionalPhoto.get();

                if (photo.getAlbum().getId() != album_id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                photo.setName(photoPayloadDTO.getName());
                photo.setDescription(photoPayloadDTO.getDescription());
                photo = photoService.save(photo);

                PhotoViewDTO photoViewDTO = new PhotoViewDTO(photo.getId(), photo.getName(), photo.getDescription());
                return ResponseEntity.ok(photoViewDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            log.debug(AlbumError.PHOTO_UPDATE_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @DeleteMapping(value = "albums/{album_id}/photos/{photo_id}/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponse(responseCode = "202", description = "Photo delete")
    @Operation(summary = "Delete a photo")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<Map<String, String>> deletePhoto(@PathVariable long album_id, @PathVariable long photo_id, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != album.getOwner().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            if (photo.getAlbum().getId() != album_id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            AppUtil.deletePhotoFromPath(photo.getFileName(), PHOTOS_FOLDER_NAME, album_id);
            AppUtil.deletePhotoFromPath(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);
            photoService.delete(photo);

            Map<String, String> response = new HashMap<>();
            response.put("message", AlbumSuccess.PHOTO_DELETED.toString());
            return ResponseEntity.ok(response);

        } else {
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
            List<PhotoDTO> photos = new ArrayList<>();
            for (Photo photo : photoService.findByAlbumId(album.getId())) {
                String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link));
            }
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos));
        }
        return albums;
    }

    @GetMapping(value = "/albums/{album_id}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of albums by album id")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List album by album id")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<AlbumViewDTO> albumsByAlbumId(@PathVariable long album_id, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (account.getId() != album.getOwner().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<PhotoDTO> photos = new ArrayList<>();
        for (Photo photo : photoService.findByAlbumId(album_id)) {
            String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
            photos.add(new PhotoDTO(photo.getId(), photo.getName(), album.getDescription(), photo.getFileName(), link));
        }

        AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos);
        return ResponseEntity.ok(albumViewDTO);

    }


    @PostMapping(value = "/{album_id}/upload-photos", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload photos in album")
    @ApiResponse(responseCode = "400", description = "Please check the payload or token")
    @ApiResponse(responseCode = "401", description = "Invalid token error")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<List<HashMap<String, List<?>>>> addPhotos(@RequestPart(required = true) MultipartFile[] files, @PathVariable long album_id, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;

        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != album.getOwner().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
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
//            Map<String, List<String>> response = new HashMap<>();
            HashMap<String, List<?>> result = new HashMap<>();
            result.put("successFiles", fileNamesWithSuccess);
            result.put("failedFiles", fileNamesWithError);

            List<HashMap<String, List<?>>> response = new ArrayList<>();
            response.add(result);


            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


    }

    @GetMapping("albums/{album_id}/photos/{photo_id}/download-photo")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<?> downloadPhoto(@PathVariable("album_id") long album_id, @PathVariable("photo_id") long photo_id, Authentication authentication) {

        return downloadFile(album_id, photo_id, PHOTOS_FOLDER_NAME, authentication);

    }

    @GetMapping("albums/{album_id}/photos/{photo_id}/download-thumbnail")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<?> downloadThumbnail(@PathVariable("album_id") long album_id, @PathVariable("photo_id") long photo_id, Authentication authentication) {


        return downloadFile(album_id, photo_id, THUMBNAIL_FOLDER_NAME, authentication);

    }


    @DeleteMapping(value = "albums/{album_id}/delete")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "202", description = "Album deleted")
    @Operation(summary = "Delete a photo")
    @SecurityRequirement(name = "mrd-api")
    public ResponseEntity<Map<String, String>> deleteAlbum(@PathVariable long album_id, Authentication authentication){

        try{
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if(optionalAlbum.isPresent()){
                album = optionalAlbum.get();
                if(account.getId() != album.getOwner().getId()){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            for(Photo photo: photoService.findByAlbumId(album.getId())){
                AppUtil.deletePhotoFromPath(photo.getFileName(), PHOTOS_FOLDER_NAME, album_id);
                AppUtil.deletePhotoFromPath(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);
                photoService.delete(photo);
            }
            albumService.delete(album);
            Map<String, String> response = new HashMap<>();
            response.put("message", AlbumSuccess.ALBUM_DELETED.toString());
            return ResponseEntity.ok(response);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


    }

    private ResponseEntity<?> downloadFile(long album_id, long photo_id, String folderName, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != album.getOwner().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();

            if (photo.getAlbum().getId() != album_id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            Resource resource = null;

            try {
                resource = AppUtil.getFileAsResource(album_id, folderName, photo.getFileName());
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            }

            if (resource == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }

            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

}
