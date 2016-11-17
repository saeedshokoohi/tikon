(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceOptionItemSearch', ServiceOptionItemSearch);

    ServiceOptionItemSearch.$inject = ['$resource'];

    function ServiceOptionItemSearch($resource) {
        var resourceUrl =  'api/_search/service-option-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
