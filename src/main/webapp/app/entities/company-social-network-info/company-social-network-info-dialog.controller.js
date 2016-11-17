(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanySocialNetworkInfoDialogController', CompanySocialNetworkInfoDialogController);

    CompanySocialNetworkInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanySocialNetworkInfo', 'Company', 'SocialNetworkInfo'];

    function CompanySocialNetworkInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanySocialNetworkInfo, Company, SocialNetworkInfo) {
        var vm = this;

        vm.companySocialNetworkInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.socialnetworkinfos = SocialNetworkInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companySocialNetworkInfo.id !== null) {
                CompanySocialNetworkInfo.update(vm.companySocialNetworkInfo, onSaveSuccess, onSaveError);
            } else {
                CompanySocialNetworkInfo.save(vm.companySocialNetworkInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:companySocialNetworkInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
