package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.WaitingList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WaitingList entity.
 */
public interface WaitingListSearchRepository extends ElasticsearchRepository<WaitingList, Long> {
}
