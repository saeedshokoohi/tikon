(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServiceCategorySearch', ServiceCategorySearch);

    ServiceCategorySearch.$inject = ['$resource'];

    function ServiceCategorySearch($resource) {
        var resourceUrl =  'api/_search/service-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
