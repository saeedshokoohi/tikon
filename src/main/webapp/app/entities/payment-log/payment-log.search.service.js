(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('PaymentLogSearch', PaymentLogSearch);

    PaymentLogSearch.$inject = ['$resource'];

    function PaymentLogSearch($resource) {
        var resourceUrl =  'api/_search/payment-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
