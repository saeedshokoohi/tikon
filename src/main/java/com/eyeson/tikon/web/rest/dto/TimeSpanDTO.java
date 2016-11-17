package com.eyeson.tikon.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by saeed on 11/3/2016.
 */
public class TimeSpanDTO implements Serializable {
    private Long id;
    LocalDate spanDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;
    private String comment;

    public TimeSpanDTO() {
    }


    public TimeSpanDTO(LocalDate spanDate, ZonedDateTime startTime, ZonedDateTime endTime) {
        setSpanDate(spanDate);
        setStartTime(startTime.toLocalDateTime());
        setEndTime(endTime.toLocalDateTime());


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSpanDate() {
        return spanDate;
    }

    public void setSpanDate(LocalDate spanDate) {
        this.spanDate = spanDate;
    }

    public TimeSpanDTO(LocalDate date) {
        setSpanDate(date);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime=startTime;
        if(this.spanDate!=null && startTime!=null)
        this.startTime =  LocalDateTime.of(getSpanDate(),startTime.toLocalTime());
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime=endTime;
        if(this.spanDate!=null && endTime!=null)
            this.endTime =  LocalDateTime.of(getSpanDate(),endTime.toLocalTime());
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
