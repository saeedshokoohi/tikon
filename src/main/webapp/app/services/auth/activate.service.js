(function() {
    'use strict';

    angular
        .module('tikonApp')
        .factory('Activate', Activate);

    Activate.$inject = ['$resource'];

    function Activate ($resource) {
        debugger;
        var activateByKey = function (login, key) {
            debugger;
            var url="api/activate/"+login+"/"+key;
            return $resource(url, {}, {
                    'get': { method: 'GET', isArray: false}
                }

            );

        }
        var regenerateActivationKey = function (login) {
            var url="api/activate/regenerate_key/"+login;
            return $resource(url, {}, {
                    'get': { method: 'GET', isArray: false}
                }

            );

        }

        return {activateByKey: activateByKey,
                regenerateActivationKey:regenerateActivationKey };

        //var service = $resource('api/activate/userid/', {}, {
        //    'get': { method: 'GET', params: {}, isArray: false}
        //});
        //debugger;
        //return service;

    }
})();
