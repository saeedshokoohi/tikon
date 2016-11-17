(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('AlbumInfoDialogController', AlbumInfoDialogController);

    AlbumInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AlbumInfo'];

    function AlbumInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AlbumInfo) {
        var vm = this;

        vm.albumInfo = entity;
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
            if (vm.albumInfo.id !== null) {
                AlbumInfo.update(vm.albumInfo, onSaveSuccess, onSaveError);
            } else {
                AlbumInfo.save(vm.albumInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:albumInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
