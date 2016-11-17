(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceTimeSessionDetailController', ServiceTimeSessionDetailController);

    ServiceTimeSessionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceTimeSession', 'ServiceTimeSessionInfo'];

    function ServiceTimeSessionDetailController($scope, $rootScope, $stateParams, entity, ServiceTimeSession, ServiceTimeSessionInfo) {
        var vm = this;

        vm.serviceTimeSession = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceTimeSessionUpdate', function(event, result) {
            vm.serviceTimeSession = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
