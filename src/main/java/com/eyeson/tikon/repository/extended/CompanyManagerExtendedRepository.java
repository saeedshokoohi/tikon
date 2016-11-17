package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.CompanyManager;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/16/2016.
 */
@Named
public interface CompanyManagerExtendedRepository extends BaseExtendedRepository<CompanyManager> {
    List<CompanyManager> getCompanyManagerByPersonInfoID(Long personInfoID);
}
