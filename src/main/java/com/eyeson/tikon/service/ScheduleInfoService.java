package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.repository.ScheduleInfoRepository;
import com.eyeson.tikon.repository.ServiceItemRepository;
import com.eyeson.tikon.repository.ServiceTimeSessionInfoRepository;
import com.eyeson.tikon.repository.extended.ScheduleInfoExtendedRepository;
import com.eyeson.tikon.repository.search.ScheduleInfoSearchRepository;
import com.eyeson.tikon.service.util.LocalDateUtil;
import com.eyeson.tikon.web.rest.dto.ScheduleInfoDTO;
import com.eyeson.tikon.web.rest.dto.TimeSpanDTO;
import com.eyeson.tikon.web.rest.dto.TimingInfoDTO;
import com.eyeson.tikon.web.rest.dto.WeeklyScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleInfoMapper;
import com.eyeson.tikon.web.rest.mapper.WeeklyScheduleInfoMapper;
import com.eyeson.tikon.web.rest.util.ScheduleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ScheduleInfo.
 */
@Service
@Transactional
public class ScheduleInfoService {

    private final Logger log = LoggerFactory.getLogger(ScheduleInfoService.class);

    @Inject
    private ScheduleInfoRepository scheduleInfoRepository;



    @Inject
    private ScheduleInfoSearchRepository scheduleInfoSearchRepository;
    @Inject
    private ServiceItemRepository serviceItemRepository;

    /**
     * Save a scheduleInfo.
     *
     * @param scheduleInfoDTO the entity to save
     * @return the persisted entity
     */
    public ScheduleInfoDTO save(ScheduleInfoDTO scheduleInfoDTO) {
        log.debug("Request to save ScheduleInfo : {}", scheduleInfoDTO);
        ScheduleInfo scheduleInfo = scheduleInfoMapper.scheduleInfoDTOToScheduleInfo(scheduleInfoDTO);
        scheduleInfo = scheduleInfoRepository.save(scheduleInfo);
        ScheduleInfoDTO result = scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(scheduleInfo);
        scheduleInfoSearchRepository.save(scheduleInfo);
        return result;
    }

    /**
     *  Get all the scheduleInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ScheduleInfo> findAll(Pageable pageable) {
        log.debug("Request to get all ScheduleInfos");
        Page<ScheduleInfo> result = scheduleInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one scheduleInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ScheduleInfoDTO findOne(Long id) {
        log.debug("Request to get ScheduleInfo : {}", id);
        ScheduleInfo scheduleInfo = scheduleInfoRepository.findOne(id);
        ScheduleInfoDTO scheduleInfoDTO = scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(scheduleInfo);
      //  TimingInfoDTO timings = ScheduleUtil.makeTimingInfoFromScheduleInfo(scheduleInfoDTO);
        return scheduleInfoDTO;
    }

    public TimingInfoDTO getTimingInfo(ScheduleInfoDTO scheduleInfo)
    {
        TimingInfoDTO timings = ScheduleUtil.makeTimingInfoFromScheduleInfo(scheduleInfo);
        return timings;

    }
    /**
     *  Delete the  scheduleInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ScheduleInfo : {}", id);
        scheduleInfoRepository.delete(id);
        scheduleInfoSearchRepository.delete(id);
    }

    /**
     * Search for the scheduleInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ScheduleInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ScheduleInfos for query {}", query);
        return scheduleInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
 @Inject
    ScheduleInfoExtendedRepository scheduleInfoExtendedRepository;
    @Inject
    ServiceTimeSessionInfoRepository serviceTimeSessionInfoRepository;
    public List<ScheduleInfo> getPrimaryScheduleInfoByServiceItem(Long serviceItemId) {
        return scheduleInfoExtendedRepository.getPrimaryScheduleInfoByServiceItem(serviceItemId) ;
    }

    public TimingInfoDTO fillCurrentTimingInfo(ScheduleInfoDTO scheduleInfoDTO) {
       TimingInfoDTO retTimingInfoDTO=new TimingInfoDTO();
        List<ServiceTimeSessionInfo> currentTimeInfo = serviceTimeSessionInfoRepository.findByServiceItemIdAndScheduleInfoId(scheduleInfoDTO.getServiceItemId(), scheduleInfoDTO.getId());
        if(currentTimeInfo.size()>0)
        {
            retTimingInfoDTO=convertSessionTimingInfoToTimingInfoDto(currentTimeInfo.get(0));
        }
        else
        {
            retTimingInfoDTO=getTimingInfo(scheduleInfoDTO);
        }


        return retTimingInfoDTO;
    }

    @Inject
    ScheduleInfoMapper scheduleInfoMapper;

    private TimingInfoDTO convertSessionTimingInfoToTimingInfoDto(ServiceTimeSessionInfo serviceTimeSessionInfo) {
        ScheduleInfoDTO scheduleInfoDto = scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(serviceTimeSessionInfo.getScheduleInfo());
        TimingInfoDTO retTimingInfoDTO=getTimingInfo(scheduleInfoDto);
        retTimingInfoDTO.setAvailableTimes(makeTimeSpanDtoFromSessionList(serviceTimeSessionInfo.getServiceTimeSessions()));


        return retTimingInfoDTO;
    }

    private List<TimeSpanDTO> makeTimeSpanDtoFromSessionList(Set<ServiceTimeSession> serviceTimeSessions) {
        List<TimeSpanDTO> retList=new ArrayList<>();
        for(ServiceTimeSession sts:serviceTimeSessions)
        {
            TimeSpanDTO newTimeSpan=convertTimeSessionToTimeSpan(sts);
            retList.add(newTimeSpan);

        }

        return  retList;
    }

    private TimeSpanDTO convertTimeSessionToTimeSpan(ServiceTimeSession sts) {
        TimeSpanDTO retTimeSpanDTO=new TimeSpanDTO();
        retTimeSpanDTO.setStartTime(sts.getStartTime().toLocalDateTime());
        retTimeSpanDTO.setEndTime(sts.getEndTime().toLocalDateTime());
        retTimeSpanDTO.setId(sts.getId());
        retTimeSpanDTO.setComment(makeComment(sts));
        return retTimeSpanDTO;
    }

    private String makeComment(ServiceTimeSession sts) {
        return sts.getComment();
    }

    public ServiceTimeSessionInfo convertTimingInfoToSessionTimingInfo(TimingInfoDTO timingInfo) {
        ServiceTimeSessionInfo retSessionInfo = null;
        if (timingInfo != null) {
            boolean isNewSessionInfo = false;
            if (timingInfo.getId() == null) {
                isNewSessionInfo = true;
                retSessionInfo = new ServiceTimeSessionInfo();
                ScheduleInfo scheduleInfo = scheduleInfoRepository.findOne(timingInfo.getSchuleInfoId());
                ServiceItem serviceItem = serviceItemRepository.findOne(timingInfo.getServiceItemId());
                retSessionInfo.setScheduleInfo(scheduleInfo);
                retSessionInfo.setServiceItem(serviceItem);
                retSessionInfo.setServiceTimeSessions(makeServiceTimeSessionList(timingInfo));
            } else {

            }

        }
        return retSessionInfo;
    }

    private Set<ServiceTimeSession> makeServiceTimeSessionList(TimingInfoDTO timingInfo) {
        Set<ServiceTimeSession> retServiceTimeList = new HashSet<>();
        for (TimeSpanDTO ts : timingInfo.getAvailableTimes()) {
            ServiceTimeSession sts = new ServiceTimeSession();
            sts.setStartTime(LocalDateUtil.toZonedDateTime(ts.getStartTime()));
            sts.setEndTime(LocalDateUtil.toZonedDateTime(ts.getEndTime()));
            sts.setStartDate(ts.getSpanDate());
            sts.setEndDate(ts.getSpanDate());
            sts.setCapacity(findServiceCapacity(timingInfo.getServiceItemId(), sts));
            sts.setComment(findServiceComment(timingInfo.getServiceItemId(), sts));
            sts.setDiscount(findServiceDiscount(timingInfo.getServiceItemId(), sts));
            sts.setPrice(findServicePrice(timingInfo.getServiceItemId(), sts));
            retServiceTimeList.add(sts);
        }
        return retServiceTimeList;
    }

    private Double findServicePrice(Long serviceItemId, ServiceTimeSession sts) {
        return 0d;
    }

    private Double findServiceDiscount(Long serviceItemId, ServiceTimeSession sts) {
        return 0d;
    }

    private String findServiceComment(Long serviceItemId, ServiceTimeSession sts) {

        return " آماده";

    }

    private Integer findServiceCapacity(Long serviceItemId, ServiceTimeSession sts) {
        return 0;
    }

}
