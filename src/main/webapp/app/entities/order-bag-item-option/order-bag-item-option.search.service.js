(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('OrderBagItemOptionSearch', OrderBagItemOptionSearch);

    OrderBagItemOptionSearch.$inject = ['$resource'];

    function OrderBagItemOptionSearch($resource) {
        var resourceUrl =  'api/_search/order-bag-item-options/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
