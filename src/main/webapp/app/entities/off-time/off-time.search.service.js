(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('OffTimeSearch', OffTimeSearch);

    OffTimeSearch.$inject = ['$resource'];

    function OffTimeSearch($resource) {
        var resourceUrl =  'api/_search/off-times/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
