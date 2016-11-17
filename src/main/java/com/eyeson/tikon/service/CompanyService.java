package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.Company;
import com.eyeson.tikon.domain.CompanyManager;
import com.eyeson.tikon.domain.PersonInfo;
import com.eyeson.tikon.repository.CompanyRepository;
import com.eyeson.tikon.repository.PersonInfoRepository;
import com.eyeson.tikon.repository.extended.CompanyManagerExtendedRepository;
import com.eyeson.tikon.repository.search.CompanySearchRepository;
import com.eyeson.tikon.web.rest.dto.CompanyDTO;
import com.eyeson.tikon.web.rest.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanyMapper companyMapper;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private PersonInfoService personInfoService;

    @Inject
    private CompanyManagerExtendedRepository companyManagerExtendedRepository;

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save
     * @return the persisted entity
     */
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.companyToCompanyDTO(company);
        companySearchRepository.save(company);
        return result;
    }

    /**
     *  Get all the companies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        Page<Company> result = companyRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one company by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CompanyDTO findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Company company = companyRepository.findOneWithEagerRelationships(id);
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);
        return companyDTO;
    }

    /**
     *  Delete the  company by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.delete(id);
        companySearchRepository.delete(id);
    }

    /**
     * Search for the company corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Company> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Companies for query {}", query);
        return companySearchRepository.search(queryStringQuery(query), pageable);
    }


    public Company getCurrentCompanyInfo() {

        PersonInfo personInfo = personInfoService.getCurrentPersonInfo();

        if(personInfo!=null) {
            List<CompanyManager> companyManager = companyManagerExtendedRepository.getCompanyManagerByPersonInfoID(personInfo.getId());


            if (companyManager.size() > 0) {
                return companyManager.get(0).getCompany();
            }
        }
        return null;
    }



}
