package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ImageData;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ImageData entity.
 */
@SuppressWarnings("unused")
public interface ImageDataRepository extends JpaRepository<ImageData,Long> {

    List<ImageData> findByAlbumInfoId(Long albumid);
}
