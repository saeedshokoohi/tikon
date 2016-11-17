package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.RelatedServiceItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RelatedServiceItem entity.
 */
public interface RelatedServiceItemSearchRepository extends ElasticsearchRepository<RelatedServiceItem, Long> {
}
