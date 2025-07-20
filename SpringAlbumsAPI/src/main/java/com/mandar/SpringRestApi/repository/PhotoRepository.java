package com.mandar.SpringRestApi.repository;

import com.mandar.SpringRestApi.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {


}
