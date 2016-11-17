(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('SelectorDataTypeSearch', SelectorDataTypeSearch);

    SelectorDataTypeSearch.$inject = ['$resource'];

    function SelectorDataTypeSearch($resource) {
        var resourceUrl =  'api/_search/selector-data-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
