(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('WeeklyScheduleInfo', WeeklyScheduleInfo);

    WeeklyScheduleInfo.$inject = ['$resource'];

    function WeeklyScheduleInfo ($resource) {
        var resourceUrl =  'api/weekly-schedule-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
