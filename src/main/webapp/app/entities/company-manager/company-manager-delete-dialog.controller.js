(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyManagerDeleteController',CompanyManagerDeleteController);

    CompanyManagerDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyManager'];

    function CompanyManagerDeleteController($uibModalInstance, entity, CompanyManager) {
        var vm = this;

        vm.companyManager = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyManager.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
