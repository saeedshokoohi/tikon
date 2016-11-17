package com.eyeson.tikon.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by saeed on 8/22/2016.
 */
public class LocalDateUtil {
    public static LocalDate setLocalDateFromString( String dateStr) {
        Logger log = LoggerFactory.getLogger(LocalDateUtil.class);
        try{
            return  LocalDate.parse(dateStr.split("T")[0]);
        }catch (Exception ex)
        {
            log.error("error on converting "+dateStr+" to LocalDate");
            return null;
        }
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime) {
        return dateTime.atZone(ZonedDateTime.now().getZone());
    }
}
