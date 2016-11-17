package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.Authority;
import com.eyeson.tikon.domain.Customer;
import com.eyeson.tikon.domain.User;
import com.eyeson.tikon.domain.PersonInfo;
import com.eyeson.tikon.repository.AuthorityRepository;
import com.eyeson.tikon.repository.PersistentTokenRepository;
import com.eyeson.tikon.repository.PersonInfoRepository;
import com.eyeson.tikon.repository.UserRepository;
import com.eyeson.tikon.repository.extended.CustomerExtendedRepository;
import com.eyeson.tikon.repository.extended.PersonInfoExtendedRepository;
import com.eyeson.tikon.repository.search.PersonInfoSearchRepository;
import com.eyeson.tikon.repository.search.UserSearchRepository;
import com.eyeson.tikon.repository.CustomerRepository;
import com.eyeson.tikon.repository.search.CustomerSearchRepository;
import com.eyeson.tikon.security.SecurityUtils;
import com.eyeson.tikon.service.util.RandomUtil;
import com.eyeson.tikon.web.rest.dto.ManagedUserDTO;
import com.eyeson.tikon.web.rest.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);


    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private PersonInfoRepository personInfoRepository;

    @Inject
    private PersonInfoExtendedRepository personInfoExtendedRepository;

    @Inject
    private PersonInfoSearchRepository personInfoSearchRepository;


    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerExtendedRepository customerExtendedRepository;

    @Inject
    private CustomerSearchRepository customerSearchRepository;


    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String login, String key) {
        log.debug("Activating user for activation key {}", key);
        Optional<User> users = userRepository.findOneByLogin(login);


        return userRepository.findOneByLogin(login)
            .map(user -> {
                // activate given user for the registration key.
                if (user.getActivationKey().equals(key)) {
                    user.setActivated(true);
                    user.setActivationKey(null);
                    userRepository.save(user);
                    userSearchRepository.save(user);
                    log.debug("Activated user: {}", user);

                } else {
                    log.debug("Activation for user is Invalid: {}", user);
                    user = null;

                }
                return user;

            });


    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
            })
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey, String phoneNumber) {


        PersonInfo personinfo = new PersonInfo();
        personinfo.setFisrtName(firstName);
        personinfo.setLastName(lastName);
        personinfo.setPhoneNumber(phoneNumber);

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);


        userRepository.save(newUser);
        userSearchRepository.save(newUser);

        personInfoRepository.save(personinfo);
        personInfoSearchRepository.save(personinfo);

        Customer newCustomer = new Customer();
        newCustomer.setPersonalInfo(personinfo);
        newCustomer.setUserAccountId(newUser.getId());

        customerRepository.save(newCustomer);
        customerSearchRepository.save(newCustomer);


        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(ManagedUserDTO managedUserDTO) {


        User user = new User();
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        if (managedUserDTO.getLangKey() == null) {
            user.setLangKey("en"); // default language
        } else {
            user.setLangKey(managedUserDTO.getLangKey());
        }
        if (managedUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserDTO.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        userSearchRepository.save(user);


        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            userSearchRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void deleteUserInformation(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            userSearchRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    public void generateNewActivationKey(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            u.setActivationKey(RandomUtil.generateActivationKey());
            userRepository.save(u);
            log.debug("Generate New Key for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        try {
            User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
            user.getAuthorities().size(); // eagerly load the association
            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        User user = null;
        List<PersonInfo> personInfo = personInfoExtendedRepository.findOneByPhoneNumber(phoneNumber);
        if (personInfo.size() > 0) {
            List<Customer> customers = customerExtendedRepository.getCustomerByPersonInfoID(personInfo.get(0).getId());
            if (customers.size() > 0) {
                user = userRepository.findOne(customers.get(0).getUserAccountId());
            }

        }
        if (user == null)
            return Optional.empty();

        return Optional.of(user);
    }


    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
        }
    }
}
