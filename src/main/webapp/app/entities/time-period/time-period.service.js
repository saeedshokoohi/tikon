(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('TimePeriod', TimePeriod);

    TimePeriod.$inject = ['$resource', 'DateUtils'];

    function TimePeriod ($resource, DateUtils) {
        var resourceUrl =  'api/time-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
