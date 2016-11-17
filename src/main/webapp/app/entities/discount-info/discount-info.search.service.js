(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('DiscountInfoSearch', DiscountInfoSearch);

    DiscountInfoSearch.$inject = ['$resource'];

    function DiscountInfoSearch($resource) {
        var resourceUrl =  'api/_search/discount-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
