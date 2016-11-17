package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.AgreementInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AgreementInfo entity.
 */
public interface AgreementInfoSearchRepository extends ElasticsearchRepository<AgreementInfo, Long> {
}
