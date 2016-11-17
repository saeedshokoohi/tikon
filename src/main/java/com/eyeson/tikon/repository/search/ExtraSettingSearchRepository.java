package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ExtraSetting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ExtraSetting entity.
 */
public interface ExtraSettingSearchRepository extends ElasticsearchRepository<ExtraSetting, Long> {
}
