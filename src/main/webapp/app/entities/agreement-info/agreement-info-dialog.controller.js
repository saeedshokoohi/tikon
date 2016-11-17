(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('AgreementInfoDialogController', AgreementInfoDialogController);

    AgreementInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AgreementInfo'];

    function AgreementInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AgreementInfo) {
        var vm = this;

        vm.agreementInfo = entity;
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
            if (vm.agreementInfo.id !== null) {
                AgreementInfo.update(vm.agreementInfo, onSaveSuccess, onSaveError);
            } else {
                AgreementInfo.save(vm.agreementInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:agreementInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
