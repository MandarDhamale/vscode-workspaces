package com.mandar.SpringRestApi.repository;

import com.mandar.SpringRestApi.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByOwnerId(long id);

}
