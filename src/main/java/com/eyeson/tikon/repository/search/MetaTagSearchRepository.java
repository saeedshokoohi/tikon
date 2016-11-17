package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.MetaTag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MetaTag entity.
 */
public interface MetaTagSearchRepository extends ElasticsearchRepository<MetaTag, Long> {
}
