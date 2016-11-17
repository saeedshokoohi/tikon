(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('OrderBagServiceItemSearch', OrderBagServiceItemSearch);

    OrderBagServiceItemSearch.$inject = ['$resource'];

    function OrderBagServiceItemSearch($resource) {
        var resourceUrl =  'api/_search/order-bag-service-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
