package com.eyeson.tikon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ServiceTimeSessionInfo.
 */
@Entity
@Table(name = "service_time_session_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servicetimesessioninfo")
public class ServiceTimeSessionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private Integer type;

    @ManyToOne
    private ServiceItem serviceItem;

    @ManyToOne
    private ScheduleInfo scheduleInfo;


    @OneToMany(mappedBy = "serviceTimeSessionInfo",cascade = {CascadeType.ALL})

    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceTimeSession> serviceTimeSessions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public Set<ServiceTimeSession> getServiceTimeSessions() {
        return serviceTimeSessions;
    }

    public void setServiceTimeSessions(Set<ServiceTimeSession> serviceTimeSessions) {
        this.serviceTimeSessions = serviceTimeSessions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceTimeSessionInfo serviceTimeSessionInfo = (ServiceTimeSessionInfo) o;
        if(serviceTimeSessionInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceTimeSessionInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceTimeSessionInfo{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }
}
