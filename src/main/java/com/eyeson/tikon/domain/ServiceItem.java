package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.ServiceItemType;

/**
 * A ServiceItem.
 */
@Entity
@Table(name = "service_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serviceitem")
public class ServiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "item_title")
    private String itemTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "min_pre_reserve_time")
    private Double minPreReserveTime;

    @Column(name = "max_pre_reserve_time")
    private Double maxPreReserveTime;

    @Column(name = "has_waiting_list")
    private Boolean hasWaitingList;

    @Column(name = "must_get_participant_info")
    private Boolean mustGetParticipantInfo;

    @Column(name = "can_be_canceled")
    private Boolean canBeCanceled;

    @Column(name = "min_pre_cancel_time")
    private Double minPreCancelTime;

    @Column(name = "payment_type")
    private Integer paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceItemType serviceType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_options",
               joinColumns = @JoinColumn(name="service_items_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="options_id", referencedColumnName="ID"))
    private Set<ServiceOptionInfo> options = new HashSet<>();

    @ManyToOne
    private DiscountInfo discountInfo;

    @ManyToOne
    private LocationInfo location;

    @ManyToOne
    private AlbumInfo imageAlbum;

    @ManyToOne
    private ServiceCapacityInfo capacityInfo;

    @ManyToOne
    private ServiceCategory category;

//    @ManyToOne
//    private PriceInfo priceinfo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_service_times",
               joinColumns = @JoinColumn(name="service_items_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="service_times_id", referencedColumnName="ID"))
    private Set<ScheduleInfo> serviceTimes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_servants",
               joinColumns = @JoinColumn(name="service_items_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="servants_id", referencedColumnName="ID"))
    private Set<Servant> servants = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.ALL})
    private AgreementInfo agreement;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_tags",
               joinColumns = @JoinColumn(name="service_items_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<MetaTag> tags = new HashSet<>();


    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_price_info",
        joinColumns = @JoinColumn(name="service_items_id", referencedColumnName="ID"),
        inverseJoinColumns = @JoinColumn(name="price_info_id", referencedColumnName="ID"))
    private Set<PriceInfo> servicePriceInfo = new HashSet<>();



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

    public Boolean isHasWaitingList() {
        return hasWaitingList;
    }

    public void setHasWaitingList(Boolean hasWaitingList) {
        this.hasWaitingList = hasWaitingList;
    }

    public Boolean isMustGetParticipantInfo() {
        return mustGetParticipantInfo;
    }

    public void setMustGetParticipantInfo(Boolean mustGetParticipantInfo) {
        this.mustGetParticipantInfo = mustGetParticipantInfo;
    }

    public Boolean isCanBeCanceled() {
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

    public Set<ServiceOptionInfo> getOptions() {
        return options;
    }

    public void setOptions(Set<ServiceOptionInfo> serviceOptionInfos) {
        this.options = serviceOptionInfos;
    }

    public DiscountInfo getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(DiscountInfo discountInfo) {
        this.discountInfo = discountInfo;
    }

    public LocationInfo getLocation() {
        return location;
    }

    public void setLocation(LocationInfo locationInfo) {
        this.location = locationInfo;
    }

    public AlbumInfo getImageAlbum() {
        return imageAlbum;
    }

    public void setImageAlbum(AlbumInfo albumInfo) {
        this.imageAlbum = albumInfo;
    }

    public ServiceCapacityInfo getCapacityInfo() {
        return capacityInfo;
    }

    public void setCapacityInfo(ServiceCapacityInfo serviceCapacityInfo) {
        this.capacityInfo = serviceCapacityInfo;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

//    public PriceInfo getPriceinfo() {
//        return priceinfo;
//    }
//
//    public void setPriceinfo(PriceInfo priceInfo) {
//        this.priceinfo = priceInfo;
//    }

    public Set<ScheduleInfo> getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Set<ScheduleInfo> scheduleInfos) {
        this.serviceTimes = scheduleInfos;
    }

    public Set<Servant> getServants() {
        return servants;
    }

    public void setServants(Set<Servant> servants) {
        this.servants = servants;
    }

    public AgreementInfo getAgreement() {
        return agreement;
    }

    public void setAgreement(AgreementInfo agreementInfo) {
        this.agreement = agreementInfo;
    }

    public Set<MetaTag> getTags() {
        return tags;
    }

    public void setTags(Set<MetaTag> metaTags) {
        this.tags = metaTags;
    }

    public Set<PriceInfo> getServicePriceInfo() {
        return servicePriceInfo;
    }

    public void setServicePriceInfo(Set<PriceInfo> priceInfos) {
        this.servicePriceInfo = priceInfos;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceItem serviceItem = (ServiceItem) o;
        if(serviceItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceItem{" +
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
