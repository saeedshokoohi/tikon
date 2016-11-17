(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('PriceInfoSearch', PriceInfoSearch);

    PriceInfoSearch.$inject = ['$resource'];

    function PriceInfoSearch($resource) {
        var resourceUrl =  'api/_search/price-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
