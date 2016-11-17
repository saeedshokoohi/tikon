(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('DiscountInfoDialogController', DiscountInfoDialogController);

    DiscountInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiscountInfo'];

    function DiscountInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DiscountInfo) {
        var vm = this;

        vm.discountInfo = entity;
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
            if (vm.discountInfo.id !== null) {
                DiscountInfo.update(vm.discountInfo, onSaveSuccess, onSaveError);
            } else {
                DiscountInfo.save(vm.discountInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:discountInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
