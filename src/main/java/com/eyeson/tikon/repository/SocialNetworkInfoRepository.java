package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.SocialNetworkInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SocialNetworkInfo entity.
 */
@SuppressWarnings("unused")
public interface SocialNetworkInfoRepository extends JpaRepository<SocialNetworkInfo,Long> {

}
