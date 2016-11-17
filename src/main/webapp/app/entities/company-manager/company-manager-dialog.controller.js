(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyManagerDialogController', CompanyManagerDialogController);

    CompanyManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyManager', 'Company', 'PersonInfo'];

    function CompanyManagerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyManager, Company, PersonInfo) {
        var vm = this;

        vm.companyManager = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.personinfos = PersonInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyManager.id !== null) {
                CompanyManager.update(vm.companyManager, onSaveSuccess, onSaveError);
            } else {
                CompanyManager.save(vm.companyManager, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:companyManagerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
