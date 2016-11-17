package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.FinancialSetting;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FinancialSetting entity.
 */
@SuppressWarnings("unused")
public interface FinancialSettingRepository extends JpaRepository<FinancialSetting,Long> {

}
