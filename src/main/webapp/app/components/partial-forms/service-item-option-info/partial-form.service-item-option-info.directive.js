(function() {
    'use strict';

    function ServiceItemOptionInfoController ($scope, $state, ParseLinks, AlertService, paginationConstants,translate, translatePartialLoader,ServiceOptionItemCustom,ServiceOptionItem) {
        debugger;
        var $ctrl = this;
        var pagingParams={};
        $ctrl.loadPage = loadPage;
        $ctrl.selectedId='';
        $ctrl.formState='list';
        $ctrl.newItem=newItem;
        $ctrl.editItem=editItem;
        $ctrl.backToList=backToList;
        $ctrl.serviceOptionItems =null;


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
            translatePartialLoader.addPart('serviceOptionInfo');
            translatePartialLoader.addPart('serviceOptionItem');
            translatePartialLoader.addPart('priceInfoDtail');
            translatePartialLoader.addPart('global');
            translatePartialLoader=translate.refresh();
            debugger;
           if($ctrl.serviceItemId==undefined || $ctrl.serviceItemId==null)$ctrl.serviceOptionItems=[];
            else
              $ctrl.serviceOptionItems =  ServiceOptionItemCustom.ServiceOptionItemsByServiceItem($ctrl.serviceItemId).query();
        }
        function newItem()
        {
            debugger;
            $ctrl.formState='new';
            $ctrl.serviceOptionItem = null;
            $ctrl.serviceOptionItem.optionInfo.title = 'serviceOption';
            $ctrl.onCreateNewPriceInfo();

        }
        function editItem(id)
        {
            debugger;
            $ctrl.formState='edit';
            //$ctrl.selectedId=id;
            //$ctrl.onSelectedPriceInfoChanged(id);

            $ctrl.serviceOptionItem =  ServiceOptionItem.get(id);

            debugger;

        }
        function backToList()
        {
            $ctrl.formState='list';
            if($ctrl.serviceItemId==undefined || $ctrl.serviceItemId==null)$ctrl.serviceOptionItems=[];
            else
              $ctrl.serviceOptionItems =  ServiceOptionItemCustom.ServiceOptionItemsByServiceItem($ctrl.serviceItemId).query();

        }

    }

    var ServiceItemOptionInfo = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/service-item-option-info/partial-form.service-item-option-info.html',
        controller:ServiceItemOptionInfoController,
        bindings:
        {
            serviceItemId:'<',
            serviceOptionItem:'=',
            selectedId:'=?',
            formState:'=',
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
        .component('pfServiceItemOptionInfo', ServiceItemOptionInfo)

    ServiceItemOptionInfoController.$inject = ['$scope', '$state',  'ParseLinks', 'AlertService', 'paginationConstants','$translate', '$translatePartialLoader','ServiceOptionItemCustom','ServiceOptionItem'];



})();

