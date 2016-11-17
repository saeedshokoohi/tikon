(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceTimeSessionInfoDeleteController',ServiceTimeSessionInfoDeleteController);

    ServiceTimeSessionInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceTimeSessionInfo'];

    function ServiceTimeSessionInfoDeleteController($uibModalInstance, entity, ServiceTimeSessionInfo) {
        var vm = this;

        vm.serviceTimeSessionInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceTimeSessionInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
