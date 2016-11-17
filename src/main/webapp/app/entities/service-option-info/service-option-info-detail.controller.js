(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceOptionInfoDetailController', ServiceOptionInfoDetailController);

    ServiceOptionInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceOptionInfo', 'ServiceItem'];

    function ServiceOptionInfoDetailController($scope, $rootScope, $stateParams, entity, ServiceOptionInfo, ServiceItem) {
        var vm = this;

        vm.serviceOptionInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceOptionInfoUpdate', function(event, result) {
            vm.serviceOptionInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
