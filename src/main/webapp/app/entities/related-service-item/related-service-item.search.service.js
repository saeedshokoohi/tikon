(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('RelatedServiceItemSearch', RelatedServiceItemSearch);

    RelatedServiceItemSearch.$inject = ['$resource'];

    function RelatedServiceItemSearch($resource) {
        var resourceUrl =  'api/_search/related-service-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
