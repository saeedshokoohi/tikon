package com.eyeson.tikon.web.rest.mapper.decorator;

import com.eyeson.tikon.domain.PriceInfo;
import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.domain.ServiceCapacityInfo;
import com.eyeson.tikon.domain.ServiceItem;
import com.eyeson.tikon.web.rest.dto.PersonInfoDTO;
import com.eyeson.tikon.web.rest.dto.PriceInfoDTO;
import com.eyeson.tikon.web.rest.dto.ServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by saeed on 8/23/2016.
 */

public abstract class ServiceItemMapperDecorator implements ServiceItemMapper {
    @Autowired
    @Qualifier("delegate")
    private  ServiceItemMapper delegate;


    @Autowired
    private ScheduleInfoMapper scheduleInfoMapper;

    @Autowired
    private PriceInfoMapper priceInfoMapper;

    @Autowired
    private ServiceCapacityInfoMapper serviceCapacityInfoMapper;

    @Autowired
    private AgreementInfoMapper agreementInfoMapper;


    @Override
    public ServiceItemDTO serviceItemToServiceItemDTO(ServiceItem serviceItem) {
        ServiceItemDTO serviceItemDTO = delegate.serviceItemToServiceItemDTO(serviceItem);
        if(serviceItem.getServiceTimes().size()>0)
            serviceItemDTO.setScheduleInfo(scheduleInfoMapper.scheduleInfoToScheduleInfoDTO((ScheduleInfo) serviceItem.getServiceTimes().toArray()[0]));
        if(serviceItem.getServicePriceInfo().size()>0)
            serviceItemDTO.setServicePriceInfo(PriceInfoSetToPriceInfoDTOSet(serviceItem.getServicePriceInfo()));
        if(serviceItem.getCapacityInfo() != null)
            serviceItemDTO.setCapacityInfo(serviceCapacityInfoMapper.serviceCapacityInfoToServiceCapacityInfoDTO(serviceItem.getCapacityInfo()));
//        if(serviceItem.getAgreement() != null)
//            serviceItemDTO.setAgreementInfo(agreementInfoMapper.agreementInfoToAgreementInfoDTO(serviceItem.getAgreement()));
        return  serviceItemDTO;
    }

    @Override
    public ServiceItem serviceItemDTOToServiceItem(ServiceItemDTO serviceItemDTO) {
        ServiceItem serviceItem = delegate.serviceItemDTOToServiceItem(serviceItemDTO);
        if(serviceItemDTO.getCapacityInfo() != null)
            serviceItem.setCapacityInfo(serviceCapacityInfoMapper.serviceCapacityInfoDTOToServiceCapacityInfo(serviceItemDTO.getCapacityInfo()));
        return  serviceItem;
    }

    protected Set<PriceInfoDTO> PriceInfoSetToPriceInfoDTOSet(Set<PriceInfo> set) {
        if ( set == null ) {
            return null;
        }

        Set<PriceInfoDTO> set_ = new HashSet<PriceInfoDTO>();
        for ( PriceInfo priceInfo : set ) {
            set_.add( priceInfoMapper.priceInfoToPriceInfoDTO( priceInfo ) );
        }

        return set_;
    }

    protected Set<PriceInfo> priceInfoDTOSetToPriceInfoSet(Set<PriceInfoDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<PriceInfo> set_ = new HashSet<PriceInfo>();
        for ( PriceInfoDTO priceInfoDTO : set ) {
            set_.add( priceInfoMapper.priceInfoDTOToPriceInfo ( priceInfoDTO ) );
        }

        return set_;
    }


}
