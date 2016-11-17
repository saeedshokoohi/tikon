(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OrderBagServiceItemDialogController', OrderBagServiceItemDialogController);

    OrderBagServiceItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderBagServiceItem', 'OrderBag', 'ServiceItem'];

    function OrderBagServiceItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderBagServiceItem, OrderBag, ServiceItem) {
        var vm = this;

        vm.orderBagServiceItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.orderbags = OrderBag.query();
        vm.serviceitems = ServiceItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderBagServiceItem.id !== null) {
                OrderBagServiceItem.update(vm.orderBagServiceItem, onSaveSuccess, onSaveError);
            } else {
                OrderBagServiceItem.save(vm.orderBagServiceItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:orderBagServiceItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
