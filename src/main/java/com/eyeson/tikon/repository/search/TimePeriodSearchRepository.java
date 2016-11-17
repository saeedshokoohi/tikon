package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.TimePeriod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TimePeriod entity.
 */
public interface TimePeriodSearchRepository extends ElasticsearchRepository<TimePeriod, Long> {
}
