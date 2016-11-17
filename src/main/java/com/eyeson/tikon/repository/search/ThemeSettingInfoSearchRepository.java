package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ThemeSettingInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ThemeSettingInfo entity.
 */
public interface ThemeSettingInfoSearchRepository extends ElasticsearchRepository<ThemeSettingInfo, Long> {
}
