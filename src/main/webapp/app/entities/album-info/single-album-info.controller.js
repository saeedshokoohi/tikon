(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SingleAlbumInfoController', SingleAlbumInfoController);

    SingleAlbumInfoController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'AlbumInfo'];

    function SingleAlbumInfoController ($timeout, $scope, $stateParams, entity, AlbumInfo) {
        var vm = this;

        vm.albumInfo = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

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
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
