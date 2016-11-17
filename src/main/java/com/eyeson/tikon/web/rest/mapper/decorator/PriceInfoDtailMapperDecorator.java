package com.eyeson.tikon.web.rest.mapper.decorator;

import com.eyeson.tikon.domain.PriceInfoDtail;
import com.eyeson.tikon.web.rest.dto.PriceInfoDtailDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoDtailMapper;
import com.eyeson.tikon.web.rest.mapper.PriceInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by saeed on 8/23/2016.
 */

public abstract class PriceInfoDtailMapperDecorator implements PriceInfoDtailMapper {
    @Autowired
    @Qualifier("delegate")
    private PriceInfoDtailMapper delegate;

    @Autowired
    private PriceInfoMapper priceInfoMapper ;

    @Override
    public PriceInfoDtailDTO priceInfoDtailToPriceInfoDtailDTO(PriceInfoDtail priceInfoDtail) {
        PriceInfoDtailDTO priceInfoDtailDTO = delegate.priceInfoDtailToPriceInfoDtailDTO(priceInfoDtail);
        if(priceInfoDtail.getPriceInfo()!=null)
            priceInfoDtailDTO.setPriceInfo( priceInfoMapper.priceInfoToPriceInfoDTO( priceInfoDtail.getPriceInfo()));
        return priceInfoDtailDTO ;
    }
}
