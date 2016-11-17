package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ParticipantPersonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ParticipantPerson and its DTO ParticipantPersonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParticipantPersonMapper {

    @Mapping(source = "personInfo.id", target = "personInfoId")
    @Mapping(source = "orderBagItemDtail.id", target = "orderBagItemDtailId")
    ParticipantPersonDTO participantPersonToParticipantPersonDTO(ParticipantPerson participantPerson);

    List<ParticipantPersonDTO> participantPeopleToParticipantPersonDTOs(List<ParticipantPerson> participantPeople);

    @Mapping(source = "personInfoId", target = "personInfo")
    @Mapping(source = "orderBagItemDtailId", target = "orderBagItemDtail")
    ParticipantPerson participantPersonDTOToParticipantPerson(ParticipantPersonDTO participantPersonDTO);

    List<ParticipantPerson> participantPersonDTOsToParticipantPeople(List<ParticipantPersonDTO> participantPersonDTOs);

    default PersonInfo personInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PersonInfo personInfo = new PersonInfo();
        personInfo.setId(id);
        return personInfo;
    }

    default OrderBagServiceItemDtail orderBagServiceItemDtailFromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderBagServiceItemDtail orderBagServiceItemDtail = new OrderBagServiceItemDtail();
        orderBagServiceItemDtail.setId(id);
        return orderBagServiceItemDtail;
    }
}
