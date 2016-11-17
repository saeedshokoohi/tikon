(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CancelingInfo', CancelingInfo);

    CancelingInfo.$inject = ['$resource'];

    function CancelingInfo ($resource) {
        var resourceUrl =  'api/canceling-infos/:id';

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
