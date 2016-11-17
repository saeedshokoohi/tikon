package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.OrderBag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderBag entity.
 */
public interface OrderBagSearchRepository extends ElasticsearchRepository<OrderBag, Long> {
}
