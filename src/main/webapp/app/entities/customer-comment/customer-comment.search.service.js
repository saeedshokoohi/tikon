(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('CustomerCommentSearch', CustomerCommentSearch);

    CustomerCommentSearch.$inject = ['$resource'];

    function CustomerCommentSearch($resource) {
        var resourceUrl =  'api/_search/customer-comments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
