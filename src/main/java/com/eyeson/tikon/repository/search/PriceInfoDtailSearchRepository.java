package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.PriceInfoDtail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PriceInfoDtail entity.
 */
public interface PriceInfoDtailSearchRepository extends ElasticsearchRepository<PriceInfoDtail, Long> {
}
