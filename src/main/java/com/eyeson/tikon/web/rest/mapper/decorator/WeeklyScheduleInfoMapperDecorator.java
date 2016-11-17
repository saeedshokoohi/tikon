
package com.eyeson.tikon.web.rest.mapper.decorator;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceItemDTO;
import com.eyeson.tikon.web.rest.dto.WeeklyScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by saeed on 8/23/2016.
 */

public abstract class WeeklyScheduleInfoMapperDecorator implements WeeklyScheduleInfoMapper {
    @Autowired
    @Qualifier("delegate")
    private  WeeklyScheduleInfoMapper delegate;


    @Autowired
    private DatePeriodMapper datePeriodMapper;
    @Autowired
    private TimePeriodMapper timePeriodMapper;

    @Override
    public WeeklyScheduleInfoDTO weeklyScheduleInfoToWeeklyScheduleInfoDTO(WeeklyScheduleInfo weeklyScheduleInfo) {
        WeeklyScheduleInfoDTO weeklyScheduleInfoDTO=delegate.weeklyScheduleInfoToWeeklyScheduleInfoDTO(weeklyScheduleInfo);
        if(weeklyScheduleInfo.getDatePeriods().size()>0)
            weeklyScheduleInfoDTO.setDatePeriod(datePeriodMapper.datePeriodToDatePeriodDTO((DatePeriod) weeklyScheduleInfo.getDatePeriods().toArray()[0]));
        if(weeklyScheduleInfo.getDailyDurations().size()>0)
            weeklyScheduleInfoDTO.setTimePeriod(timePeriodMapper.timePeriodToTimePeriodDTO((TimePeriod) weeklyScheduleInfo.getDailyDurations().toArray()[0]));
        return weeklyScheduleInfoDTO;
    }
}
