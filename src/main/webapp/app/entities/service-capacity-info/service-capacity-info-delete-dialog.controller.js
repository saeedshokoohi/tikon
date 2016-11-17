(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceCapacityInfoDeleteController',ServiceCapacityInfoDeleteController);

    ServiceCapacityInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceCapacityInfo'];

    function ServiceCapacityInfoDeleteController($uibModalInstance, entity, ServiceCapacityInfo) {
        var vm = this;

        vm.serviceCapacityInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceCapacityInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
