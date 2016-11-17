(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyAgreementInfoController', CompanyAgreementInfoController);

    CompanyAgreementInfoController.$inject = ['$timeout', '$scope', '$stateParams', 'AgreementInfo','CompanyInfoQuery'];

    function CompanyAgreementInfoController ($timeout, $scope, $stateParams, AgreementInfo,CompanyInfoQuery) {
        var vm = this;
 debugger;
        //vm.agreementInfo = entity;
        vm.companyInfo= CompanyInfoQuery.getCurrentCompanyInfo().query();
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyInfo.agreementInfo.id !== null) {
                AgreementInfo.update(vm.companyInfo.agreementInfo, onSaveSuccess, onSaveError);
            } else {
                AgreementInfo.save(vm.companyInfo.agreementInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:agreementInfoUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
