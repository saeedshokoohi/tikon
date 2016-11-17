package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.WaitingList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WaitingList entity.
 */
@SuppressWarnings("unused")
public interface WaitingListRepository extends JpaRepository<WaitingList,Long> {

}
