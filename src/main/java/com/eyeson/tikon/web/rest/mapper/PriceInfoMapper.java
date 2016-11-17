package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.PriceInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PriceInfo and its DTO PriceInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {ServantMapper.class, })
public interface PriceInfoMapper {

    PriceInfoDTO priceInfoToPriceInfoDTO(PriceInfo priceInfo);

    List<PriceInfoDTO> priceInfosToPriceInfoDTOs(List<PriceInfo> priceInfos);

    @Mapping(target = "serviceItems", ignore = true)
    PriceInfo priceInfoDTOToPriceInfo(PriceInfoDTO priceInfoDTO);

    List<PriceInfo> priceInfoDTOsToPriceInfos(List<PriceInfoDTO> priceInfoDTOs);

    default Servant servantFromId(Long id) {
        if (id == null) {
            return null;
        }
        Servant servant = new Servant();
        servant.setId(id);
        return servant;
    }
}
