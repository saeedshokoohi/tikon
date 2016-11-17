    package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.Customer;
import com.eyeson.tikon.domain.PersonInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/13/2016.
 */
@Named
public class PersonInfoExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<PersonInfo>
    implements PersonInfoExtendedRepository

    {

    @Override
    public List<PersonInfo> findOneByPhoneNumber(String phoneNumber) {
       return getEm().createQuery("select c from PersonInfo c  where c.phoneNumber = :phoneNumber ").setParameter("phoneNumber",phoneNumber).getResultList();

    }
}
