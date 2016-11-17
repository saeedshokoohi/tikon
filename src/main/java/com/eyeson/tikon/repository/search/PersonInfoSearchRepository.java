package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.PersonInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PersonInfo entity.
 */
public interface PersonInfoSearchRepository extends ElasticsearchRepository<PersonInfo, Long> {
}
