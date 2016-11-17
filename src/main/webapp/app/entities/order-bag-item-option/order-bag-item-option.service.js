(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('OrderBagItemOption', OrderBagItemOption);

    OrderBagItemOption.$inject = ['$resource', 'DateUtils'];

    function OrderBagItemOption ($resource, DateUtils) {
        var resourceUrl =  'api/order-bag-item-options/:id';

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
