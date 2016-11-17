(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServiceTimeSession', ServiceTimeSession);

    ServiceTimeSession.$inject = ['$resource', 'DateUtils'];

    function ServiceTimeSession ($resource, DateUtils) {
        var resourceUrl =  'api/service-time-sessions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                        data.startDate = DateUtils.convertLocalDateFromServer(data.startDate);
                        data.endDate = DateUtils.convertLocalDateFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startDate = DateUtils.convertLocalDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocalDateToServer(data.endDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startDate = DateUtils.convertLocalDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocalDateToServer(data.endDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
