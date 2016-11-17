package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.DatePeriod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DatePeriod entity.
 */
public interface DatePeriodSearchRepository extends ElasticsearchRepository<DatePeriod, Long> {
}
