package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.PriceInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by saeed on 9/30/2016.
 */
@Named
public class PriceInfoExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<PriceInfo>
    implements PriceInfoExtendedRepository

    {

    @Override
    public List<PriceInfo> getPriceInfosByServiceItem(Long serviceItemId) {
        return getEm().createQuery("select pd from PriceInfo pd  join pd.serviceItems si  where  si.id=:serviceItemid ").setParameter("serviceItemid",serviceItemId).getResultList();
    }
}
