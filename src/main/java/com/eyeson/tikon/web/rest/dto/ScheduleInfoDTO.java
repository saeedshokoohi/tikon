package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.WeeklyScheduleInfo;
import com.eyeson.tikon.domain.enumeration.ScheduleType;

/**
 * A DTO for the ScheduleInfo entity.
 */
public class ScheduleInfoDTO implements Serializable {

    private Long id;

    private ScheduleType scheduleType;
    TimingInfoDTO timingInfo;


    private Long weeklyScheduleInfoId;
    private WeeklyScheduleInfoDTO weeklyScheduleInfo=new WeeklyScheduleInfoDTO();
    private Long serviceItemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Long getWeeklyScheduleInfoId() {
        return weeklyScheduleInfoId;
    }

    public void setWeeklyScheduleInfoId(Long weeklyScheduleInfoId) {
        this.weeklyScheduleInfoId = weeklyScheduleInfoId;
    }

    public WeeklyScheduleInfoDTO getWeeklyScheduleInfo() {
        return weeklyScheduleInfo;
    }

    public TimingInfoDTO getTimingInfo() {
        return timingInfo;
    }

    public void setTimingInfo(TimingInfoDTO timingInfo) {
        this.timingInfo = timingInfo;
    }

    public void setWeeklyScheduleInfo(WeeklyScheduleInfoDTO weeklyScheduleInfo) {
        this.weeklyScheduleInfo = weeklyScheduleInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduleInfoDTO scheduleInfoDTO = (ScheduleInfoDTO) o;

        if ( ! Objects.equals(id, scheduleInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleInfoDTO{" +
            "id=" + id +
            ", scheduleType='" + scheduleType + "'" +
            '}';
    }

    public Long getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Long serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
}
