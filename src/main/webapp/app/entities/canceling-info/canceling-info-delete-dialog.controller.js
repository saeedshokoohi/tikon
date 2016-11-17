(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CancelingInfoDeleteController',CancelingInfoDeleteController);

    CancelingInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'CancelingInfo'];

    function CancelingInfoDeleteController($uibModalInstance, entity, CancelingInfo) {
        var vm = this;

        vm.cancelingInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CancelingInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
