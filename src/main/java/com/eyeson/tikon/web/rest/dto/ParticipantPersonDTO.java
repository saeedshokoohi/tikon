package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ParticipantPerson entity.
 */
public class ParticipantPersonDTO implements Serializable {

    private Long id;


    private Long personInfoId;
    
    private Long orderBagItemDtailId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonInfoId() {
        return personInfoId;
    }

    public void setPersonInfoId(Long personInfoId) {
        this.personInfoId = personInfoId;
    }

    public Long getOrderBagItemDtailId() {
        return orderBagItemDtailId;
    }

    public void setOrderBagItemDtailId(Long orderBagServiceItemDtailId) {
        this.orderBagItemDtailId = orderBagServiceItemDtailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParticipantPersonDTO participantPersonDTO = (ParticipantPersonDTO) o;

        if ( ! Objects.equals(id, participantPersonDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParticipantPersonDTO{" +
            "id=" + id +
            '}';
    }
}
