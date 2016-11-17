(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ParticipantPerson', ParticipantPerson);

    ParticipantPerson.$inject = ['$resource'];

    function ParticipantPerson ($resource) {
        var resourceUrl =  'api/participant-people/:id';

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
