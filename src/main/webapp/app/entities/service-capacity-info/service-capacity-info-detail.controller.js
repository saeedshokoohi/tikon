(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceCapacityInfoDetailController', ServiceCapacityInfoDetailController);

    ServiceCapacityInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceCapacityInfo'];

    function ServiceCapacityInfoDetailController($scope, $rootScope, $stateParams, entity, ServiceCapacityInfo) {
        var vm = this;

        vm.serviceCapacityInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceCapacityInfoUpdate', function(event, result) {
            vm.serviceCapacityInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
