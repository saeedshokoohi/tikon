package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.ImageData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ImageData entity.
 */
public interface ImageDataSearchRepository extends ElasticsearchRepository<ImageData, Long> {
}
