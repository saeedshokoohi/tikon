package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.PriceInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/20/2016.
 */
@Named
public interface PriceInfoExtendedRepository extends BaseExtendedRepository<PriceInfo> {
    List<PriceInfo> getPriceInfosByServiceItem(Long serviceItemId);
}
