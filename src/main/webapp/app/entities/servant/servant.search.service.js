(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ServantSearch', ServantSearch);

    ServantSearch.$inject = ['$resource'];

    function ServantSearch($resource) {
        var resourceUrl =  'api/_search/servants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
