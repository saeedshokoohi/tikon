package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ServiceTimeSessionInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceTimeSessionInfo entity.
 */
public interface ServiceTimeSessionInfoSearchRepository extends ElasticsearchRepository<ServiceTimeSessionInfo, Long> {
}
