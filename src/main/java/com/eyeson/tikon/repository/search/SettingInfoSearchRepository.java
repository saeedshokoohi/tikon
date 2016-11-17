package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.SettingInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SettingInfo entity.
 */
public interface SettingInfoSearchRepository extends ElasticsearchRepository<SettingInfo, Long> {
}
