(function() {
    'use strict';

    function SettingInfoComponentController ($scope, $state,  SettingInfoSearch, ParseLinks, AlertService, paginationConstants,translate, translatePartialLoader) {
        debugger;
        var $ctrl = this;
        var pagingParams={};
        $ctrl.loadPage = loadPage;
        $ctrl.selectedId='';

        loadAll();

        loadPage();

        function loadAll () {


            function onSuccess(data, headers) {

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            translatePartialLoader.addPart('settingInfo');
            translatePartialLoader.addPart('notificationSetting');
            translatePartialLoader.addPart('financialSetting');
            translatePartialLoader.addPart('themeSettingInfo');
            translatePartialLoader.addPart('notificationType');
            translatePartialLoader.addPart('currency');

            translatePartialLoader.addPart('global');
            translatePartialLoader=translate.refresh();
            debugger;

            //$ctrl.SettingInfoDtails =  SettingInfoDtailCustom.SettingInfoDtailsByServiceItem($ctrl.serviceItemId).query();
        }

        function country_changed()
        {
            //debugger;
            //if(vm.SettingInfo.locationInfo.countryId!=null)
            //    vm.provinceList=SelectorDataQuery.findByTypeAndParent('province',vm.SettingInfo.locationInfo.countryId).query();
            //if(vm.SettingInfo.locationInfo.stateId!=null)
            //    vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.SettingInfo.locationInfo.stateId).query();
        }

        function province_changed()
        {
            //vm.cityList=SelectorDataQuery.findByTypeAndParent('city',vm.locationInfo.stateId).query();
        }

    }

    var SettingInfoComponent = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/setting-info/partial-form.setting-info.html',
        controller:SettingInfoComponentController,
        bindings:
        {
            settingInfo:'=',
        }

    };

    angular
        .module('tikonApp')
            .component('pfSettingInfo', SettingInfoComponent)

    SettingInfoComponentController.$inject = ['$scope', '$state',  'SettingInfoSearch', 'ParseLinks', 'AlertService', 'paginationConstants','$translate', '$translatePartialLoader'];



})();

