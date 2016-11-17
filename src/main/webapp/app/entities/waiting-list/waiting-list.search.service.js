(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('WaitingListSearch', WaitingListSearch);

    WaitingListSearch.$inject = ['$resource'];

    function WaitingListSearch($resource) {
        var resourceUrl =  'api/_search/waiting-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
