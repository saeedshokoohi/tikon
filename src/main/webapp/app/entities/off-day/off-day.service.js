(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('OffDay', OffDay);

    OffDay.$inject = ['$resource', 'DateUtils'];

    function OffDay ($resource, DateUtils) {
        var resourceUrl =  'api/off-days/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.offDate = DateUtils.convertLocalDateFromServer(data.offDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.offDate = DateUtils.convertLocalDateToServer(data.offDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.offDate = DateUtils.convertLocalDateToServer(data.offDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
