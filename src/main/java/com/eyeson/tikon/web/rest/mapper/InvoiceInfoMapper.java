package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.InvoiceInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity InvoiceInfo and its DTO InvoiceInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {PaymentLogMapper.class, })
public interface InvoiceInfoMapper {

    @Mapping(source = "orderBag.id", target = "orderBagId")
    @Mapping(source = "customer.id", target = "customerId")
    InvoiceInfoDTO invoiceInfoToInvoiceInfoDTO(InvoiceInfo invoiceInfo);

    List<InvoiceInfoDTO> invoiceInfosToInvoiceInfoDTOs(List<InvoiceInfo> invoiceInfos);

    @Mapping(source = "orderBagId", target = "orderBag")
    @Mapping(source = "customerId", target = "customer")
    InvoiceInfo invoiceInfoDTOToInvoiceInfo(InvoiceInfoDTO invoiceInfoDTO);

    List<InvoiceInfo> invoiceInfoDTOsToInvoiceInfos(List<InvoiceInfoDTO> invoiceInfoDTOs);

    default OrderBag orderBagFromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderBag orderBag = new OrderBag();
        orderBag.setId(id);
        return orderBag;
    }

    default PaymentLog paymentLogFromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setId(id);
        return paymentLog;
    }

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
