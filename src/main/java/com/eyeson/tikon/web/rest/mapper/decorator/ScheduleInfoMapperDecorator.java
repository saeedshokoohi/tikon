package com.eyeson.tikon.web.rest.mapper.decorator;

import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.domain.ServiceItem;
import com.eyeson.tikon.service.ScheduleInfoService;
import com.eyeson.tikon.web.rest.dto.ScheduleInfoDTO;
import com.eyeson.tikon.web.rest.dto.ServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleInfoMapper;
import com.eyeson.tikon.web.rest.mapper.ServiceItemMapper;
import com.eyeson.tikon.web.rest.mapper.WeeklyScheduleInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by saeed on 8/23/2016.
 */

public abstract class ScheduleInfoMapperDecorator implements ScheduleInfoMapper {
    @Autowired
    @Qualifier("delegate")
    private  ScheduleInfoMapper delegate;



    @Autowired
    private WeeklyScheduleInfoMapper weeklyScheduleInfoMapper;
    @Autowired
    private ScheduleInfoService scheduleInfoService;
    @Override
    public ScheduleInfoDTO scheduleInfoToScheduleInfoDTO(ScheduleInfo scheduleInfo) {
        ScheduleInfoDTO scheduleInfoDTO=delegate.scheduleInfoToScheduleInfoDTO(scheduleInfo);
        if(scheduleInfo.getWeeklyScheduleInfo()!=null && scheduleInfo.getWeeklyScheduleInfo().getId()!=null) {

            scheduleInfoDTO.setWeeklyScheduleInfo(weeklyScheduleInfoMapper.weeklyScheduleInfoToWeeklyScheduleInfoDTO(scheduleInfo.getWeeklyScheduleInfo()));
        }
        //setting service Item id in ScheduleInfoDto
        if(scheduleInfo.getServiceItems()!=null && scheduleInfo.getServiceItems().size()>0)
        {
            scheduleInfoDTO.setServiceItemId(((ServiceItem)scheduleInfo.getServiceItems().toArray()[0]).getId());
        }


        return scheduleInfoDTO ;
    }
}
