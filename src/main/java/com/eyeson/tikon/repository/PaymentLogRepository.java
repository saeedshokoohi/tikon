package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.PaymentLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaymentLog entity.
 */
@SuppressWarnings("unused")
public interface PaymentLogRepository extends JpaRepository<PaymentLog,Long> {

}
