(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CapacityExceptionDeleteController',CapacityExceptionDeleteController);

    CapacityExceptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'CapacityException'];

    function CapacityExceptionDeleteController($uibModalInstance, entity, CapacityException) {
        var vm = this;

        vm.capacityException = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CapacityException.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
