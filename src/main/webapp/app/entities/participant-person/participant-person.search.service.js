(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ParticipantPersonSearch', ParticipantPersonSearch);

    ParticipantPersonSearch.$inject = ['$resource'];

    function ParticipantPersonSearch($resource) {
        var resourceUrl =  'api/_search/participant-people/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
