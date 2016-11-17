(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PriceInfoDialogController', PriceInfoDialogController);

    PriceInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PriceInfo', 'Servant'];

    function PriceInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PriceInfo, Servant) {
        var vm = this;

        vm.priceInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.servants = Servant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.priceInfo.id !== null) {
                PriceInfo.update(vm.priceInfo, onSaveSuccess, onSaveError);
            } else {
                PriceInfo.save(vm.priceInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:priceInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fromValidDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
