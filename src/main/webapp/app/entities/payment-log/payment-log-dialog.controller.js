(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PaymentLogDialogController', PaymentLogDialogController);

    PaymentLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentLog', 'InvoiceInfo'];

    function PaymentLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentLog, InvoiceInfo) {
        var vm = this;

        vm.paymentLog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.invoiceinfos = InvoiceInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paymentLog.id !== null) {
                PaymentLog.update(vm.paymentLog, onSaveSuccess, onSaveError);
            } else {
                PaymentLog.save(vm.paymentLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:paymentLogUpdate', result);
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
