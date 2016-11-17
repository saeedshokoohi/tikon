package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.CancelingInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CancelingInfo entity.
 */
public interface CancelingInfoSearchRepository extends ElasticsearchRepository<CancelingInfo, Long> {
}
