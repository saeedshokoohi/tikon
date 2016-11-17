package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ServiceOptionInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceOptionInfo entity.
 */
public interface ServiceOptionInfoSearchRepository extends ElasticsearchRepository<ServiceOptionInfo, Long> {
}
