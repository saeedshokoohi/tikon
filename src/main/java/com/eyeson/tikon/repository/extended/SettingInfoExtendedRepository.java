package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.SettingInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 11/07/2016.
 */
@Named
public interface SettingInfoExtendedRepository extends BaseExtendedRepository<SettingInfo> {
    List<SettingInfo> getSettingInfoByCompanyId(Long companyId);
}
