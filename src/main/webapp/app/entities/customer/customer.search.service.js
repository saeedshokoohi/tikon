(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CustomerSearch', CustomerSearch);

    CustomerSearch.$inject = ['$resource'];

    function CustomerSearch($resource) {
        var resourceUrl =  'api/_search/customers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
