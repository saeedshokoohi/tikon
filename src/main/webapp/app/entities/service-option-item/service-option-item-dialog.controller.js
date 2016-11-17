(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceOptionItemDialogController', ServiceOptionItemDialogController);

    ServiceOptionItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceOptionItem', 'ServiceOptionInfo', 'PriceInfo', 'AlbumInfo'];

    function ServiceOptionItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceOptionItem, ServiceOptionInfo, PriceInfo, AlbumInfo) {
        var vm = this;

        vm.serviceOptionItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviceoptioninfos = ServiceOptionInfo.query();
        vm.priceinfos = PriceInfo.query();
        vm.albuminfos = AlbumInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceOptionItem.id !== null) {
                ServiceOptionItem.update(vm.serviceOptionItem, onSaveSuccess, onSaveError);
            } else {
                ServiceOptionItem.save(vm.serviceOptionItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceOptionItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
