(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('LocationInfoSearch', LocationInfoSearch);

    LocationInfoSearch.$inject = ['$resource'];

    function LocationInfoSearch($resource) {
        var resourceUrl =  'api/_search/location-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
