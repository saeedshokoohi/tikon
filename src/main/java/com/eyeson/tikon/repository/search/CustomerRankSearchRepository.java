package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.CustomerRank;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CustomerRank entity.
 */
public interface CustomerRankSearchRepository extends ElasticsearchRepository<CustomerRank, Long> {
}
