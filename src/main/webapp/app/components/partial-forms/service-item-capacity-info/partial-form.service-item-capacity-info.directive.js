(function() {
    'use strict';

    function ServiceCapacityInfoCustomController ($scope, $state, ParseLinks, AlertService, paginationConstants,translate, translatePartialLoader,ServiceCapacityInfoCustom,ServiceCapacityInfo) {
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
            translatePartialLoader.addPart('serviceCapacityInfo');
            translatePartialLoader.addPart('global');
            translatePartialLoader=translate.refresh();
            debugger;
           if($ctrl.serviceItemId==undefined || $ctrl.serviceItemId==null);
            else
              $ctrl.serviceCapacityInfo =  serviceCapacityInfoCustom.serviceCapacityInfoByServiceItem($ctrl.serviceItemId).query();
        }


    }

    var ServiceCapacityInfo = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/service-capacity-info/partial-form.service-capacity-info.html',
        controller:ServiceCapacityInfoCustomController,
        bindings:
        {
            serviceItemId:'<',
            serviceCapacityInfo:'=',
            selectedId:'=?',
            onSelectedServiceOptionInfoChanged:'=',
            onCreateNewServiceOptionInfo:'='


          /*  items :'<',
            displaytag:'@?',
            displayitem:'@?',
            list:'='*/

        }

    };

    angular
        .module('tikonApp')
        .component('pfServiceCapacityInfo', ServiceCapacityInfo)

    ServiceCapacityInfoCustomController.$inject = ['$scope', '$state',  'ParseLinks', 'AlertService', 'paginationConstants','$translate', '$translatePartialLoader','ServiceCapacityInfoCustom','ServiceCapacityInfo'];



})();

