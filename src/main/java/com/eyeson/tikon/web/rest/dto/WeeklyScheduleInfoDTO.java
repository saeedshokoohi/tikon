package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the WeeklyScheduleInfo entity.
 */
public class WeeklyScheduleInfoDTO implements Serializable {

    private Long id;


    private Set<DatePeriodDTO> datePeriods = new HashSet<>();

    private Set<TimePeriodDTO> dailyDurations = new HashSet<>();

    private Set<WeeklyWorkDayDTO> weekdays = new HashSet<>();
    private DatePeriodDTO datePeriod=new DatePeriodDTO();
    private TimePeriodDTO timePeriod=new TimePeriodDTO();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<DatePeriodDTO> getDatePeriods() {
        return datePeriods;
    }

    public DatePeriodDTO getDatePeriod() {
        return datePeriod;
    }

    public void setDatePeriod(DatePeriodDTO datePeriod) {
        this.datePeriod = datePeriod;
    }

    public TimePeriodDTO getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriodDTO timePeriod) {
        this.timePeriod = timePeriod;
    }

    public void setDatePeriods(Set<DatePeriodDTO> datePeriods) {
        this.datePeriods = datePeriods;
    }

    public Set<TimePeriodDTO> getDailyDurations() {
        return dailyDurations;
    }

    public void setDailyDurations(Set<TimePeriodDTO> timePeriods) {
        this.dailyDurations = timePeriods;
    }

    public Set<WeeklyWorkDayDTO> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(Set<WeeklyWorkDayDTO> weeklyWorkDays) {
        this.weekdays = weeklyWorkDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeeklyScheduleInfoDTO weeklyScheduleInfoDTO = (WeeklyScheduleInfoDTO) o;

        if ( ! Objects.equals(id, weeklyScheduleInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WeeklyScheduleInfoDTO{" +
            "id=" + id +
            '}';
    }
}
