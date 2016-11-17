package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.SelectorDataType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SelectorDataType entity.
 */
@SuppressWarnings("unused")
public interface SelectorDataTypeRepository extends JpaRepository<SelectorDataType,Long> {

}
