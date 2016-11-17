package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceOptionItem;
import com.eyeson.tikon.domain.PriceInfoDtail;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/28/2016.
 */
@Named
public class ServiceOptionItemExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<ServiceOptionItem>
    implements ServiceOptionItemExtendedRepository

    {

    @Override
    public List<ServiceOptionItem> getServiceOptionItemsByServiceItem(Long serviceItemId) {
        return getEm().createQuery("select soi from ServiceOptionItem soi   join soi.optionInfo.serviceItems si  where   si.id=:serviceItemid ").setParameter("serviceItemid", serviceItemId).getResultList();
    }
}
