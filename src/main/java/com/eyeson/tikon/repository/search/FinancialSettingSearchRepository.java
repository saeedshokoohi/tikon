package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.FinancialSetting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FinancialSetting entity.
 */
public interface FinancialSettingSearchRepository extends ElasticsearchRepository<FinancialSetting, Long> {
}
