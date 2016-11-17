(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceTimeSessionDeleteController',ServiceTimeSessionDeleteController);

    ServiceTimeSessionDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceTimeSession'];

    function ServiceTimeSessionDeleteController($uibModalInstance, entity, ServiceTimeSession) {
        var vm = this;

        vm.serviceTimeSession = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceTimeSession.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
