package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.PriceInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PriceInfo entity.
 */
public interface PriceInfoSearchRepository extends ElasticsearchRepository<PriceInfo, Long> {
}
