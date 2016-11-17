package com.eyeson.tikon.web.rest.dto;

import com.eyeson.tikon.domain.ScheduleInfo;
import com.google.common.primitives.Shorts;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeed on 11/3/2016.
 */
public class TimingInfoDTO implements Serializable {

    List<TimeSpanDTO> availableTimes=new ArrayList<>();
    List<TimeSpanDTO> offTimes=new ArrayList<>();
    private LocalDate fromDate;
    private LocalDate toDate;
    private ZonedDateTime endTime;
    private ZonedDateTime startTime;
    private List<DayOfWeek> offDaysOfWeek;
    private List<LocalDate> offDates;
    private Integer timeDuration;
    private Integer gapDuration;
    private Long id;
    private Long schuleInfoId;
    private Long serviceItemId;

    public List<TimeSpanDTO> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<TimeSpanDTO> availableTimes) {
        this.availableTimes = availableTimes;

    }

    public List<TimeSpanDTO> getOffTimes() {
        return offTimes;
    }

    public void setOffTimes(List<TimeSpanDTO> offTimes) {
        this.offTimes = offTimes;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public List<DayOfWeek> getOffDaysOfWeek() {
        return offDaysOfWeek;
    }

    public void setOffDaysOfWeek(List<DayOfWeek> offDaysOfWeek) {
        this.offDaysOfWeek = offDaysOfWeek;
    }

    public List<LocalDate> getOffDates() {
        return offDates;
    }

    public void setOffDates(List<LocalDate> offDates) {
        this.offDates = offDates;
    }

    public long getTimeDuration() {
        if(timeDuration==null)timeDuration=0;
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        this.timeDuration = timeDuration;
    }

    public long getGapDuration() {
        if(gapDuration==null) gapDuration=0;
        return gapDuration;
    }

    public void setGapDuration(Integer gapDuration) {
        this.gapDuration = gapDuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchuleInfoId() {
        return schuleInfoId;
    }

    public void setSchuleInfoId(Long schuleInfoId) {
        this.schuleInfoId = schuleInfoId;
    }

    public Long getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Long serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
}
