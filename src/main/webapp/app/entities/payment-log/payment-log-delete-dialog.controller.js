(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PaymentLogDeleteController',PaymentLogDeleteController);

    PaymentLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentLog'];

    function PaymentLogDeleteController($uibModalInstance, entity, PaymentLog) {
        var vm = this;

        vm.paymentLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
