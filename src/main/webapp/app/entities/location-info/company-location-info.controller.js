(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyLocationInfoController', CompanyLocationInfoController);

    CompanyLocationInfoController.$inject = ['$timeout', '$scope', '$stateParams', 'LocationInfo', 'SelectorData','SelectorDataQuery','CompanyInfoQuery'];

    function CompanyLocationInfoController ($timeout, $scope, $stateParams,   LocationInfo, SelectorData,SelectorDataQuery,CompanyInfoQuery) {
        var vm = this;
        //vm.locationInfo = entity;
    debugger;
        vm.companyInfo = CompanyInfoQuery.getCurrentCompanyInfo().query();
        vm.clear = clear;
        vm.save = save;
        vm.province_changed=province_changed;
        vm.country_changed=country_changed;
        vm.selectordata = SelectorData.query();
        vm.countryList=SelectorDataQuery.findByType('country').query();

        //vm.provinceList=SelectorDataQuery.findByTypeAndParent('province',vm.companyInfo.locationInfo.countryId).query();
        //vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.companyInfo.locationInfo.stateId).query();

        vm.provinceList=SelectorDataQuery.findByType('province').query();
        vm.cityList=SelectorDataQuery.findByType('city').query();


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        function country_changed()
        {
            if(vm.companyInfo.locationInfo.countryId!=null)
            vm.provinceList=SelectorDataQuery.findByTypeAndParent('province',vm.companyInfo.locationInfo.countryId).query();
            if(vm.companyInfo.locationInfo.stateId!=null)
            vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.companyInfo.locationInfo.stateId).query();
        }
        function province_changed()
        {
           vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.companyInfo.locationInfo.stateId).query();
        }
        function save () {
            vm.isSaving = true;
            if (vm.companyInfo.locationInfo.id !== null) {
                LocationInfo.update(vm.companyInfo.locationInfo, onSaveSuccess, onSaveError);
            } else {
                LocationInfo.save(vm.companyInfo.locationInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:locationInfoUpdate', result);

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
