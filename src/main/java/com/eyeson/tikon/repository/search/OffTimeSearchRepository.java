package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.OffTime;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OffTime entity.
 */
public interface OffTimeSearchRepository extends ElasticsearchRepository<OffTime, Long> {
}
