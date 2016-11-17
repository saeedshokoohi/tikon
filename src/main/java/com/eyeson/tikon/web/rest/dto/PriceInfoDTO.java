package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.PriceType;

/**
 * A DTO for the PriceInfo entity.
 */
public class PriceInfoDTO implements Serializable {

    private Long id;

    private ZonedDateTime fromValidDate;

    private PriceType priceType;


    private Set<ServantDTO> servants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getFromValidDate() {
        return fromValidDate;
    }

    public void setFromValidDate(ZonedDateTime fromValidDate) {
        this.fromValidDate = fromValidDate;
    }
    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public Set<ServantDTO> getServants() {
        return servants;
    }

    public void setServants(Set<ServantDTO> servants) {
        this.servants = servants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PriceInfoDTO priceInfoDTO = (PriceInfoDTO) o;

        if ( ! Objects.equals(id, priceInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceInfoDTO{" +
            "id=" + id +
            ", fromValidDate='" + fromValidDate + "'" +
            ", priceType='" + priceType + "'" +
            '}';
    }
}
