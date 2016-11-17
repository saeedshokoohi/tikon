(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('PersonInfoSearch', PersonInfoSearch);

    PersonInfoSearch.$inject = ['$resource'];

    function PersonInfoSearch($resource) {
        var resourceUrl =  'api/_search/person-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
