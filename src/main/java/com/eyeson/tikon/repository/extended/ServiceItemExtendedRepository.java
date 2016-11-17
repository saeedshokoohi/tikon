package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceItem;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 11/03/2016.
 */
@Named
public interface ServiceItemExtendedRepository extends BaseExtendedRepository<ServiceItem> {
    List<ServiceItem> getServiceItemsByCompanyId(Long companyId);
}
