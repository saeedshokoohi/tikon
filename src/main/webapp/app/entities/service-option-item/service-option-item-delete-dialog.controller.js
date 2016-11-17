(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceOptionItemDeleteController',ServiceOptionItemDeleteController);

    ServiceOptionItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceOptionItem'];

    function ServiceOptionItemDeleteController($uibModalInstance, entity, ServiceOptionItem) {
        var vm = this;

        vm.serviceOptionItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceOptionItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
