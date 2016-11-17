(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('InvoiceInfoDeleteController',InvoiceInfoDeleteController);

    InvoiceInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'InvoiceInfo'];

    function InvoiceInfoDeleteController($uibModalInstance, entity, InvoiceInfo) {
        var vm = this;

        vm.invoiceInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InvoiceInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
