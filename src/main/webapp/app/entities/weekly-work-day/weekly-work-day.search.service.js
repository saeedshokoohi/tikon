(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('WeeklyWorkDaySearch', WeeklyWorkDaySearch);

    WeeklyWorkDaySearch.$inject = ['$resource'];

    function WeeklyWorkDaySearch($resource) {
        var resourceUrl =  'api/_search/weekly-work-days/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
