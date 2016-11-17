package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.WeeklyWorkDay;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WeeklyWorkDay entity.
 */
public interface WeeklyWorkDaySearchRepository extends ElasticsearchRepository<WeeklyWorkDay, Long> {
}
