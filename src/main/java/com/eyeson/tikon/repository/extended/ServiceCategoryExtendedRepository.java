package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceCategory;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 11/03/2016.
 */
@Named
public interface ServiceCategoryExtendedRepository extends BaseExtendedRepository<ServiceCategory> {
    List<ServiceCategory> getServiceCategoriesByCompanyId(Long companyId);
}
