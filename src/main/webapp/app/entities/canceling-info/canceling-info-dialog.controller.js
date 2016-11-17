(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CancelingInfoDialogController', CancelingInfoDialogController);

    CancelingInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CancelingInfo'];

    function CancelingInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CancelingInfo) {
        var vm = this;

        vm.cancelingInfo = entity;
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
            if (vm.cancelingInfo.id !== null) {
                CancelingInfo.update(vm.cancelingInfo, onSaveSuccess, onSaveError);
            } else {
                CancelingInfo.save(vm.cancelingInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:cancelingInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
