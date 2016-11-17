package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ScheduleBaseDiscount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ScheduleBaseDiscount entity.
 */
public interface ScheduleBaseDiscountSearchRepository extends ElasticsearchRepository<ScheduleBaseDiscount, Long> {
}
