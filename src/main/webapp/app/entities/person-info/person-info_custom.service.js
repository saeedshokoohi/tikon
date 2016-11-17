//(function() {
//    'use strict';
//    angular
//        .module('tikonApp')
//        .factory('PersonInfoCustom', PersonInfoCustom);
//
//    PersonInfoCustom.$inject = ['$resource'];
//
//    function PersonInfoCustom ($resource) {
//
//
//
//        var PersonInfosByServiceItem=function(serviceItemId)
//        {
//            var resourceUrl='api/Person-info-by-service-item/'+serviceItemId;
//            return $resource(resourceUrl, {}, {
//                    'query': { method: 'GET', isArray: true}
//                }
//
//            );
//        };
//
//
//
//        return {
//            PersonInfosByServiceItem:PersonInfosByServiceItem
//        };
//    }
//})();
