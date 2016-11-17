package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.CompanySocialNetworkInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanySocialNetworkInfo entity.
 */
public interface CompanySocialNetworkInfoSearchRepository extends ElasticsearchRepository<CompanySocialNetworkInfo, Long> {
}
