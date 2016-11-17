(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('UserProfileQuery', UserProfileQuery);

    UserProfileQuery.$inject = ['$resource'];

    function UserProfileQuery ($resource) {


        var getCurrentPersonInfo=function()
        {
            var resourceUrl =  'api/getCurrentPersonInfo';
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: false}
                }

            );
        };


        return {
            getCurrentPersonInfo:getCurrentPersonInfo
        };
    }
})();
