package com.eyeson.tikon.repository.extended;

import com.eyeson.tikon.domain.PersonInfo;

import javax.inject.Named;
import java.util.List;

/**
 * Created by majid on 10/13/2016.
 */
@Named
public interface PersonInfoExtendedRepository extends BaseExtendedRepository<PersonInfo> {
    List<PersonInfo> findOneByPhoneNumber(String phoneNumber);
}
