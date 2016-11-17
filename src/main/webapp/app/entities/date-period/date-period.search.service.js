(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('DatePeriodSearch', DatePeriodSearch);

    DatePeriodSearch.$inject = ['$resource'];

    function DatePeriodSearch($resource) {
        var resourceUrl =  'api/_search/date-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
