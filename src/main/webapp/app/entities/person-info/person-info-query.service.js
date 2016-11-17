(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('PersonInfoQuery', PersonInfoQuery);

    PersonInfoQuery.$inject = ['$resource'];

    function PersonInfoQuery ($resource) {


        var findByFirstName=function(firstName)
        {
            var resourceUrl =  'api/getPersonInfoByFirstName/'+firstName;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };


        return {
            findByFirstName:findByFirstName
        };
    }
})();
