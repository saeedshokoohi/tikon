(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('OrderBagSearch', OrderBagSearch);

    OrderBagSearch.$inject = ['$resource'];

    function OrderBagSearch($resource) {
        var resourceUrl =  'api/_search/order-bags/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
