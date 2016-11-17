package com.eyeson.tikon.web.rest.mapper.decorator;

import com.eyeson.tikon.domain.PriceInfoDtail;
import com.eyeson.tikon.domain.ServiceOptionItem;
import com.eyeson.tikon.repository.extended.PriceInfoDtailExtendedRepository;
import com.eyeson.tikon.web.rest.dto.ServiceOptionItemDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoDtailMapper;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionItemMapper;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionInfoMapper;
import com.eyeson.tikon.repository.PriceInfoDtailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


/**
 * Created by majid on 10/28/2016.
 */

public abstract class ServiceOptionItemMapperDecorator implements ServiceOptionItemMapper {
    @Autowired
    @Qualifier("delegate")
    private ServiceOptionItemMapper delegate;

    @Autowired
    private ServiceOptionInfoMapper serviceOptionInfoMapper ;

    @Autowired
    private PriceInfoDtailExtendedRepository priceInfoDtailExtendedRepository;

    @Autowired
    private PriceInfoDtailMapper priceInfoDtailMapper;


    @Override
    public ServiceOptionItemDTO serviceOptionItemToServiceOptionItemDTO(ServiceOptionItem serviceOptionItem) {
        ServiceOptionItemDTO serviceOptionItemDTO = delegate.serviceOptionItemToServiceOptionItemDTO(serviceOptionItem);
        if(serviceOptionItem.getOptionInfo()!=null)
            serviceOptionItemDTO.setOptionInfo( serviceOptionInfoMapper.serviceOptionInfoToServiceOptionInfoDTO( serviceOptionItem.getOptionInfo()));
        if(serviceOptionItem.getPriceInfo()!=null && serviceOptionItem.getPriceInfo().getId() != null) {

            List<PriceInfoDtail> priceInfoDtails =  priceInfoDtailExtendedRepository.getPriceInfoDtailsByPriceInfo(serviceOptionItem.getPriceInfo().getId());
            if(priceInfoDtails.size()>0)
            {
                serviceOptionItemDTO.setPriceInfoDtail( priceInfoDtailMapper.priceInfoDtailToPriceInfoDtailDTO( priceInfoDtails.get(0)));
            }
        }

        return serviceOptionItemDTO;
    }

}
