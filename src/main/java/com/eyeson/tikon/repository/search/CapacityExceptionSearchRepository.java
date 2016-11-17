package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.CapacityException;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CapacityException entity.
 */
public interface CapacityExceptionSearchRepository extends ElasticsearchRepository<CapacityException, Long> {
}
