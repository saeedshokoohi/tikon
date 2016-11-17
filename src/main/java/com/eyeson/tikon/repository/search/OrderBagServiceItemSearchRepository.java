package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.OrderBagServiceItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderBagServiceItem entity.
 */
public interface OrderBagServiceItemSearchRepository extends ElasticsearchRepository<OrderBagServiceItem, Long> {
}
