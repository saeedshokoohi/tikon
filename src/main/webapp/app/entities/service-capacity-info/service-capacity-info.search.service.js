(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceCapacityInfoSearch', ServiceCapacityInfoSearch);

    ServiceCapacityInfoSearch.$inject = ['$resource'];

    function ServiceCapacityInfoSearch($resource) {
        var resourceUrl =  'api/_search/service-capacity-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
