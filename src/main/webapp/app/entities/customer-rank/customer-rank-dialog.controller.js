(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerRankDialogController', CustomerRankDialogController);

    CustomerRankDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerRank', 'Customer', 'ServiceItem'];

    function CustomerRankDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerRank, Customer, ServiceItem) {
        var vm = this;

        vm.customerRank = entity;
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
            if (vm.customerRank.id !== null) {
                CustomerRank.update(vm.customerRank, onSaveSuccess, onSaveError);
            } else {
                CustomerRank.save(vm.customerRank, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:customerRankUpdate', result);
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
