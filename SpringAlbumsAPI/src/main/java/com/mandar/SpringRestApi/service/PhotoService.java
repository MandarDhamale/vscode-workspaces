package com.mandar.SpringRestApi.service;

import com.mandar.SpringRestApi.model.Photo;
import com.mandar.SpringRestApi.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;


    public Photo save(Photo photo){
        return photoRepository.save(photo);
    }


}
