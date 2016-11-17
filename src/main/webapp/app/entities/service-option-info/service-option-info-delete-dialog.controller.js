(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceOptionInfoDeleteController',ServiceOptionInfoDeleteController);

    ServiceOptionInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceOptionInfo'];

    function ServiceOptionInfoDeleteController($uibModalInstance, entity, ServiceOptionInfo) {
        var vm = this;

        vm.serviceOptionInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceOptionInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
