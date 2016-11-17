package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.AlbumInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AlbumInfo entity.
 */
@SuppressWarnings("unused")
public interface AlbumInfoRepository extends JpaRepository<AlbumInfo,Long> {

}
