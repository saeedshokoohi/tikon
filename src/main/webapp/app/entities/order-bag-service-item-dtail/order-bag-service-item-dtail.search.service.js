(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('OrderBagServiceItemDtailSearch', OrderBagServiceItemDtailSearch);

    OrderBagServiceItemDtailSearch.$inject = ['$resource'];

    function OrderBagServiceItemDtailSearch($resource) {
        var resourceUrl =  'api/_search/order-bag-service-item-dtails/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
