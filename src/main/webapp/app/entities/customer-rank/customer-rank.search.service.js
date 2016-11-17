(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CustomerRankSearch', CustomerRankSearch);

    CustomerRankSearch.$inject = ['$resource'];

    function CustomerRankSearch($resource) {
        var resourceUrl =  'api/_search/customer-ranks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
