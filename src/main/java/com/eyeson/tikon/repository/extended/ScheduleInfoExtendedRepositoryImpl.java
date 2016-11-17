package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.repository.ScheduleInfoRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by saeed on 9/30/2016.
 */
@Named
public class ScheduleInfoExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<ScheduleInfo>
    implements ScheduleInfoExtendedRepository

    {

    @Override
    public List<ScheduleInfo> getPrimaryScheduleInfoByServiceItem(Long serviceItemId) {
       return getEm().createQuery("select sh from ScheduleInfo sh join sh.serviceItems si where sh.scheduleType='PRIMARY' and si.id=:serviceItemid ").setParameter("serviceItemid",serviceItemId).getResultList();

    }
}
