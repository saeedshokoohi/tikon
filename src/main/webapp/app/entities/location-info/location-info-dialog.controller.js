(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('LocationInfoDialogController', LocationInfoDialogController);

    LocationInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LocationInfo', 'SelectorData'];

    function LocationInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LocationInfo, SelectorData) {
        var vm = this;

        vm.locationInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.selectordata = SelectorData.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.locationInfo.id !== null) {
                LocationInfo.update(vm.locationInfo, onSaveSuccess, onSaveError);
            } else {
                LocationInfo.save(vm.locationInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:locationInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
