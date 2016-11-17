package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.AlbumInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AlbumInfo entity.
 */
public interface AlbumInfoSearchRepository extends ElasticsearchRepository<AlbumInfo, Long> {
}
