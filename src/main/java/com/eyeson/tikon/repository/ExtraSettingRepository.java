package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ExtraSetting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExtraSetting entity.
 */
@SuppressWarnings("unused")
public interface ExtraSettingRepository extends JpaRepository<ExtraSetting,Long> {

}
