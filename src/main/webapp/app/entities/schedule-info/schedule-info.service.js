(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ScheduleInfo', ScheduleInfo);

    ScheduleInfo.$inject = ['$resource'];

    function ScheduleInfo ($resource) {
        var resourceUrl =  'api/schedule-infos/:id';

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
