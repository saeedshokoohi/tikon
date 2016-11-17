package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServantDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Servant and its DTO ServantDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class,PersonInfoMapper.class} )
public interface ServantMapper {

    @Mapping(source = "personInfo.id", target = "personInfoId")
    ServantDTO servantToServantDTO(Servant servant);

    List<ServantDTO> servantsToServantDTOs(List<Servant> servants);

//    @Mapping(source = "personInfoId", target = "personInfo")
    @Mapping(target = "serviceCategories", ignore = true)
    @Mapping(target = "serviceItems", ignore = true)
    @Mapping(target = "priceInfos", ignore = true)
    Servant servantDTOToServant(ServantDTO servantDTO);

    List<Servant> servantDTOsToServants(List<ServantDTO> servantDTOs);

    default PersonInfo personInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PersonInfo personInfo = new PersonInfo();
        personInfo.setId(id);
        return personInfo;
    }
}
