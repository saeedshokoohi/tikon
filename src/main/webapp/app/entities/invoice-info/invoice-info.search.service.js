(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('InvoiceInfoSearch', InvoiceInfoSearch);

    InvoiceInfoSearch.$inject = ['$resource'];

    function InvoiceInfoSearch($resource) {
        var resourceUrl =  'api/_search/invoice-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
