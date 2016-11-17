(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceItemDeleteController',ServiceItemDeleteController);

    ServiceItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceItem'];

    function ServiceItemDeleteController($uibModalInstance, entity, ServiceItem) {
        var vm = this;

        vm.serviceItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
