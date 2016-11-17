package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ServiceOptionItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceOptionItem entity.
 */
public interface ServiceOptionItemSearchRepository extends ElasticsearchRepository<ServiceOptionItem, Long> {
}
