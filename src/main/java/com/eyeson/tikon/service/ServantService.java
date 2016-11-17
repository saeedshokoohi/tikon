package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.Company;
import com.eyeson.tikon.domain.PersonInfo;
import com.eyeson.tikon.domain.Servant;
import com.eyeson.tikon.repository.PersonInfoRepository;
import com.eyeson.tikon.repository.ServantRepository;
import com.eyeson.tikon.repository.search.ServantSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServantDTO;
import com.eyeson.tikon.web.rest.mapper.PersonInfoMapper;
import com.eyeson.tikon.web.rest.mapper.ServantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Servant.
 */
@Service
@Transactional
public class ServantService {

    private final Logger log = LoggerFactory.getLogger(ServantService.class);

    @Inject
    private ServantRepository servantRepository;

    @Inject
    private ServantMapper servantMapper;

    @Inject
    private ServantSearchRepository servantSearchRepository;

    @Inject
    private CompanyService companyService;

    @Inject
    private PersonInfoRepository personInfoRepository;

   @Inject
    private PersonInfoMapper personInfoMapper;

    /**
     * Save a servant.
     *
     * @param servantDTO the entity to save
     * @return the persisted entity
     */
    public ServantDTO save(ServantDTO servantDTO) {
        log.debug("Request to save Servant : {}", servantDTO);
        PersonInfo personInfo = SavePersonInfo(servantDTO);
        Servant servant = servantMapper.servantDTOToServant(servantDTO);
        if(personInfo != null)
        {
            servant.setPersonInfo(personInfo);
        }
        Company company = companyService.getCurrentCompanyInfo();
        if(company != null)
        {
            servant.setCompany(company);
        }
        servant = servantRepository.save(servant);
        ServantDTO result = servantMapper.servantToServantDTO(servant);
        servantSearchRepository.save(servant);
        return result;
    }

    private PersonInfo SavePersonInfo(ServantDTO servantDTO) {

        if(servantDTO==null || servantDTO.getPersonInfo()==null )return  null;
        boolean isNewPersonInfo =  servantDTO.getPersonInfo().getId()==null;

        if(isNewPersonInfo)
        {
            PersonInfo personInfo = new PersonInfo();
            personInfo = personInfoMapper.personInfoDTOToPersonInfo(servantDTO.getPersonInfo()) ;
            personInfoRepository.save(personInfo);

            return  personInfo;
        }
        else
        {
            PersonInfo personInfo = personInfoMapper.personInfoDTOToPersonInfo(servantDTO.getPersonInfo()) ;
            personInfoRepository.save(personInfo);
            return personInfo;
        }

    }

    /**
     *  Get all the servants.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Servant> findAll(Pageable pageable) {
        log.debug("Request to get all Servants");
        Page<Servant> result = servantRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one servant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ServantDTO findOne(Long id) {
        log.debug("Request to get Servant : {}", id);
        Servant servant = servantRepository.findOne(id);
        ServantDTO servantDTO = servantMapper.servantToServantDTO(servant);
        return servantDTO;
    }

    /**
     *  Delete the  servant by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Servant : {}", id);
        servantRepository.delete(id);
        servantSearchRepository.delete(id);
    }

    /**
     * Search for the servant corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Servant> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Servants for query {}", query);
        return servantSearchRepository.search(queryStringQuery(query), pageable);
    }


    /**
     *  servants  by Companyid.
     *
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public List<ServantDTO> findByCompanyId() {
        log.debug("Request to get Servant : {}");
        Company company = companyService.getCurrentCompanyInfo();
        List<ServantDTO> servantDTO=new ArrayList<>();
        if(company!=null) {
            List<Servant> servant = servantRepository.findByCompanyId(company.getId());
             servantDTO = servantMapper.servantsToServantDTOs(servant);
        }
        return servantDTO;
    }


}
