(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('AlbumInfo', AlbumInfo);

    AlbumInfo.$inject = ['$resource'];

    function AlbumInfo ($resource) {
        var resourceUrl =  'api/album-infos/:id';

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
