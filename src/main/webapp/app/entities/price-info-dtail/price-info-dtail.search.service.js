(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('PriceInfoDtailSearch', PriceInfoDtailSearch);

    PriceInfoDtailSearch.$inject = ['$resource'];

    function PriceInfoDtailSearch($resource) {
        var resourceUrl =  'api/_search/price-info-dtails/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
