package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.PriceInfoDtail;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/20/2016.
 */
@Named
public interface PriceInfoDtailExtendedRepository extends BaseExtendedRepository<PriceInfoDtail> {
    List<PriceInfoDtail> getPriceInfoDtailsByServiceItem(Long serviceItemId);
    List<PriceInfoDtail> getPriceInfoDtailsByPriceInfo(Long priceInfoId);
}
