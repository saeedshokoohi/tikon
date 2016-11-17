package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.SelectorData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SelectorData entity.
 */
public interface SelectorDataSearchRepository extends ElasticsearchRepository<SelectorData, Long> {

}
