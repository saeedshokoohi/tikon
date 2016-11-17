package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.InvoiceInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InvoiceInfo entity.
 */
public interface InvoiceInfoSearchRepository extends ElasticsearchRepository<InvoiceInfo, Long> {
}
