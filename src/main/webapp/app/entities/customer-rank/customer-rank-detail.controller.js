(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerRankDetailController', CustomerRankDetailController);

    CustomerRankDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CustomerRank', 'Customer', 'ServiceItem'];

    function CustomerRankDetailController($scope, $rootScope, $stateParams, entity, CustomerRank, Customer, ServiceItem) {
        var vm = this;

        vm.customerRank = entity;

        var unsubscribe = $rootScope.$on('tikonApp:customerRankUpdate', function(event, result) {
            vm.customerRank = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
