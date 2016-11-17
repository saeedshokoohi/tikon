package com.eyeson.tikon.web.rest.mapper.decorator;

import com.eyeson.tikon.domain.Company;
import com.eyeson.tikon.web.rest.dto.CompanyDTO;
import com.eyeson.tikon.web.rest.mapper.AgreementInfoMapper;
import com.eyeson.tikon.web.rest.mapper.LocationInfoMapper;
import com.eyeson.tikon.web.rest.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by majid on 10/16/2016.
 */

public abstract class CompanyMapperDecorator implements CompanyMapper {
    @Autowired
    @Qualifier("delegate")
    private CompanyMapper delegate;

    @Autowired
    private LocationInfoMapper locationInfoMapper;
    @Autowired
    private AgreementInfoMapper agreementInfoMapper;

    @Override
    public CompanyDTO companyToCompanyDTO(Company company) {
        CompanyDTO companyDTO=delegate.companyToCompanyDTO(company);
        if(companyDTO !=null) {
            if (company.getLocation() != null && company.getLocation().getId() != null)
                companyDTO.setLocationInfo(locationInfoMapper.locationInfoToLocationInfoDTO(company.getLocation()));
            if (company.getAgreement() != null && company.getAgreement().getId() != null)
                companyDTO.setAgreementInfo(agreementInfoMapper.agreementInfoToAgreementInfoDTO(company.getAgreement()));
        }
        return companyDTO ;
    }
    @Override
    public Company companyDTOToCompany(CompanyDTO companyDTO) {
        Company company=delegate.companyDTOToCompany(companyDTO);
        if(companyDTO != null) {
            if (companyDTO.getLocationInfo() != null && companyDTO.getLocationId() != null)
                company.setLocation(locationInfoMapper.locationInfoDTOToLocationInfo(companyDTO.getLocationInfo()));
            if (companyDTO.getAgreementInfo() != null && companyDTO.getAgreementInfo() != null)
                company.setAgreement(agreementInfoMapper.agreementInfoDTOToAgreementInfo(companyDTO.getAgreementInfo()));
        }
        return company ;
    }
}
