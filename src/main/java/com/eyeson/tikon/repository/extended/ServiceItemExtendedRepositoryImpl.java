package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceItem;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 11/03/2016.
 */
@Named
public class ServiceItemExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<ServiceItem>
    implements ServiceItemExtendedRepository

    {

    @Override
    public List<ServiceItem> getServiceItemsByCompanyId(Long companyId) {
        return getEm().createQuery("select si from ServiceItem si  where  si.category.company.id=:companyId ").setParameter("companyId",companyId).getResultList();
    }
}
