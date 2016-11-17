package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceCategory;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 11/03/2016.
 */
@Named
public class ServiceCategoryExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<ServiceCategory>
    implements ServiceCategoryExtendedRepository

    {

    @Override
    public List<ServiceCategory> getServiceCategoriesByCompanyId(Long companyId) {
        return getEm().createQuery("select sc from ServiceCategory sc  where  sc.company.id=:companyId ").setParameter("companyId",companyId).getResultList();
    }
}
