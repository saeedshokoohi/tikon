(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('ImageDataSearch', ImageDataSearch);

    ImageDataSearch.$inject = ['$resource'];

    function ImageDataSearch($resource) {
        var resourceUrl =  'api/_search/image-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
