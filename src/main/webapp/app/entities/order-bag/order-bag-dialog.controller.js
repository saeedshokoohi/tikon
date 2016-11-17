(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagDialogController', OrderBagDialogController);

    OrderBagDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderBag'];

    function OrderBagDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderBag) {
        var vm = this;

        vm.orderBag = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderBag.id !== null) {
                OrderBag.update(vm.orderBag, onSaveSuccess, onSaveError);
            } else {
                OrderBag.save(vm.orderBag, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:orderBagUpdate', result);
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
