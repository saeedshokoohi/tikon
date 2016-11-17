(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceCategoryDeleteController',ServiceCategoryDeleteController);

    ServiceCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceCategory'];

    function ServiceCategoryDeleteController($uibModalInstance, entity, ServiceCategory) {
        var vm = this;

        vm.serviceCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
