(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('AlbumInfoExtend', AlbumInfoExtend);

    AlbumInfoExtend.$inject = ['$resource'];

    function AlbumInfoExtend($resource) {


        var findAlbumImages=function(albumId)
        {
            var resourceUrl='api/image-data-by-albumid/'+albumId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };
        return{
            findAlbumImages:findAlbumImages
        };
    }
})();
