package com.eyeson.tikon.web.rest.util;

import java.time.ZonedDateTime;

/**
 * Created by saeed on 11/7/2016.
 */
public class IsOffTime
{
     boolean isInOffTime;
     ZonedDateTime endofOffTime;



    public boolean isInOffTime() {

        return isInOffTime;
    }

    public void setInOffTime(boolean inOffTime) {
        isInOffTime = inOffTime;
    }

    public ZonedDateTime getEndofOffTime() {
        return endofOffTime;
    }

    public void setEndofOffTime(ZonedDateTime endofOffTime) {
        this.endofOffTime = endofOffTime;
    }
}
