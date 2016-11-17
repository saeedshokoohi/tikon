(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PersonInfo', PersonInfo);

    PersonInfo.$inject = ['$resource', 'DateUtils'];

    function PersonInfo ($resource, DateUtils) {
        var resourceUrl =  'api/person-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthDate = DateUtils.convertDateTimeFromServer(data.birthDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
