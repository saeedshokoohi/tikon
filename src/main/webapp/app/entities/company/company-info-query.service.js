(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('CompanyInfoQuery', CompanyInfoQuery);

    CompanyInfoQuery.$inject = ['$resource'];

    function CompanyInfoQuery ($resource) {


        var getCurrentCompanyInfo=function()
        {
            var resourceUrl =  'api/getCurrentCompanyInfo';
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: false}
                }

            );
        };


        return {
            getCurrentCompanyInfo:getCurrentCompanyInfo
        };
    }
})();
