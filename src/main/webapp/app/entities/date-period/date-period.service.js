(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('DatePeriod', DatePeriod);

    DatePeriod.$inject = ['$resource', 'DateUtils'];

    function DatePeriod ($resource, DateUtils) {
        var resourceUrl =  'api/date-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fromDate = DateUtils.convertLocalDateFromServer(data.fromDate);
                        data.toDate = DateUtils.convertLocalDateFromServer(data.toDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fromDate = DateUtils.convertLocalDateToServer(data.fromDate);
                    data.toDate = DateUtils.convertLocalDateToServer(data.toDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fromDate = DateUtils.convertLocalDateToServer(data.fromDate);
                    data.toDate = DateUtils.convertLocalDateToServer(data.toDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
