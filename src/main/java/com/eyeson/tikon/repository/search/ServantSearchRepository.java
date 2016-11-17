package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.Servant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Servant entity.
 */
public interface ServantSearchRepository extends ElasticsearchRepository<Servant, Long> {
}
