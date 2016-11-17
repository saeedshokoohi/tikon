(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('TimePeriodSearch', TimePeriodSearch);

    TimePeriodSearch.$inject = ['$resource'];

    function TimePeriodSearch($resource) {
        var resourceUrl =  'api/_search/time-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
