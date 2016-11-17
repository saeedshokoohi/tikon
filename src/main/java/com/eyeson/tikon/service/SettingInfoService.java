package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.Company;
import com.eyeson.tikon.domain.CompanyManager;
import com.eyeson.tikon.domain.PersonInfo;
import com.eyeson.tikon.domain.SettingInfo;
import com.eyeson.tikon.repository.CompanyRepository;
import com.eyeson.tikon.repository.SettingInfoRepository;
import com.eyeson.tikon.repository.extended.CompanyManagerExtendedRepository;
import com.eyeson.tikon.repository.extended.SettingInfoExtendedRepository;
import com.eyeson.tikon.repository.search.SettingInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.SettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.SettingInfoMapper;
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
 * Service Implementation for managing SettingInfo.
 */
@Service
@Transactional
public class SettingInfoService {

    private final Logger log = LoggerFactory.getLogger(SettingInfoService.class);

    @Inject
    private SettingInfoRepository settingInfoRepository;

    @Inject
    private SettingInfoMapper settingInfoMapper;

    @Inject
    private SettingInfoSearchRepository settingInfoSearchRepository;

    @Inject
    private PersonInfoService personInfoService ;

    /**
     * Save a settingInfo.
     *
     * @param settingInfoDTO the entity to save
     * @return the persisted entity
     */
    public SettingInfoDTO save(SettingInfoDTO settingInfoDTO) {
        log.debug("Request to save SettingInfo : {}", settingInfoDTO);
        SettingInfo settingInfo = settingInfoMapper.settingInfoDTOToSettingInfo(settingInfoDTO);
        settingInfo = settingInfoRepository.save(settingInfo);
        SettingInfoDTO result = settingInfoMapper.settingInfoToSettingInfoDTO(settingInfo);
        settingInfoSearchRepository.save(settingInfo);
        return result;
    }

    /**
     *  Get all the settingInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SettingInfo> findAll(Pageable pageable) {
        log.debug("Request to get all SettingInfos");
        Page<SettingInfo> result = settingInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one settingInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SettingInfoDTO findOne(Long id) {
        log.debug("Request to get SettingInfo : {}", id);
        SettingInfo settingInfo = settingInfoRepository.findOne(id);
        SettingInfoDTO settingInfoDTO = settingInfoMapper.settingInfoToSettingInfoDTO(settingInfo);
        return settingInfoDTO;
    }

    /**
     *  Delete the  settingInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SettingInfo : {}", id);
        settingInfoRepository.delete(id);
        settingInfoSearchRepository.delete(id);
    }

    /**
     * Search for the settingInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SettingInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SettingInfos for query {}", query);
        return settingInfoSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Inject
    CompanyManagerExtendedRepository companyManagerExtendedRepository;
    @Inject
    SettingInfoExtendedRepository settingInfoExtendedRepository;

    public SettingInfo getSettingInfoByCurrentCompany() {

        SettingInfo retSettingInfo=new SettingInfo();
        PersonInfo personInfo = personInfoService.getCurrentPersonInfo();
        List<CompanyManager> companyManager  = companyManagerExtendedRepository.getCompanyManagerByPersonInfoID(personInfo.getId());
        if(companyManager.size()>0) {
            List<SettingInfo> retList = settingInfoExtendedRepository.getSettingInfoByCompanyId(companyManager.get(0).getCompany().getId());
            if(retList.size()>0)
                retSettingInfo=retList.get(0);
        }
        return  retSettingInfo;
    }

    @Inject
    CompanyService companyService;
    @Inject
    CompanyRepository companyRepository;

    public SettingInfoDTO saveWithCompany(SettingInfoDTO settingInfoDTO) {
        log.debug("Request to save SettingInfo : {}", settingInfoDTO);
        SettingInfo settingInfo = settingInfoMapper.settingInfoDTOToSettingInfo(settingInfoDTO);
        settingInfo = settingInfoRepository.save(settingInfo);
        Company company = companyService.getCurrentCompanyInfo();
        if(company!=null)
        {
            if(settingInfo.getId()!=null)
            {
                company.setSetting(settingInfo);
                companyRepository.save(company);
            }
        }

        SettingInfoDTO result = settingInfoMapper.settingInfoToSettingInfoDTO(settingInfo);
        settingInfoSearchRepository.save(settingInfo);
        return result;
    }
}
