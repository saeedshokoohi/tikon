package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.PersonInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Spring Data JPA repository for the PersonInfo entity.
 */
@SuppressWarnings("unused")
public interface PersonInfoRepository extends JpaRepository<PersonInfo,Long> {

    @Query("select distinct personInfo from PersonInfo personInfo left join fetch personInfo.socialNetworkInfos")
    List<PersonInfo> findAllWithEagerRelationships();

    @Query("select personInfo from PersonInfo personInfo left join fetch personInfo.socialNetworkInfos where personInfo.id =:id")
    PersonInfo findOneWithEagerRelationships(@Param("id") Long id);



}
