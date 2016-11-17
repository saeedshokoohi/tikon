(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CapacityExceptionDetailController', CapacityExceptionDetailController);

    CapacityExceptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CapacityException', 'ServiceCapacityInfo'];

    function CapacityExceptionDetailController($scope, $rootScope, $stateParams, entity, CapacityException, ServiceCapacityInfo) {
        var vm = this;

        vm.capacityException = entity;

        var unsubscribe = $rootScope.$on('tikonApp:capacityExceptionUpdate', function(event, result) {
            vm.capacityException = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
