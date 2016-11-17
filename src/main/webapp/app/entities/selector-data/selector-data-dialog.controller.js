(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SelectorDataDialogController', SelectorDataDialogController);

    SelectorDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SelectorData', 'SelectorDataType'];

    function SelectorDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SelectorData, SelectorDataType) {
        var vm = this;

        vm.selectorData = entity;
        vm.clear = clear;
        vm.save = save;
        vm.selectordatatypes = SelectorDataType.query();
        vm.selectordata = SelectorData.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.selectorData.id !== null) {
                SelectorData.update(vm.selectorData, onSaveSuccess, onSaveError);
            } else {
                SelectorData.save(vm.selectorData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:selectorDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
