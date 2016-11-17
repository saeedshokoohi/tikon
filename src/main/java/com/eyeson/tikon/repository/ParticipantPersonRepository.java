package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.ParticipantPerson;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParticipantPerson entity.
 */
@SuppressWarnings("unused")
public interface ParticipantPersonRepository extends JpaRepository<ParticipantPerson,Long> {

}
