(function() {
    'use strict';

    function PersonInfoComponentController ($scope, $state,  PersonInfoSearch, ParseLinks, AlertService, paginationConstants,translate, translatePartialLoader,SelectorData,SelectorDataQuery) {
        debugger;
        var $ctrl = this;
        var pagingParams={};
        $ctrl.loadPage = loadPage;
        $ctrl.selectedId='';
        $ctrl.country_changed=country_changed;
        $ctrl.province_changed= province_changed;
        $ctrl.selectordata = SelectorData.query();
        $ctrl.countryList=SelectorDataQuery.findByType('country').query();

        $ctrl.provinceList=SelectorDataQuery.findByType('province').query();
        //$ctrl.provinceList=SelectorDataQuery.findByTypeAndParent('province',$ctrl.personInfo.locationInfo.countryId).query();
        //$ctrl.cityList=SelectorDataQuery.findByTypeAndParent('city',$ctrl.personInfo.locationInfo.stateId).query();

        $ctrl.cityList=SelectorDataQuery.findByType('city').query();

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
            translatePartialLoader.addPart('personInfo');
            translatePartialLoader.addPart('locationInfo');
            translatePartialLoader.addPart('global');
            translatePartialLoader=translate.refresh();
            debugger;

            //$ctrl.PersonInfoDtails =  PersonInfoDtailCustom.PersonInfoDtailsByServiceItem($ctrl.serviceItemId).query();
        }

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

    }

    var PersonInfoComponent = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/person-info/partial-form.person-info.html',
        controller:PersonInfoComponentController,
        bindings:
        {
            personInfo:'=',
        }

    };

    angular
        .module('tikonApp')
            .component('pfPersonInfo', PersonInfoComponent)

    PersonInfoComponentController.$inject = ['$scope', '$state',  'PersonInfoSearch', 'ParseLinks', 'AlertService', 'paginationConstants','$translate', '$translatePartialLoader','SelectorData','SelectorDataQuery'];



})();

