(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerRankDeleteController',CustomerRankDeleteController);

    CustomerRankDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerRank'];

    function CustomerRankDeleteController($uibModalInstance, entity, CustomerRank) {
        var vm = this;

        vm.customerRank = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerRank.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
