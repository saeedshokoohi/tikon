package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TimePeriod entity.
 */
public class TimePeriodDTO implements Serializable {

    private Long id;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private Integer duration;

    private Integer gaptime;


    private Set<OffTimeDTO> offtimes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public Integer getGaptime() {
        return gaptime;
    }

    public void setGaptime(Integer gaptime) {
        this.gaptime = gaptime;
    }

    public Set<OffTimeDTO> getOfftimes() {
        return offtimes;
    }

    public void setOfftimes(Set<OffTimeDTO> offTimes) {
        this.offtimes = offTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimePeriodDTO timePeriodDTO = (TimePeriodDTO) o;

        if ( ! Objects.equals(id, timePeriodDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TimePeriodDTO{" +
            "id=" + id +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", duration='" + duration + "'" +
            ", gaptime='" + gaptime + "'" +
            '}';
    }
}
