package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.ScheduleInfo;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import javax.inject.Named;
import java.util.List;

/**
 * Created by saeed on 9/30/2016.
 */
@Named
public interface ScheduleInfoExtendedRepository extends BaseExtendedRepository<ScheduleInfo> {
    List<ScheduleInfo> getPrimaryScheduleInfoByServiceItem(Long serviceItemId);
}
