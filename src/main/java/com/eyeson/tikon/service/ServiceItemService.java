package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.domain.enumeration.ScheduleType;
import com.eyeson.tikon.repository.*;
import com.eyeson.tikon.repository.extended.CompanyManagerExtendedRepository;
import com.eyeson.tikon.repository.extended.ServiceItemExtendedRepository;
import com.eyeson.tikon.repository.search.ServiceItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.*;
import com.eyeson.tikon.web.rest.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ServiceItem.
 */
@Service
@Transactional
public class ServiceItemService {

    private final Logger log = LoggerFactory.getLogger(ServiceItemService.class);

    @Inject
    private ServiceItemRepository serviceItemRepository;

    @Inject
    private ServiceItemMapper serviceItemMapper;

    @Inject
    private ServiceItemSearchRepository serviceItemSearchRepository;
    @Inject
    private DatePeriodRepository datePeriodRepository;
    @Inject
    private TimePeriodRepository timePeriodRepository;
    @Inject
    private WeeklyScheduleInfoRepository weeklyScheduleInfoRepository;
    @Inject
    private ScheduleInfoRepository scheduleInfoRepository;
    @Inject
    DatePeriodMapper datePeriodMapper;
    @Inject
    TimePeriodMapper timePeriodMapper;
    @Inject
    WeeklyScheduleInfoMapper weeklyScheduleInfoMapper;
    @Inject
    private ScheduleInfoMapper scheduleInfoMapper;
    @Inject
    private PriceInfoRepository priceInfoRepository;
    @Inject
    private PriceInfoMapper priceInfoMapper;
    @Inject
    private PriceInfoDtailRepository priceInfoDtailRepository;
    @Inject
    private PriceInfoDtailMapper priceInfoDtailMapper;
    @Inject
    WeeklyWorkDayRepository weeklyWorkDayRepository;
    @Inject
    private ServiceOptionInfoMapper serviceOptionInfoMapper;
    @Inject
    private ServiceOptionItemRepository serviceOptionItemRepository;
    @Inject
    private ServiceOptionInfoRepository serviceOptionInfoRepository;
    @Inject
    private ServiceOptionItemMapper serviceOptionItemMapper;
    @Inject
    private ServiceCapacityInfoRepository serviceCapacityInfoRepository;
    @Inject
    private ServiceTimeSessionInfoRepository serviceTimeSessionInfoRepository;
    @Inject
    private ServiceCapacityInfoMapper serviceCapacityInfoMapper;
    @Inject
    OffDayRepository offDayRepository;
    @Inject
    OffTimeRepository offTimeRepository;
    @Inject
    private PersonInfoService personInfoService;
    @Inject
    private CompanyManagerExtendedRepository companyManagerExtendedRepository;
    @Inject
    ScheduleInfoService scheduleInfoService;


    /**
     * Save a serviceItem.
     *
     * @param serviceItemDTO the entity to save
     * @return the persisted entity
     */
    public ServiceItemDTO save(ServiceItemDTO serviceItemDTO) {
        log.debug("Request to save ServiceItem : {}", serviceItemDTO);
        ServiceItem serviceItem = serviceItemMapper.serviceItemDTOToServiceItem(serviceItemDTO);
        serviceItem = serviceItemRepository.save(serviceItem);
        ServiceItemDTO result = serviceItemMapper.serviceItemToServiceItemDTO(serviceItem);
        serviceItemSearchRepository.save(serviceItem);
        return result;
    }

    /**
     * Save a serviceItem.
     *
     * @param serviceItemDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public ServiceItemDTO saveWithRelatedEntities(ServiceItemDTO serviceItemDTO) {
        log.debug("Request to save ServiceItem : {}", serviceItemDTO);

//        boolean isNewPriceInfo = serviceItemDTO.getServicePriceInfoDtail().getPriceInfo().getId()==null;

        ScheduleInfo schInfo = SaveScheduleInfo(serviceItemDTO);
        PriceInfo priceInfo = SavePriceInfo(serviceItemDTO);
        ServiceOptionInfo optionInfo = SaveOptionInfo(serviceItemDTO);
        ServiceCapacityInfo serviceCapacityInfo = SaveCapacityInfo(serviceItemDTO);
        ServiceItem serviceItem = serviceItemMapper.serviceItemDTOToServiceItem(serviceItemDTO);
        serviceItem.getServiceTimes().add(schInfo);


        if (serviceItem.getId() != null) {
            ServiceItem tempService = serviceItemRepository.getOne(serviceItem.getId());
            serviceItem.setServicePriceInfo(tempService.getServicePriceInfo());
            serviceItem.setOptions(tempService.getOptions());

            if (priceInfo != null) {
                serviceItem.getServicePriceInfo().add(priceInfo);
            }
            if (optionInfo != null) {
                serviceItem.getOptions().add(optionInfo);
            }
            if (serviceCapacityInfo != null) {
                serviceItem.setCapacityInfo(serviceCapacityInfo);
            }

        }
        serviceItem = serviceItemRepository.save(serviceItem);
        ServiceItemDTO result = serviceItemMapper.serviceItemToServiceItemDTO(serviceItem);
        //setting current schedule info as default
        result.setScheduleInfo(scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(schInfo));
        serviceItemSearchRepository.save(serviceItem);
        return result;
    }

    private void SaveTimingInfo(ServiceItemDTO serviceItemDTO) {

        if (serviceItemDTO != null && serviceItemDTO.getScheduleInfo() != null && serviceItemDTO.getScheduleInfo().getTimingInfo() != null) {
            ServiceTimeSessionInfo sessionInfo =scheduleInfoService.convertTimingInfoToSessionTimingInfo(serviceItemDTO.getScheduleInfo().getTimingInfo());
            sessionInfo.getServiceTimeSessions().forEach(ts -> ts.setServiceTimeSessionInfo(sessionInfo));
            List<ServiceTimeSessionInfo> oldSessionInfo = serviceTimeSessionInfoRepository.findByServiceItemIdAndScheduleInfoId(sessionInfo.getServiceItem().getId(), sessionInfo.getScheduleInfo().getId());
            oldSessionInfo.forEach(e -> serviceTimeSessionInfoRepository.delete(e));
            serviceTimeSessionInfoRepository.save(sessionInfo);
        }


    }


    private ScheduleInfo SaveScheduleInfo(ServiceItemDTO serviceItemDTO) {

        if (serviceItemDTO == null || serviceItemDTO.getScheduleInfo() == null) return null;
        boolean isNewSchedule = serviceItemDTO.getScheduleInfo().getId() == null;
        boolean isNewWeeklySchedule = isNewSchedule || serviceItemDTO.getScheduleInfo().getWeeklyScheduleInfo().getId() == null;
        boolean isNewDatePeriod = isNewWeeklySchedule || serviceItemDTO.getScheduleInfo().getWeeklyScheduleInfo().getDatePeriod().getId() == null;
        boolean isNewTimePeriod = isNewWeeklySchedule || serviceItemDTO.getScheduleInfo().getWeeklyScheduleInfo().getTimePeriod().getId() == null;

        SaveTimingInfo(serviceItemDTO);
        //adding datePeriod
        //     DatePeriod datePeriod=new DatePeriod();
        DatePeriod datePeriod = datePeriodMapper.datePeriodDTOToDatePeriod(serviceItemDTO.getScheduleInfo().getWeeklyScheduleInfo().getDatePeriod());
        AddOrGetOffDay(datePeriod.getOffdays());

        datePeriodRepository.save(datePeriod);

        TimePeriod timePeriod = timePeriodMapper.timePeriodDTOToTimePeriod(serviceItemDTO.getScheduleInfo().getWeeklyScheduleInfo().getTimePeriod());
        AddOrGetOffTime(timePeriod.getOfftimes());
        timePeriodRepository.save(timePeriod);


        //adding WeeklyScheduleInfo
        if (isNewWeeklySchedule) {
            WeeklyScheduleInfo weeklyScheduleInfo = weeklyScheduleInfoMapper.weeklyScheduleInfoDTOToWeeklyScheduleInfo(serviceItemDTO.getScheduleInfo()
                .getWeeklyScheduleInfo());
            weeklyScheduleInfo.getDatePeriods().add(datePeriod);
            weeklyScheduleInfo.getDailyDurations().add(timePeriod);
            AddOrGetWorkays(weeklyScheduleInfo.getWeekdays());

            weeklyScheduleInfoRepository.save(weeklyScheduleInfo);
            //adding scheduleInfo
            ScheduleInfo schInfo = new ScheduleInfo();
            schInfo.setScheduleType(ScheduleType.PRIMARY);
            schInfo.setWeeklyScheduleInfo(weeklyScheduleInfo);
            scheduleInfoRepository.save(schInfo);


            return schInfo;
        } else {
            WeeklyScheduleInfo weeklyScheduleInfo = weeklyScheduleInfoMapper.weeklyScheduleInfoDTOToWeeklyScheduleInfo(serviceItemDTO.getScheduleInfo()
                .getWeeklyScheduleInfo());
            if (isNewDatePeriod)
                weeklyScheduleInfo.getDatePeriods().add(datePeriod);
            if (isNewTimePeriod)
                weeklyScheduleInfo.getDailyDurations().add(timePeriod);
            AddOrGetWorkays(weeklyScheduleInfo.getWeekdays());

            weeklyScheduleInfoRepository.save(weeklyScheduleInfo);
            ScheduleInfo schInfo = scheduleInfoMapper.scheduleInfoDTOToScheduleInfo(serviceItemDTO.getScheduleInfo());
            schInfo.setScheduleType(ScheduleType.PRIMARY);
            schInfo.setWeeklyScheduleInfo(weeklyScheduleInfo);
            scheduleInfoRepository.save(schInfo);
            //bcx some of the details has been changed
            //i get the last version of schedule info
            schInfo = scheduleInfoRepository.getOne(schInfo.getId());
            return schInfo;
        }


    }

    private void AddOrGetWorkays(Set<WeeklyWorkDay> weekdays) {
        for (WeeklyWorkDay wd : weekdays) {
            List<WeeklyWorkDay> wds = weeklyWorkDayRepository.findByWeekday(wd.getWeekday());
            if (wds != null && wds.size() > 0) {
                wd = wds.get(0);

            }
            weeklyWorkDayRepository.save(wd);
        }

    }

    private void AddOrGetOffDay(Set<OffDay> offDates) {
        for (OffDay wd : offDates) {
            List<OffDay> wds = offDayRepository.findByOffDate(wd.getOffDate());
            if (wds != null && wds.size() > 0) {
                wd = wds.get(0);

            }
            offDayRepository.save(wd);
        }

    }

    private void AddOrGetOffTime(Set<OffTime> offDates) {
        for (OffTime wd : offDates) {
            List<OffTime> wds = offTimeRepository.findByFromTimeAndToTime(wd.getFromTime(), wd.getToTime());
            if (wds != null && wds.size() > 0) {
                wd = wds.get(0);

            }
            offTimeRepository.save(wd);
        }

    }

    // in this moment save one detail with header
    private PriceInfo SavePriceInfo(ServiceItemDTO serviceItemDTO) {

        if (serviceItemDTO == null || serviceItemDTO.getServicePriceInfoDtail() == null) return null;
        PriceInfoDtailDTO priceInfoDtailDTO = serviceItemDTO.getServicePriceInfoDtail();

        if (priceInfoDtailDTO.getPriceInfo() == null) return null;

        boolean isNewPriceInfo = priceInfoDtailDTO.getPriceInfo().getId() == null;
        boolean isNewPriceInfoDtail = priceInfoDtailDTO.getId() == null;


        if (isNewPriceInfo) {
            PriceInfo priceInfo = new PriceInfo();
            priceInfo = priceInfoMapper.priceInfoDTOToPriceInfo(priceInfoDtailDTO.getPriceInfo());
            priceInfo.getServiceItems().add(serviceItemMapper.serviceItemDTOToServiceItem(serviceItemDTO));
            priceInfoRepository.save(priceInfo);

            PriceInfoDtail priceInfoDtail = new PriceInfoDtail();
            priceInfoDtail = priceInfoDtailMapper.priceInfoDtailDTOToPriceInfoDtail(priceInfoDtailDTO);
            priceInfoDtail.setPriceInfo(priceInfo);

            priceInfoDtailRepository.save(priceInfoDtail);

            return priceInfo;
        } else {
            PriceInfo priceInfo = priceInfoMapper.priceInfoDTOToPriceInfo(priceInfoDtailDTO.getPriceInfo());
//            priceInfo.getServiceItems().add(serviceItemMapper.serviceItemDTOToServiceItem(serviceItemDTO));
            priceInfoRepository.save(priceInfo);

            PriceInfoDtail priceInfoDtail = priceInfoDtailMapper.priceInfoDtailDTOToPriceInfoDtail(priceInfoDtailDTO);
            priceInfoDtail.setPriceInfo(priceInfo);

            priceInfoDtailRepository.save(priceInfoDtail);
            return priceInfo;
        }


    }

    private ServiceOptionInfo SaveOptionInfo(ServiceItemDTO serviceItemDTO) {

        if (serviceItemDTO == null || serviceItemDTO.getServiceOptionItem() == null) return null;

        ServiceOptionItemDTO serviceOptionItemDTO = serviceItemDTO.getServiceOptionItem();
        if (serviceOptionItemDTO.getOptionInfo() == null) return null;

        boolean isNewOptionInfo = false;

        if (serviceOptionItemDTO.getOptionInfo().getId() == null)
            isNewOptionInfo = true;
        if (isNewOptionInfo) {
            ServiceOptionInfo optionInfo = new ServiceOptionInfo();
            optionInfo = serviceOptionInfoMapper.serviceOptionInfoDTOToServiceOptionInfo(serviceOptionItemDTO.getOptionInfo());
            optionInfo.getServiceItems().add(serviceItemMapper.serviceItemDTOToServiceItem(serviceItemDTO));
            serviceOptionInfoRepository.save(optionInfo);


            PriceInfo priceInfo = new PriceInfo();
            priceInfo = priceInfoMapper.priceInfoDTOToPriceInfo(serviceOptionItemDTO.getPriceInfoDtail().getPriceInfo());
            priceInfoRepository.save(priceInfo);

            PriceInfoDtail priceInfoDtail = new PriceInfoDtail();
            priceInfoDtail = priceInfoDtailMapper.priceInfoDtailDTOToPriceInfoDtail(serviceOptionItemDTO.getPriceInfoDtail());
            priceInfoDtail.setPriceInfo(priceInfo);
            priceInfoDtailRepository.save(priceInfoDtail);

            ServiceOptionItem serviceOptionItem = new ServiceOptionItem();
            serviceOptionItem = serviceOptionItemMapper.serviceOptionItemDTOToServiceOptionItem(serviceOptionItemDTO);
            serviceOptionItem.setOptionInfo(optionInfo);
            serviceOptionItem.setPriceInfo(priceInfo);

            serviceOptionItemRepository.save(serviceOptionItem);


            return optionInfo;
        } else {
            ServiceOptionInfo serviceOptionInfo = serviceOptionInfoMapper.serviceOptionInfoDTOToServiceOptionInfo(serviceOptionItemDTO.getOptionInfo());
            serviceOptionInfoRepository.save(serviceOptionInfo);

            PriceInfo priceInfo = priceInfoMapper.priceInfoDTOToPriceInfo(serviceOptionItemDTO.getPriceInfoDtail().getPriceInfo());
            priceInfoRepository.save(priceInfo);

            PriceInfoDtail priceInfoDtail = priceInfoDtailMapper.priceInfoDtailDTOToPriceInfoDtail(serviceOptionItemDTO.getPriceInfoDtail());
            priceInfoDtail.setPriceInfo(priceInfo);
            priceInfoDtailRepository.save(priceInfoDtail);

            ServiceOptionItem serviceOptionItem = serviceOptionItemMapper.serviceOptionItemDTOToServiceOptionItem(serviceOptionItemDTO);
            serviceOptionItem.setOptionInfo(serviceOptionInfo);
            serviceOptionItem.setPriceInfo(priceInfo);
            serviceOptionItemRepository.save(serviceOptionItem);

            return serviceOptionInfo;
        }


    }

    private ServiceCapacityInfo SaveCapacityInfo(ServiceItemDTO serviceItemDTO) {

        if (serviceItemDTO == null || serviceItemDTO.getCapacityInfo() == null) return null;
        if (serviceItemDTO.getCapacityInfo().getQty() == null) {
            serviceItemDTO.getCapacityInfo().setQty(0);
        }
        boolean isNewCapacityInfo = serviceItemDTO.getCapacityInfo().getId() == null;

        if (isNewCapacityInfo) {
            ServiceCapacityInfo capacityInfo = new ServiceCapacityInfo();
            capacityInfo = serviceCapacityInfoMapper.serviceCapacityInfoDTOToServiceCapacityInfo(serviceItemDTO.getCapacityInfo());
            serviceCapacityInfoRepository.save(capacityInfo);

            return capacityInfo;
        } else {
            ServiceCapacityInfo capacityInfo = serviceCapacityInfoMapper.serviceCapacityInfoDTOToServiceCapacityInfo(serviceItemDTO.getCapacityInfo());
            serviceCapacityInfoRepository.save(capacityInfo);
            return capacityInfo;
        }


    }


    /**
     * Get all the serviceItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceItem> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceItems");
        Page<ServiceItem> result = serviceItemRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one serviceItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ServiceItemDTO findOne(Long id) {
        log.debug("Request to get ServiceItem : {}", id);
        ServiceItem serviceItem = serviceItemRepository.findOneWithEagerRelationships(id);
        ServiceItemDTO serviceItemDTO = serviceItemMapper.serviceItemToServiceItemDTO(serviceItem);

        return serviceItemDTO;
    }

    /**
     * Delete the  serviceItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceItem : {}", id);
        serviceItemRepository.delete(id);
        serviceItemSearchRepository.delete(id);
    }

    /**
     * Search for the serviceItem corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceItem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceItems for query {}", query);
        return serviceItemSearchRepository.search(queryStringQuery(query), pageable);
    }


    @Inject
    ServiceItemExtendedRepository serviceItemExtendedRepository;

    public List<ServiceItem> getServiceItemsByCurrentCompany() {
        List<ServiceItem> retList=new ArrayList<>();
        PersonInfo personInfo = personInfoService.getCurrentPersonInfo();
        if(personInfo!=null) {
            List<CompanyManager> companyManager = companyManagerExtendedRepository.getCompanyManagerByPersonInfoID(personInfo.getId());
            if (companyManager.size() > 0)
                retList= serviceItemExtendedRepository.getServiceItemsByCompanyId(companyManager.get(0).getCompany().getId());
        }
       return  retList;
    }
}
