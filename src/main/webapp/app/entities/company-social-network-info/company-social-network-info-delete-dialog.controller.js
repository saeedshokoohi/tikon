(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanySocialNetworkInfoDeleteController',CompanySocialNetworkInfoDeleteController);

    CompanySocialNetworkInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanySocialNetworkInfo'];

    function CompanySocialNetworkInfoDeleteController($uibModalInstance, entity, CompanySocialNetworkInfo) {
        var vm = this;

        vm.companySocialNetworkInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanySocialNetworkInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
