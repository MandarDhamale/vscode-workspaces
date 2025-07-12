package com.mandar.SpringRestApi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album Controller", description = "Controller for album & photo management")
@Slf4j
public class AlbumController {

    public String addAlbum(){
        return null;
    }


}
