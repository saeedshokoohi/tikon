(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagItemOptionDialogController', OrderBagItemOptionDialogController);

    OrderBagItemOptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderBagItemOption', 'OrderBagServiceItemDtail', 'ServiceOptionItem'];

    function OrderBagItemOptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderBagItemOption, OrderBagServiceItemDtail, ServiceOptionItem) {
        var vm = this;

        vm.orderBagItemOption = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.orderbagserviceitemdtails = OrderBagServiceItemDtail.query();
        vm.serviceoptionitems = ServiceOptionItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderBagItemOption.id !== null) {
                OrderBagItemOption.update(vm.orderBagItemOption, onSaveSuccess, onSaveError);
            } else {
                OrderBagItemOption.save(vm.orderBagItemOption, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:orderBagItemOptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.reserveTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
