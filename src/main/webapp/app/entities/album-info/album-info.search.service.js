(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('AlbumInfoSearch', AlbumInfoSearch);

    AlbumInfoSearch.$inject = ['$resource'];

    function AlbumInfoSearch($resource) {
        var resourceUrl =  'api/_search/album-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
