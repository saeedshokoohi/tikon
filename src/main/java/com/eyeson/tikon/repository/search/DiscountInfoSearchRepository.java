package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.DiscountInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DiscountInfo entity.
 */
public interface DiscountInfoSearchRepository extends ElasticsearchRepository<DiscountInfo, Long> {
}
