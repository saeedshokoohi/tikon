(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('SettingInfo', SettingInfo);

    SettingInfo.$inject = ['$resource'];

    function SettingInfo ($resource) {
        var resourceUrl =  'api/setting-infos/:id';

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
