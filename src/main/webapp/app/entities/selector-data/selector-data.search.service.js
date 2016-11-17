(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('SelectorDataSearch', SelectorDataSearch);

    SelectorDataSearch.$inject = ['$resource'];

    function SelectorDataSearch($resource) {
        var resourceUrl =  'api/_search/selector-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
