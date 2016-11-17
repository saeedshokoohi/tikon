(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OffTimeDeleteController',OffTimeDeleteController);

    OffTimeDeleteController.$inject = ['$uibModalInstance', 'entity', 'OffTime'];

    function OffTimeDeleteController($uibModalInstance, entity, OffTime) {
        var vm = this;

        vm.offTime = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OffTime.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
