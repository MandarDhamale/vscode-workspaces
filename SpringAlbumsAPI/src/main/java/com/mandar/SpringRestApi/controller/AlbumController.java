package com.mandar.SpringRestApi.controller;

import com.mandar.SpringRestApi.model.Account;
import com.mandar.SpringRestApi.model.Album;
import com.mandar.SpringRestApi.payload.album.AlbumPayloadDTO;
import com.mandar.SpringRestApi.payload.album.AlbumViewDTO;
import com.mandar.SpringRestApi.service.AccountService;
import com.mandar.SpringRestApi.service.AlbumService;
import com.mandar.SpringRestApi.util.constants.album.AlbumError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album Controller", description = "Controller for album & photo management")
@Slf4j
public class AlbumController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;


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

    @GetMapping(value = "/albums", produces = "application/json", consumes = "application/json")
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

    @PostMapping(value = "/photos", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload photos in album")
    @SecurityRequirement(name = "mrd-api")
    public List<String> addPhotos(@RequestPart(required = true) MultipartFile[] files){

        List<String> fileNames = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file -> {fileNames.add(file.getOriginalFilename());});
        return fileNames;
    }



}
