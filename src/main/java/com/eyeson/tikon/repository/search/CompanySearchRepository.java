package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Company entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, Long> {
}
