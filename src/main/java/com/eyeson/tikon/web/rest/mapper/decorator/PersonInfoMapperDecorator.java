//package com.eyeson.tikon.web.rest.mapper.decorator;
//
//import com.eyeson.tikon.domain.PersonInfo;
//import com.eyeson.tikon.web.rest.dto.PersonInfoDTO;
//import com.eyeson.tikon.web.rest.mapper.LocationInfoMapper;
//import com.eyeson.tikon.web.rest.mapper.PersonInfoMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
///**
// * Created by saeed on 8/23/2016.
// */
//
//public abstract class PersonInfoMapperDecorator implements PersonInfoMapper {
//    @Autowired
//    @Qualifier("delegate")
//    private PersonInfoMapper delegate;
//
//    @Autowired
//    private LocationInfoMapper locationInfoMapper;
//    @Override
//    public PersonInfoDTO personInfoToPersonInfoDTO(PersonInfo personInfo) {
//        PersonInfoDTO personInfoDTO=delegate.personInfoToPersonInfoDTO(personInfo);
//        if(personInfo.getLocation()!=null && personInfo.getLocation().getId()!=null)
//        personInfoDTO.setLocationInfo(locationInfoMapper.locationInfoToLocationInfoDTO(personInfo.getLocation()));
//        return personInfoDTO ;
//    }
//    @Override
//    public PersonInfo personInfoDTOToPersonInfo(PersonInfoDTO personInfoDTO) {
//        PersonInfo personInfo=delegate.personInfoDTOToPersonInfo(personInfoDTO);
//        if(personInfoDTO.getLocationInfo()!=null && personInfoDTO.getLocationId()!=null)
//            personInfo.setLocation( locationInfoMapper.locationInfoDTOToLocationInfo(personInfoDTO.getLocationInfo()));
//        return personInfo ;
//    }
//}
