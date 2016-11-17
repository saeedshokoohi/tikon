(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceOptionInfoSearch', ServiceOptionInfoSearch);

    ServiceOptionInfoSearch.$inject = ['$resource'];

    function ServiceOptionInfoSearch($resource) {
        var resourceUrl =  'api/_search/service-option-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
