package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.OffDay;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OffDay entity.
 */
public interface OffDaySearchRepository extends ElasticsearchRepository<OffDay, Long> {
}
