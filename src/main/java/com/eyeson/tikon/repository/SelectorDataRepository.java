package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.SelectorData;

import com.eyeson.tikon.web.rest.dto.SelectorDataDTO;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SelectorData entity.
 */
@SuppressWarnings("unused")
public interface SelectorDataRepository extends JpaRepository<SelectorData,Long> {


    List<SelectorData> findByTypeKey(String typeKey);

    List<SelectorData> findByTypeKeyAndParentId(String typeKey, Long parentId);
}
