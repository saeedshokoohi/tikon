package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.CustomerComment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerComment entity.
 */
@SuppressWarnings("unused")
public interface CustomerCommentRepository extends JpaRepository<CustomerComment,Long> {

}
