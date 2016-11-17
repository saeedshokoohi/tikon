package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.Servant;

import com.eyeson.tikon.web.rest.dto.ServantDTO;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Servant entity.
 */
@SuppressWarnings("unused")
public interface ServantRepository extends JpaRepository<Servant,Long> {

    List<Servant> findByCompanyId(Long id);

    List<ServantDTO> findByServiceItemsId(Long serviceItemId);
}
