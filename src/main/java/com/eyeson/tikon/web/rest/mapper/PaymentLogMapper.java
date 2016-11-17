package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.PaymentLogDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PaymentLog and its DTO PaymentLogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentLogMapper {

    PaymentLogDTO paymentLogToPaymentLogDTO(PaymentLog paymentLog);

    List<PaymentLogDTO> paymentLogsToPaymentLogDTOs(List<PaymentLog> paymentLogs);

    @Mapping(target = "invoiceInfos", ignore = true)
    PaymentLog paymentLogDTOToPaymentLog(PaymentLogDTO paymentLogDTO);

    List<PaymentLog> paymentLogDTOsToPaymentLogs(List<PaymentLogDTO> paymentLogDTOs);
}
