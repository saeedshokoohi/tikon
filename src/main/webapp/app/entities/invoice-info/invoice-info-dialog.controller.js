(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('InvoiceInfoDialogController', InvoiceInfoDialogController);

    InvoiceInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InvoiceInfo', 'OrderBag', 'PaymentLog', 'Customer'];

    function InvoiceInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InvoiceInfo, OrderBag, PaymentLog, Customer) {
        var vm = this;

        vm.invoiceInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.orderbags = OrderBag.query();
        vm.paymentlogs = PaymentLog.query();
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invoiceInfo.id !== null) {
                InvoiceInfo.update(vm.invoiceInfo, onSaveSuccess, onSaveError);
            } else {
                InvoiceInfo.save(vm.invoiceInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:invoiceInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
