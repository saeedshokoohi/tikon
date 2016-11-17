package com.eyeson.tikon.web.rest.util;

import com.eyeson.tikon.web.rest.dto.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by saeed on 11/3/2016.
 */
public class ScheduleUtil {

    public static TimingInfoDTO makeTimingInfoFromScheduleInfo(ScheduleInfoDTO scheduleInfo) {
        TimingInfoDTO retTimingInfo = fillTimingInfoDTO(scheduleInfo);
        if (retTimingInfo != null) {
            List<TimeSpanDTO> daysInDuration;
            List<TimeSpanDTO> activeDaysInDuration;
            //making list of days in given duration
            daysInDuration = getDatesInDuration(retTimingInfo);
            activeDaysInDuration = filterActiveDays(daysInDuration, retTimingInfo);
            List<TimeSpanDTO> activeDaysTimeStamps = makeTimeSpanList(activeDaysInDuration, retTimingInfo);
            retTimingInfo.setServiceItemId(scheduleInfo.getServiceItemId());
            retTimingInfo.setSchuleInfoId(scheduleInfo.getId());
            retTimingInfo.setAvailableTimes(activeDaysTimeStamps);
        }
        return retTimingInfo;

    }

    private static List<TimeSpanDTO> makeTimeSpanList(List<TimeSpanDTO> activeDaysInDuration, TimingInfoDTO retTimingInfo) {
        List<TimeSpanDTO> retTimeStamps = new ArrayList<>();
        for (TimeSpanDTO ts : activeDaysInDuration) {
            retTimeStamps.addAll(makeTimeSpanListInSingleDay(ts, retTimingInfo));
        }

        return retTimeStamps;
    }

    private static List<TimeSpanDTO> makeTimeSpanListInSingleDay(TimeSpanDTO day, TimingInfoDTO retTimingInfo) {
        List<TimeSpanDTO> retTimeStamps = new ArrayList<>();
        boolean isEndOfWorkDay = false;
        ZonedDateTime currentTime = retTimingInfo.getStartTime();
        while (!isEndOfWorkDay) {
            ZonedDateTime endOfSessionTime = currentTime.plusMinutes(retTimingInfo.getTimeDuration());
            IsOffTime isInOffTimeStatus = inOffTimes(currentTime, endOfSessionTime, retTimingInfo);
            if (endOfSessionTime.compareTo(retTimingInfo.getEndTime()) > 0) isEndOfWorkDay = true;
            else if(!isInOffTimeStatus.isInOffTime) {
                retTimeStamps.add(new TimeSpanDTO(day.getSpanDate(),currentTime,endOfSessionTime));
                currentTime=endOfSessionTime.plusMinutes(retTimingInfo.getGapDuration());
            } else if(isInOffTimeStatus.endofOffTime!=null ) {
              //  currentTime = endOfSessionTime.plusMinutes(retTimingInfo.getGapDuration());
             //   if (isInOffTimeStatus.endofOffTime.compareTo(endOfSessionTime) > 0)
                    currentTime = isInOffTimeStatus.endofOffTime;
            }
            else
            {
                currentTime=endOfSessionTime.plusMinutes(retTimingInfo.getGapDuration());
            }

        }
        return retTimeStamps;
    }

    private static IsOffTime inOffTimes(ZonedDateTime startTime, ZonedDateTime endOfSessionTime, TimingInfoDTO retTimingInfo) {
        IsOffTime retIsOfTime=new IsOffTime();
        ZonedDateTime endOfOffTime=null;
        boolean isInOffTime=false;
        for(TimeSpanDTO offt:retTimingInfo.getOffTimes())
        {
            if((offt.getStartTime().compareTo(startTime.toLocalDateTime())<0 && offt.getEndTime().compareTo(startTime.toLocalDateTime())>0)
                || (offt.getStartTime().compareTo(endOfSessionTime.toLocalDateTime())<0 && offt.getEndTime().compareTo(endOfSessionTime.toLocalDateTime())>=0) ) {
                isInOffTime = true;
                if((offt.getStartTime().compareTo(startTime.toLocalDateTime())<0 && offt.getEndTime().compareTo(startTime.toLocalDateTime())>0))
                    endOfOffTime=offt.getEndTime().atZone(startTime.getZone());
                break;
            }
        }
        retIsOfTime.setInOffTime(isInOffTime);
        retIsOfTime.setEndofOffTime(endOfOffTime);
        return retIsOfTime;
    }


    private static TimingInfoDTO fillTimingInfoDTO(ScheduleInfoDTO scheduleInfo) {
        TimingInfoDTO retTimingInfo = new TimingInfoDTO();
        if (scheduleInfo != null && scheduleInfo.getWeeklyScheduleInfo() != null && scheduleInfo.getWeeklyScheduleInfo().getDatePeriod() != null
            && scheduleInfo.getWeeklyScheduleInfo().getTimePeriod() != null) {
            retTimingInfo.setFromDate(scheduleInfo.getWeeklyScheduleInfo().getDatePeriod().getFromDate());
            retTimingInfo.setToDate(scheduleInfo.getWeeklyScheduleInfo().getDatePeriod().getToDate());
            retTimingInfo.setStartTime(scheduleInfo.getWeeklyScheduleInfo().getTimePeriod().getStartTime());
            retTimingInfo.setEndTime(scheduleInfo.getWeeklyScheduleInfo().getTimePeriod().getEndTime());
            retTimingInfo.setOffDates(mapToOffDate(scheduleInfo.getWeeklyScheduleInfo().getDatePeriod().getOffdays()));
            retTimingInfo.setOffTimes(mapToOffTimes( scheduleInfo.getWeeklyScheduleInfo().getTimePeriod().getOfftimes()));
            retTimingInfo.setOffDaysOfWeek(mapToOffDaysOfWeek(scheduleInfo.getWeeklyScheduleInfo().getWeekdays()));
               retTimingInfo.setTimeDuration(scheduleInfo.getWeeklyScheduleInfo().getTimePeriod().getDuration());
               retTimingInfo.setGapDuration(scheduleInfo.getWeeklyScheduleInfo().getTimePeriod().getGaptime());
        }
        return retTimingInfo;
    }

    private static List<TimeSpanDTO> mapToOffTimes(Set<OffTimeDTO> offtimes) {
        List<TimeSpanDTO> retOfftimes=new ArrayList<>();
        for (OffTimeDTO ot:offtimes)
        {
            if(ot.getFromTime()!=null && ot.getToTime()!=null) {
                TimeSpanDTO tst = new TimeSpanDTO(ot.getFromTime().toLocalDate(), ot.getFromTime(), ot.getToTime());
                retOfftimes.add(tst);
            }
        }
        return retOfftimes;
    }

    private static List<DayOfWeek> mapToOffDaysOfWeek(Set<WeeklyWorkDayDTO> weekdays) {
        List<DayOfWeek> retList = new ArrayList<>();
        for (DayOfWeek dow : DayOfWeek.values()) {
            boolean isOffDay = true;
            for (WeeklyWorkDayDTO wwd : weekdays) {
                if (wwd.getWeekday().name() == dow.name())
                    isOffDay = false;
            }
            if (isOffDay)
                retList.add(dow);
        }
        return retList;
    }

    private static List<LocalDate> mapToOffDate(Set<OffDayDTO> offdays) {
        List<LocalDate> retList = new ArrayList<>();
        for (OffDayDTO ofd : offdays) {
            retList.add(ofd.getOffDate());
        }
        return retList;
    }

    /***
     * filter list of days based on active days
     *
     * @param daysInDuration
     * @return
     */
    private static List<TimeSpanDTO> filterActiveDays(List<TimeSpanDTO> daysInDuration, TimingInfoDTO timingInfo) {
        List<TimeSpanDTO> activeDaysInDuration = new ArrayList<>();
        for (TimeSpanDTO d : daysInDuration) {
            boolean isOffDate = isOffDate(timingInfo, d);
            if (!isOffDate)
                activeDaysInDuration.add(d);
        }

        return activeDaysInDuration;
    }

    /**
     * check if given date if off date based on given timing info
     *
     * @param timingInfo
     * @param day
     * @return
     */
    private static boolean isOffDate(TimingInfoDTO timingInfo, TimeSpanDTO day) {
        if (!timingInfo.getOffDaysOfWeek().contains(day.getSpanDate().getDayOfWeek())) {
            if (!timingInfo.getOffDates().contains(day.getSpanDate())) {
                return false;
            }
        }
        return true;
    }

    /***
     * make the list of days in given duration
     *
     * @param timingInfo
     * @return
     */
    private static List<TimeSpanDTO> getDatesInDuration(TimingInfoDTO timingInfo) {
        List<TimeSpanDTO> daysInDuration = new ArrayList<>();
        boolean isEndOfDateDuration = false;
        LocalDate currentDate = timingInfo.getFromDate();
        while (!isEndOfDateDuration) {
            if (currentDate.compareTo(timingInfo.getToDate()) > 0) isEndOfDateDuration = true;
            else {
                daysInDuration.add(new TimeSpanDTO(currentDate));
                currentDate = currentDate.plusDays(1);
            }

        }
        return daysInDuration;
    }

}
