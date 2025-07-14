package com.mandar.SpringRestApi.service;

import com.mandar.SpringRestApi.model.Album;
import com.mandar.SpringRestApi.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public Album save(Album album){
        return albumRepository.save(album);
    }

    public List<Album> findByOwnerId(long id){
        return albumRepository.findByOwnerId(id);
    }

}
