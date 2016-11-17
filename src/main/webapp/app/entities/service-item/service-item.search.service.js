(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceItemSearch', ServiceItemSearch);

    ServiceItemSearch.$inject = ['$resource'];

    function ServiceItemSearch($resource) {
        var resourceUrl =  'api/_search/service-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
