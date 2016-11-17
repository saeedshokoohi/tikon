(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceTimeSessionSearch', ServiceTimeSessionSearch);

    ServiceTimeSessionSearch.$inject = ['$resource'];

    function ServiceTimeSessionSearch($resource) {
        var resourceUrl =  'api/_search/service-time-sessions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
