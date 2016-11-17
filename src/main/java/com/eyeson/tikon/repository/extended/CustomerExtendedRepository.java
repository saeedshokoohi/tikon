package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.Customer;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/13/2016.
 */
@Named
public interface CustomerExtendedRepository extends BaseExtendedRepository<Customer> {
    List<Customer> getCustomerByPersonInfoID(Long personInfoID);
}
