package com.eyeson.tikon.repository;

import com.eyeson.tikon.domain.InvoiceInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the InvoiceInfo entity.
 */
@SuppressWarnings("unused")
public interface InvoiceInfoRepository extends JpaRepository<InvoiceInfo,Long> {

    @Query("select distinct invoiceInfo from InvoiceInfo invoiceInfo left join fetch invoiceInfo.paymentLogs")
    List<InvoiceInfo> findAllWithEagerRelationships();

    @Query("select invoiceInfo from InvoiceInfo invoiceInfo left join fetch invoiceInfo.paymentLogs where invoiceInfo.id =:id")
    InvoiceInfo findOneWithEagerRelationships(@Param("id") Long id);

}
