package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.repository.LocationInfoRepository;
import com.eyeson.tikon.repository.PersonInfoRepository;
import com.eyeson.tikon.repository.UserRepository;
import com.eyeson.tikon.repository.search.PersonInfoSearchRepository;
import com.eyeson.tikon.security.SecurityUtils;
import com.eyeson.tikon.web.rest.dto.PersonInfoDTO;
import com.eyeson.tikon.web.rest.mapper.LocationInfoMapper;
import com.eyeson.tikon.web.rest.mapper.PersonInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PersonInfo.
 */
@Service
@Transactional
public class PersonInfoService {

    private final Logger log = LoggerFactory.getLogger(PersonInfoService.class);

    @Inject
    private PersonInfoRepository personInfoRepository;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PersonInfoMapper personInfoMapper;

    @Inject
    private PersonInfoSearchRepository personInfoSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private LocationInfoMapper locationInfoMapper;

    @Inject
    private LocationInfoRepository locationInfoRepository;

    @Inject
    private  MailService mailservice ;

    /**
     * Save a personInfo.
     *
     * @param personInfoDTO the entity to save
     * @return the persisted entity
     */
    public PersonInfoDTO save(PersonInfoDTO personInfoDTO) {
        // Edited by majid

        log.debug("Request to save PersonInfo : {}", personInfoDTO);

        LocationInfo locationInfo = saveLocationInfo(personInfoDTO);    // majid

        PersonInfo personInfo = personInfoMapper.personInfoDTOToPersonInfo(personInfoDTO);
        personInfo.setLocation(locationInfo); // majid

        personInfo = personInfoRepository.save(personInfo);
        PersonInfoDTO result = personInfoMapper.personInfoToPersonInfoDTO(personInfo);
        personInfoSearchRepository.save(personInfo);
        return result;
    }

    private LocationInfo saveLocationInfo(PersonInfoDTO personInfoDTO )
    {
//        if(personInfoDTO.getLocationInfo() !=null && personInfoDTO.getLocationInfo().getId()!=null)
//        {
//
//        }
       LocationInfo locationInfo = locationInfoMapper.locationInfoDTOToLocationInfo(personInfoDTO.getLocation());
       locationInfo = locationInfoRepository.save(locationInfo);

        return  locationInfo;
    }


    /**
     *  Get all the personInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonInfo> findAll(Pageable pageable) {
        log.debug("Request to get all PersonInfos");
        Page<PersonInfo> result = personInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one personInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PersonInfoDTO findOne(Long id) {
        log.debug("Request to get PersonInfo : {}", id);
        PersonInfo personInfo = personInfoRepository.findOneWithEagerRelationships(id);
        PersonInfoDTO personInfoDTO = personInfoMapper.personInfoToPersonInfoDTO(personInfo);
        return personInfoDTO;
    }

    /**
     *  Delete the  personInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonInfo : {}", id);
        personInfoRepository.delete(id);
        personInfoSearchRepository.delete(id);
    }

    /**
     * Search for the personInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PersonInfos for query {}", query);
        return personInfoSearchRepository.search(queryStringQuery(query), pageable);
    }

    public PersonInfo getCurrentPersonInfo() {

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        String userid = user.getId().toString();

        List<Customer> l = em.createQuery("select sd from Customer sd where sd.userAccountId  = :userid ").setParameter("userid",user.getId()).getResultList();

        if(l.size()>0)
        {
            return l.get(0).getPersonalInfo();
        }

        return null;
    }



}
