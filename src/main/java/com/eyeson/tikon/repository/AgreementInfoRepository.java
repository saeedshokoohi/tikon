package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.AgreementInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AgreementInfo entity.
 */
@SuppressWarnings("unused")
public interface AgreementInfoRepository extends JpaRepository<AgreementInfo,Long> {

}
