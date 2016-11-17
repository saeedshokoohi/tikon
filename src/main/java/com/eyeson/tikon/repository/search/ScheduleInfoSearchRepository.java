package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ScheduleInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ScheduleInfo entity.
 */
public interface ScheduleInfoSearchRepository extends ElasticsearchRepository<ScheduleInfo, Long> {
}
