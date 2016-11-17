package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.WeeklyScheduleInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WeeklyScheduleInfo entity.
 */
public interface WeeklyScheduleInfoSearchRepository extends ElasticsearchRepository<WeeklyScheduleInfo, Long> {
}
