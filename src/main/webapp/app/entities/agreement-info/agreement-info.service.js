(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('AgreementInfo', AgreementInfo);

    AgreementInfo.$inject = ['$resource'];

    function AgreementInfo ($resource) {
        var resourceUrl =  'api/agreement-infos/:id';

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
