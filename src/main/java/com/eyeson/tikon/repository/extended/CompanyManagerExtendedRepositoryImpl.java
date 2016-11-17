package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.CompanyManager;

import javax.inject.Named;
import java.io.Console;
import java.util.List;

/**
 * Created by majid on 10/13/2016.
 */
@Named
public class CompanyManagerExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<CompanyManager>
    implements CompanyManagerExtendedRepository

    {

    @Override
    public List<CompanyManager> getCompanyManagerByPersonInfoID(Long personInfoID) {
                 return getEm().createQuery("select cm from CompanyManager cm  where cm.personInfo.id = :personInfoID ").setParameter("personInfoID", personInfoID).getResultList();

    }
}
