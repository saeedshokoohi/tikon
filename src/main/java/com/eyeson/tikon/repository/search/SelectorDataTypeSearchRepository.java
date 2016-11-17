package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.SelectorDataType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SelectorDataType entity.
 */
public interface SelectorDataTypeSearchRepository extends ElasticsearchRepository<SelectorDataType, Long> {
}
