package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ParticipantPerson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ParticipantPerson entity.
 */
public interface ParticipantPersonSearchRepository extends ElasticsearchRepository<ParticipantPerson, Long> {
}
