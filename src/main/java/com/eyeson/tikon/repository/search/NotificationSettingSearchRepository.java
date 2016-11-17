package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.NotificationSetting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationSetting entity.
 */
public interface NotificationSettingSearchRepository extends ElasticsearchRepository<NotificationSetting, Long> {
}
