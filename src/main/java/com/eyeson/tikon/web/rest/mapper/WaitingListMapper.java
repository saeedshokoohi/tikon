package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.WaitingListDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WaitingList and its DTO WaitingListDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WaitingListMapper {

    @Mapping(source = "serviceIte.id", target = "serviceIteId")
    WaitingListDTO waitingListToWaitingListDTO(WaitingList waitingList);

    List<WaitingListDTO> waitingListsToWaitingListDTOs(List<WaitingList> waitingLists);

    @Mapping(source = "serviceIteId", target = "serviceIte")
    WaitingList waitingListDTOToWaitingList(WaitingListDTO waitingListDTO);

    List<WaitingList> waitingListDTOsToWaitingLists(List<WaitingListDTO> waitingListDTOs);

    default ServiceItem serviceItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(id);
        return serviceItem;
    }
}
