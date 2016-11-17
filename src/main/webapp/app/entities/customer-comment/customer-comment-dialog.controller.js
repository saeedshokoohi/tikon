(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerCommentDialogController', CustomerCommentDialogController);

    CustomerCommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerComment', 'Customer', 'ServiceItem'];

    function CustomerCommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerComment, Customer, ServiceItem) {
        var vm = this;

        vm.customerComment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.customers = Customer.query();
        vm.serviceitems = ServiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customerComment.id !== null) {
                CustomerComment.update(vm.customerComment, onSaveSuccess, onSaveError);
            } else {
                CustomerComment.save(vm.customerComment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:customerCommentUpdate', result);
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
