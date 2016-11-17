(function() {
    'use strict';

    function ServiceItemAgreementInfoController ($scope, $state, AgreementInfoSearch, ParseLinks, AlertService, paginationConstants,translate, translatePartialLoader) {
        debugger;
        var $ctrl = this;
        var pagingParams={};
        $ctrl.loadPage = loadPage;

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
            translatePartialLoader.addPart('agreementInfo');
            translatePartialLoader.addPart('global');
            translatePartialLoader=translate.refresh();
            debugger;
           //if($ctrl.serviceItemId==undefined || $ctrl.serviceItemId==null);
           // else
           //   $ctrl.agreementInfo =  AgreementInfoDtailCustom.AgreementInfoDtailsByServiceItem($ctrl.serviceItemId).query();
        }


    }

    var ServiceItemAgreementInfo = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/service-item-agreement-info/partial-form.service-item-agreement-info.html',
        controller:ServiceItemAgreementInfoController,
        bindings:
        {
            agreement:'=',
        }

    };

    angular
        .module('tikonApp')
        .component('pfServiceItemAgreementInfo', ServiceItemAgreementInfo)

    ServiceItemAgreementInfoController.$inject = ['$scope', '$state',  'AgreementInfoSearch', 'ParseLinks', 'AlertService', 'paginationConstants','$translate', '$translatePartialLoader'];



})();

