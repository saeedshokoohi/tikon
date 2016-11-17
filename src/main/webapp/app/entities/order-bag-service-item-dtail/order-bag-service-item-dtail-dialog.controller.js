(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagServiceItemDtailDialogController', OrderBagServiceItemDtailDialogController);

    OrderBagServiceItemDtailDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderBagServiceItemDtail', 'OrderBagServiceItem', 'PriceInfoDtail'];

    function OrderBagServiceItemDtailDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderBagServiceItemDtail, OrderBagServiceItem, PriceInfoDtail) {
        var vm = this;

        vm.orderBagServiceItemDtail = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.orderbagserviceitems = OrderBagServiceItem.query();
        vm.priceinfodtails = PriceInfoDtail.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderBagServiceItemDtail.id !== null) {
                OrderBagServiceItemDtail.update(vm.orderBagServiceItemDtail, onSaveSuccess, onSaveError);
            } else {
                OrderBagServiceItemDtail.save(vm.orderBagServiceItemDtail, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:orderBagServiceItemDtailUpdate', result);
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
