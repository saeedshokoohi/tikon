package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.LocationInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LocationInfo entity.
 */
public interface LocationInfoSearchRepository extends ElasticsearchRepository<LocationInfo, Long> {
}
