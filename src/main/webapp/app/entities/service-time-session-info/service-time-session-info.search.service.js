(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceTimeSessionInfoSearch', ServiceTimeSessionInfoSearch);

    ServiceTimeSessionInfoSearch.$inject = ['$resource'];

    function ServiceTimeSessionInfoSearch($resource) {
        var resourceUrl =  'api/_search/service-time-session-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
