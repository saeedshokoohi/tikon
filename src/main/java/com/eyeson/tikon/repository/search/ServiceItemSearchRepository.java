package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ServiceItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceItem entity.
 */
public interface ServiceItemSearchRepository extends ElasticsearchRepository<ServiceItem, Long> {
}
