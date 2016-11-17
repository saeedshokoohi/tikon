package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.PriceInfoDtail;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 9/30/2016.
 */
@Named
public class PriceInfoDtailExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<PriceInfoDtail>
    implements PriceInfoDtailExtendedRepository

    {

    @Override
    public List<PriceInfoDtail> getPriceInfoDtailsByServiceItem(Long serviceItemId) {
        return getEm().createQuery("select pid from PriceInfoDtail pid   join pid.priceInfo.serviceItems si  where  si.id=:serviceItemid ").setParameter("serviceItemid", serviceItemId).getResultList();
    }

    @Override
    public List<PriceInfoDtail> getPriceInfoDtailsByPriceInfo(Long priceInfoId) {
        return getEm().createQuery("select pid from PriceInfoDtail pid   where  pid.priceInfo.id=:priceInfoId ").setParameter("priceInfoId", priceInfoId).getResultList();
    }
}
