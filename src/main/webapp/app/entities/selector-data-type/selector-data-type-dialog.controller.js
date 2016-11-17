(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SelectorDataTypeDialogController', SelectorDataTypeDialogController);

    SelectorDataTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SelectorDataType'];

    function SelectorDataTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SelectorDataType) {
        var vm = this;

        vm.selectorDataType = entity;
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
            if (vm.selectorDataType.id !== null) {
                SelectorDataType.update(vm.selectorDataType, onSaveSuccess, onSaveError);
            } else {
                SelectorDataType.save(vm.selectorDataType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:selectorDataTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
