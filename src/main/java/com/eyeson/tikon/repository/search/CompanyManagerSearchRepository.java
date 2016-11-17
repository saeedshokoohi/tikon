package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.CompanyManager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanyManager entity.
 */
public interface CompanyManagerSearchRepository extends ElasticsearchRepository<CompanyManager, Long> {
}
