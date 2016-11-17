package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceCapacityInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/30/2016.
 */
@Named
public interface ServiceCapacityInfoExtendedRepository extends BaseExtendedRepository<ServiceCapacityInfo> {
    ServiceCapacityInfo getServiceCapacityInfoByServiceItem(Long serviceItemId);
}
