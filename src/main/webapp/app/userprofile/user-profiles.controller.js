(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('UserProfileController', UserProfileController);

    UserProfileController.$inject = [ 'PersonInfo' ,'UserProfileQuery' ,'LocationInfo' ,'$translate','$scope','SelectorData','SelectorDataQuery'];

    function UserProfileController ( PersonInfo,UserProfileQuery,LocationInfo,  $translate,$scope,SelectorData,SelectorDataQuery) {
        debugger;
        var vm = this;
        vm.accid   = null;
        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;

        vm.personInfo = UserProfileQuery.getCurrentPersonInfo().query() ;
        vm.success = true;


        vm.country_changed=country_changed;
        vm.province_changed= province_changed;
        vm.selectordata = SelectorData.query();
        vm.countryList=SelectorDataQuery.findByType('country').query();

        vm.provinceList=SelectorDataQuery.findByType('province').query();
        //vm.provinceList=SelectorDataQuery.findByTypeAndParent('province',vm.personInfo.locationInfo.countryId).query();
        //vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.personInfo.locationInfo.stateId).query();

        vm.cityList=SelectorDataQuery.findByType('city').query();
        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */

        function country_changed()
        {
            //debugger;
            //if(vm.personInfo.locationInfo.countryId!=null)
            //    vm.provinceList=SelectorDataQuery.findByTypeAndParent('province',vm.personInfo.locationInfo.countryId).query();
            //if(vm.personInfo.locationInfo.stateId!=null)
            //    vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.personInfo.locationInfo.stateId).query();
        }

        function province_changed()
        {
            //vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.locationInfo.stateId).query();
        }
        function save () {
            debugger;
            vm.isSaving = true;

            if (vm.personInfo.id !== null) {
                PersonInfo.update(vm.personInfo, onSaveSuccess, onSaveError);
            }
            else {
                // ServiceItem.save(vm.serviceItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            debugger;
            $scope.$emit('tikonApp:userProfileUpdate', result);
            vm.personinfo=result.id;
            //console.log(result.id);
            //console.log( JSON.stringify(vm.serviceItem));
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }



    }
})();
