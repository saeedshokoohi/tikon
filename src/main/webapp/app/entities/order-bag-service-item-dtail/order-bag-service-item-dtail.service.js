(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('OrderBagServiceItemDtail', OrderBagServiceItemDtail);

    OrderBagServiceItemDtail.$inject = ['$resource', 'DateUtils'];

    function OrderBagServiceItemDtail ($resource, DateUtils) {
        var resourceUrl =  'api/order-bag-service-item-dtails/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.reserveTime = DateUtils.convertDateTimeFromServer(data.reserveTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
