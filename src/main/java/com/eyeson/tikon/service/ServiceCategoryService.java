package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.repository.ServiceCategoryRepository;
import com.eyeson.tikon.repository.extended.CompanyManagerExtendedRepository;
import com.eyeson.tikon.repository.extended.ServiceCategoryExtendedRepository;
import com.eyeson.tikon.repository.search.ServiceCategorySearchRepository;
import com.eyeson.tikon.web.rest.dto.PriceInfoDtailDTO;
import com.eyeson.tikon.web.rest.dto.ServiceCategoryDTO;
import com.eyeson.tikon.web.rest.dto.SettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceCategoryMapper;
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
 * Service Implementation for managing ServiceCategory.
 */
@Service
@Transactional
public class ServiceCategoryService {

    private final Logger log = LoggerFactory.getLogger(ServiceCategoryService.class);

    @Inject
    private ServiceCategoryRepository serviceCategoryRepository;

    @Inject
    private ServiceCategoryMapper serviceCategoryMapper;

    @Inject
    private ServiceCategorySearchRepository serviceCategorySearchRepository;

    @Inject
    private PersonInfoService personInfoService;

    @Inject
    private CompanyManagerExtendedRepository companyManagerExtendedRepository;

    /**
     * Save a serviceCategory.
     *
     * @param serviceCategoryDTO the entity to save
     * @return the persisted entity
     */
    public ServiceCategoryDTO save(ServiceCategoryDTO serviceCategoryDTO) {
        log.debug("Request to save ServiceCategory : {}", serviceCategoryDTO);

        ServiceCategory serviceCategory = serviceCategoryMapper.serviceCategoryDTOToServiceCategory(serviceCategoryDTO);
        PersonInfo personInfo = personInfoService.getCurrentPersonInfo();
        List<CompanyManager> companyManager =  companyManagerExtendedRepository.getCompanyManagerByPersonInfoID(personInfo.getId());
        if(companyManager.size()>0)
            serviceCategory.setCompany(companyManager.get(0).getCompany());
        serviceCategory = serviceCategoryRepository.save(serviceCategory);
        ServiceCategoryDTO result = serviceCategoryMapper.serviceCategoryToServiceCategoryDTO(serviceCategory);
        serviceCategorySearchRepository.save(serviceCategory);
        return result;
    }



    /**
     *  Get all the serviceCategories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceCategory> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceCategories");
        Page<ServiceCategory> result = serviceCategoryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one serviceCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ServiceCategoryDTO findOne(Long id) {
        log.debug("Request to get ServiceCategory : {}", id);
        ServiceCategory serviceCategory = serviceCategoryRepository.findOneWithEagerRelationships(id);
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.serviceCategoryToServiceCategoryDTO(serviceCategory);
        return serviceCategoryDTO;
    }

    /**
     *  Delete the  serviceCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceCategory : {}", id);
        serviceCategoryRepository.delete(id);
        serviceCategorySearchRepository.delete(id);
    }

    /**
     * Search for the serviceCategory corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceCategory> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceCategories for query {}", query);
        return serviceCategorySearchRepository.search(queryStringQuery(query), pageable);
    }

    @Inject
    ServiceCategoryExtendedRepository serviceCategoryExtendedRepository;
    public List<ServiceCategory> getServiceCategoriesByCurrentCompany() {
        List<ServiceCategory> retList=new ArrayList<>();
        PersonInfo personInfo = personInfoService.getCurrentPersonInfo();
        if(personInfo!=null) {
            List<CompanyManager> companyManager = companyManagerExtendedRepository.getCompanyManagerByPersonInfoID(personInfo.getId());
            if (companyManager.size() > 0)
                retList= serviceCategoryExtendedRepository.getServiceCategoriesByCompanyId(companyManager.get(0).getCompany().getId());
        }
        return  retList;
    }
}
