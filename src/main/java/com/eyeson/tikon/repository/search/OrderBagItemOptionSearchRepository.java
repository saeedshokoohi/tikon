package com.eyeson.tikon.repository.search;

import com.eyeson.tikon.domain.OrderBagItemOption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderBagItemOption entity.
 */
public interface OrderBagItemOptionSearchRepository extends ElasticsearchRepository<OrderBagItemOption, Long> {
}
