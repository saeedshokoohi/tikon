package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.Customer;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/13/2016.
 */
@Named
public class CustomerExtendedRepositoryImpl
    extends BaseExtendedRepositoryImpl<Customer>
    implements CustomerExtendedRepository

    {

    @Override
    public List<Customer> getCustomerByPersonInfoID(Long personInfoID) {
       return getEm().createQuery("select c from Customer c  where c.personalInfo.id = :personInfoID ").setParameter("personInfoID",personInfoID).getResultList();

    }
}
