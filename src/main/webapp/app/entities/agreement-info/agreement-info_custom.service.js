(function() {
    'use strict';
    angular
        .module('tikonApp')
        .factory('AgreementInfoCustom', AgreementInfoCustom);

    AgreementInfoCustom.$inject = ['$resource'];

    function AgreementInfoCustom ($resource) {



        var AgreementInfoByServiceItem=function(serviceItemId)
        {
            var resourceUrl='api/agreement-info-by-service-item/'+serviceItemId;
            return $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true}
                }

            );
        };



        return {
            AgreementInfoByServiceItem:AgreementInfoByServiceItem
        };
    }
})();
