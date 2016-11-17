(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('OffDaySearch', OffDaySearch);

    OffDaySearch.$inject = ['$resource'];

    function OffDaySearch($resource) {
        var resourceUrl =  'api/_search/off-days/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
