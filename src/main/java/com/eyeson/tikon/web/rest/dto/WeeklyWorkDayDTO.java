package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.WeekDay;

/**
 * A DTO for the WeeklyWorkDay entity.
 */
public class WeeklyWorkDayDTO implements Serializable {

    private Long id;

    private WeekDay weekday;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public WeekDay getWeekday() {
        return weekday;
    }

    public void setWeekday(WeekDay weekday) {
        this.weekday = weekday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeeklyWorkDayDTO weeklyWorkDayDTO = (WeeklyWorkDayDTO) o;

        if ( ! Objects.equals(id, weeklyWorkDayDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.weekday);
    }

    @Override
    public String toString() {
        return "WeeklyWorkDayDTO{" +
            "id=" + id +
            ", weekday='" + weekday + "'" +
            '}';
    }
}
