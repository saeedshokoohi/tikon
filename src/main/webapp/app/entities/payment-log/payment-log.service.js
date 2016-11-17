(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PaymentLog', PaymentLog);

    PaymentLog.$inject = ['$resource', 'DateUtils'];

    function PaymentLog ($resource, DateUtils) {
        var resourceUrl =  'api/payment-logs/:id';

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
