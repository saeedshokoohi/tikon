package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.PriceInfo;
import com.eyeson.tikon.domain.ServiceCapacityInfo;
import com.eyeson.tikon.domain.ServiceOptionItem;
import com.eyeson.tikon.domain.enumeration.ServiceItemType;

/**
 * A DTO for the ServiceItem entity.
 */
public class ServiceItemDTO implements Serializable {

    private Long id;

    private String itemTitle;

    private String description;

    private Double minPreReserveTime;

    private Double maxPreReserveTime;

    private Boolean hasWaitingList;

    private Boolean mustGetParticipantInfo;

    private Boolean canBeCanceled;

    private Double minPreCancelTime;

    private Integer paymentType;

    private ServiceItemType serviceType;


    private Set<ServiceOptionInfoDTO> options = new HashSet<>();

    private Long discountInfoId;

    private Long locationId;

    private Long imageAlbumId;

    private Long capacityInfoId;

    private Long categoryId;

    private Long priceinfoId;

    private Set<ScheduleInfoDTO> serviceTimes = new HashSet<>();
    private ScheduleInfoDTO scheduleInfo=new ScheduleInfoDTO();

    private Set<ServantDTO> servants = new HashSet<>();

    private Long agreementId;
    private String startDate;
    private String endDate;

    private Set<PriceInfoDTO> servicePriceInfo = new HashSet<>();

    private PriceInfoDtailDTO servicePriceInfoDtail = new PriceInfoDtailDTO();

    private ServiceOptionItemDTO serviceOptionItem = new ServiceOptionItemDTO();

    private ServiceCapacityInfoDTO capacityInfo = new ServiceCapacityInfoDTO();

    private AgreementInfoDTO agreement = new AgreementInfoDTO();


    public String getStartDate() {
        return startDate;
    }

    public ScheduleInfoDTO getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfoDTO scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private Set<MetaTagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Double getMinPreReserveTime() {
        return minPreReserveTime;
    }

    public void setMinPreReserveTime(Double minPreReserveTime) {
        this.minPreReserveTime = minPreReserveTime;
    }
    public Double getMaxPreReserveTime() {
        return maxPreReserveTime;
    }

    public void setMaxPreReserveTime(Double maxPreReserveTime) {
        this.maxPreReserveTime = maxPreReserveTime;
    }
    public Boolean getHasWaitingList() {
        return hasWaitingList;
    }

    public void setHasWaitingList(Boolean hasWaitingList) {
        this.hasWaitingList = hasWaitingList;
    }
    public Boolean getMustGetParticipantInfo() {
        return mustGetParticipantInfo;
    }

    public void setMustGetParticipantInfo(Boolean mustGetParticipantInfo) {
        this.mustGetParticipantInfo = mustGetParticipantInfo;
    }
    public Boolean getCanBeCanceled() {
        return canBeCanceled;
    }

    public void setCanBeCanceled(Boolean canBeCanceled) {
        this.canBeCanceled = canBeCanceled;
    }
    public Double getMinPreCancelTime() {
        return minPreCancelTime;
    }

    public void setMinPreCancelTime(Double minPreCancelTime) {
        this.minPreCancelTime = minPreCancelTime;
    }
    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    public ServiceItemType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceItemType serviceType) {
        this.serviceType = serviceType;
    }

    public Set<ServiceOptionInfoDTO> getOptions() {
        return options;
    }

    public void setOptions(Set<ServiceOptionInfoDTO> serviceOptionInfos) {
        this.options = serviceOptionInfos;
    }

    public Long getDiscountInfoId() {
        return discountInfoId;
    }

    public void setDiscountInfoId(Long discountInfoId) {
        this.discountInfoId = discountInfoId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationInfoId) {
        this.locationId = locationInfoId;
    }

    public Long getImageAlbumId() {
        return imageAlbumId;
    }

    public void setImageAlbumId(Long albumInfoId) {
        this.imageAlbumId = albumInfoId;
    }

    public Long getCapacityInfoId() {
        return capacityInfoId;
    }

    public void setCapacityInfoId(Long serviceCapacityInfoId) {
        this.capacityInfoId = serviceCapacityInfoId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long serviceCategoryId) {
        this.categoryId = serviceCategoryId;
    }

    public Long getPriceinfoId() {
        return priceinfoId;
    }

    public void setPriceinfoId(Long priceInfoId) {
        this.priceinfoId = priceInfoId;
    }

    public Set<ScheduleInfoDTO> getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Set<ScheduleInfoDTO> scheduleInfos) {
        this.serviceTimes = scheduleInfos;
    }

    public Set<ServantDTO> getServants() {
        return servants;
    }

    public void setServants(Set<ServantDTO> servants) {
        this.servants = servants;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementInfoId) {
        this.agreementId = agreementInfoId;
    }

    public Set<MetaTagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<MetaTagDTO> metaTags) {
        this.tags = metaTags;
    }

    public Set<PriceInfoDTO> getServicePriceInfo() {
        return servicePriceInfo;
    }

    public void setServicePriceInfo(Set<PriceInfoDTO> priceInfos) {
        this.servicePriceInfo = priceInfos;
    }

    public PriceInfoDtailDTO getServicePriceInfoDtail() {
        return servicePriceInfoDtail;
    }

    public void setServicePriceInfoDtail(PriceInfoDtailDTO priceInfoDtails) {
        this.servicePriceInfoDtail = priceInfoDtails;
    }

    public ServiceOptionItemDTO getServiceOptionItem() {
        return serviceOptionItem;
    }

    public void setServiceOptionItem(ServiceOptionItemDTO serviceOptionItems) {
        this.serviceOptionItem = serviceOptionItems;
    }

    public ServiceCapacityInfoDTO getCapacityInfo() {
        return capacityInfo;
    }

    public void setCapacityInfo(ServiceCapacityInfoDTO capacityInfo) {
        this.capacityInfo = capacityInfo;
    }

    public AgreementInfoDTO getAgreement() {
        return agreement;
    }

    public void setAgreement(AgreementInfoDTO agreement) {
        this.agreement = agreement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceItemDTO serviceItemDTO = (ServiceItemDTO) o;

        if ( ! Objects.equals(id, serviceItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceItemDTO{" +
            "id=" + id +
            ", itemTitle='" + itemTitle + "'" +
            ", description='" + description + "'" +
            ", minPreReserveTime='" + minPreReserveTime + "'" +
            ", maxPreReserveTime='" + maxPreReserveTime + "'" +
            ", hasWaitingList='" + hasWaitingList + "'" +
            ", mustGetParticipantInfo='" + mustGetParticipantInfo + "'" +
            ", canBeCanceled='" + canBeCanceled + "'" +
            ", minPreCancelTime='" + minPreCancelTime + "'" +
            ", paymentType='" + paymentType + "'" +
            ", serviceType='" + serviceType + "'" +
            '}';
    }
}
