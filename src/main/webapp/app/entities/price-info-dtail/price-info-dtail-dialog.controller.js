(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PriceInfoDtailDialogController', PriceInfoDtailDialogController);

    PriceInfoDtailDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PriceInfoDtail'];

    function PriceInfoDtailDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PriceInfoDtail) {
        var vm = this;

        vm.priceInfoDtail = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.priceInfoDtail.id !== null) {
                PriceInfoDtail.update(vm.priceInfoDtail, onSaveSuccess, onSaveError);
            } else {
                PriceInfoDtail.save(vm.priceInfoDtail, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:priceInfoDtailUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
