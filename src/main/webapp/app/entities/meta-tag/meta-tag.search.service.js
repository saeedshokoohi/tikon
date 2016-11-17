(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('MetaTagSearch', MetaTagSearch);

    MetaTagSearch.$inject = ['$resource'];

    function MetaTagSearch($resource) {
        var resourceUrl =  'api/_search/meta-tags/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
