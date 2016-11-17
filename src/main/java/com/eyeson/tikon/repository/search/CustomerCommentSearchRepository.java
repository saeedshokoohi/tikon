package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.CustomerComment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CustomerComment entity.
 */
public interface CustomerCommentSearchRepository extends ElasticsearchRepository<CustomerComment, Long> {
}
