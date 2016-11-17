package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.CustomerRank;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerRank entity.
 */
@SuppressWarnings("unused")
public interface CustomerRankRepository extends JpaRepository<CustomerRank,Long> {

}
