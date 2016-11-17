package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.PriceInfoDtail;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PriceInfoDtail entity.
 */
@SuppressWarnings("unused")
public interface PriceInfoDtailRepository extends JpaRepository<PriceInfoDtail,Long> {

}
