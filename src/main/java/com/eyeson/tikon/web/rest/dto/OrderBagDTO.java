package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the OrderBag entity.
 */
public class OrderBagDTO implements Serializable {

    private Long id;

    private ZonedDateTime createDate;

    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderBagDTO orderBagDTO = (OrderBagDTO) o;

        if ( ! Objects.equals(id, orderBagDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagDTO{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
