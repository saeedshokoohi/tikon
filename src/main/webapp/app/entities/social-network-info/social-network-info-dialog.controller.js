(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SocialNetworkInfoDialogController', SocialNetworkInfoDialogController);

    SocialNetworkInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SocialNetworkInfo', 'PersonInfo'];

    function SocialNetworkInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SocialNetworkInfo, PersonInfo) {
        var vm = this;

        vm.socialNetworkInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.personinfos = PersonInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.socialNetworkInfo.id !== null) {
                SocialNetworkInfo.update(vm.socialNetworkInfo, onSaveSuccess, onSaveError);
            } else {
                SocialNetworkInfo.save(vm.socialNetworkInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:socialNetworkInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
