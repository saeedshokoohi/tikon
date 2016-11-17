package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.PaymentLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PaymentLog entity.
 */
public interface PaymentLogSearchRepository extends ElasticsearchRepository<PaymentLog, Long> {
}
