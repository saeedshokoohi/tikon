(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company', 'SettingInfo', 'AgreementInfo', 'LocationInfo', 'MetaTag'];

    function CompanyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Company, SettingInfo, AgreementInfo, LocationInfo, MetaTag) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.save = save;
        vm.settinginfos = SettingInfo.query();
        vm.agreementinfos = AgreementInfo.query();
        vm.locationinfos = LocationInfo.query();
        vm.metatags = MetaTag.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onSaveSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:companyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
