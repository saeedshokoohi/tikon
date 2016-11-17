(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CapacityExceptionSearch', CapacityExceptionSearch);

    CapacityExceptionSearch.$inject = ['$resource'];

    function CapacityExceptionSearch($resource) {
        var resourceUrl =  'api/_search/capacity-exceptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
