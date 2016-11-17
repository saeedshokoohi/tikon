package com.eyeson.tikon.domain.enumeration;

import com.eyeson.tikon.domain.WeeklyWorkDay;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * The WeekDay enumeration.
 */
public enum WeekDay {
    SATURDAY,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY;

   @JsonCreator
    public  static WeekDay fromValue(String val)
   {
       return  WeekDay.valueOf(val);
   }
}
