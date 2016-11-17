package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.SocialNetworkInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SocialNetworkInfo entity.
 */
public interface SocialNetworkInfoSearchRepository extends ElasticsearchRepository<SocialNetworkInfo, Long> {
}
