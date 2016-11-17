package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.OrderBagServiceItemDtail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderBagServiceItemDtail entity.
 */
public interface OrderBagServiceItemDtailSearchRepository extends ElasticsearchRepository<OrderBagServiceItemDtail, Long> {
}
