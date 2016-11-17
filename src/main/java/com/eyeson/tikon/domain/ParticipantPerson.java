package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ParticipantPerson.
 */
@Entity
@Table(name = "participant_person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "participantperson")
public class ParticipantPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private PersonInfo personInfo;

    @ManyToOne
    private OrderBagServiceItemDtail orderBagItemDtail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public OrderBagServiceItemDtail getOrderBagItemDtail() {
        return orderBagItemDtail;
    }

    public void setOrderBagItemDtail(OrderBagServiceItemDtail orderBagServiceItemDtail) {
        this.orderBagItemDtail = orderBagServiceItemDtail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParticipantPerson participantPerson = (ParticipantPerson) o;
        if(participantPerson.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, participantPerson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParticipantPerson{" +
            "id=" + id +
            '}';
    }
}
