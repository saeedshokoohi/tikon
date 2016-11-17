package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ServiceOptionItem;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/28/2016.
 */
@Named
public interface ServiceOptionItemExtendedRepository extends BaseExtendedRepository<ServiceOptionItem> {
    List<ServiceOptionItem> getServiceOptionItemsByServiceItem(Long serviceItemId);
}
