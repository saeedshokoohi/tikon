package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ServiceTimeSession;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceTimeSession entity.
 */
public interface ServiceTimeSessionSearchRepository extends ElasticsearchRepository<ServiceTimeSession, Long> {
}
