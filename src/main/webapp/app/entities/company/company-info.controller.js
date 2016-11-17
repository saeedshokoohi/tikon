(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyInfoController', CompanyInfoController);

    CompanyInfoController.$inject = ['$timeout', '$scope', '$stateParams', 'Company', 'SettingInfo', 'AgreementInfo', 'LocationInfo','MetaTag','CompanyInfoQuery'];

    function CompanyInfoController ($timeout, $scope, $stateParams,  Company, SettingInfo, AgreementInfo, LocationInfo, MetaTag,CompanyInfoQuery) {
        var vm = this;
        debugger;
        //vm.company = entity;
        vm.company = CompanyInfoQuery.getCurrentCompanyInfo().query() ;
        vm.clear = clear;
        vm.save = save;
        //vm.settinginfos = SettingInfo.query();
        //vm.agreementinfos = AgreementInfo.query();
        //vm.locationinfos = LocationInfo.query();
        vm.metatags = MetaTag.query();
        vm.metatagOptions = {
            placeholder: "--",
            dataTextField: "tag",
            //dataValueField: "id",
            valuePrimitive: true,
            autoBind: false,
            dataSource:vm.metatags
        };

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
            }
            else {
                //Company.save(vm.company, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:companyUpdate', result);

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
