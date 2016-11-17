(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Customer', 'PersonInfo'];

    function CustomerDetailController($scope, $rootScope, $stateParams, entity, Customer, PersonInfo) {
        var vm = this;

        vm.customer = entity;

        var unsubscribe = $rootScope.$on('tikonApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
