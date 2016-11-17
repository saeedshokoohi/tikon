package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.SettingInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 11/07/2016.
 */
@Named
public class SettingInfoExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<SettingInfo>
    implements SettingInfoExtendedRepository

    {

    @Override
    public List<SettingInfo> getSettingInfoByCompanyId(Long companyId) {
        return getEm().createQuery("select c.setting from Company c  where  c.id=:companyId ").setParameter("companyId",companyId).getResultList();
    }
}
