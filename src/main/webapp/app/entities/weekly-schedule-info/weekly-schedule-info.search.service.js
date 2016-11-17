(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('WeeklyScheduleInfoSearch', WeeklyScheduleInfoSearch);

    WeeklyScheduleInfoSearch.$inject = ['$resource'];

    function WeeklyScheduleInfoSearch($resource) {
        var resourceUrl =  'api/_search/weekly-schedule-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
