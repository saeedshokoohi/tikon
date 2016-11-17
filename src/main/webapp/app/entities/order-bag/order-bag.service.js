(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('OrderBag', OrderBag);

    OrderBag.$inject = ['$resource', 'DateUtils'];

    function OrderBag ($resource, DateUtils) {
        var resourceUrl =  'api/order-bags/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
