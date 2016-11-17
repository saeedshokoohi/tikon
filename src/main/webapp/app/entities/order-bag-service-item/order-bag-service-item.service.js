(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('OrderBagServiceItem', OrderBagServiceItem);

    OrderBagServiceItem.$inject = ['$resource'];

    function OrderBagServiceItem ($resource) {
        var resourceUrl =  'api/order-bag-service-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
