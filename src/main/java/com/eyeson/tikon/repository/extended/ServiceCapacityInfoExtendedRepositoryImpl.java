package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceCapacityInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/30/2016.
 */
@Named
public class ServiceCapacityInfoExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<ServiceCapacityInfo>
    implements ServiceCapacityInfoExtendedRepository

    {

    @Override
    public ServiceCapacityInfo getServiceCapacityInfoByServiceItem(Long serviceItemId) {
//        return getEm().createQuery("select sci from ServiceCapacityInfo sci   join sci.priceInfo.serviceItems si  where  si.id=:serviceItemid ").setParameter("serviceItemid", serviceItemId).getResultList();
        return new ServiceCapacityInfo();
    }
}
