(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('ServantCustom', ServantCustom);

    ServantCustom.$inject = ['$resource'];

    function ServantCustom ($resource) {



        var findServantsByCompany=function()
        {
            var resourceUrl='api/servants-by-company';
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };


        var findServantsByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/servants-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };


        return {
            findServantsByCompany:findServantsByCompany,
            findServantsByServiceItem:findServantsByServiceItem
        };
    }
})();
