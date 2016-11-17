package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ServiceCapacityInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceCapacityInfo entity.
 */
public interface ServiceCapacityInfoSearchRepository extends ElasticsearchRepository<ServiceCapacityInfo, Long> {
}
