(function() {
    'use strict';

    function ServiceItemPriceInfoController ($scope, $state, PriceInfoCustom, PriceInfoSearch, ParseLinks, AlertService, paginationConstants,translate, translatePartialLoader,PriceInfoDtailCustom,PriceInfoDtail) {
        debugger;
        var $ctrl = this;
        var pagingParams={};
        $ctrl.loadPage = loadPage;
        $ctrl.selectedId='';
        $ctrl.formState='list';
        $ctrl.newItem=newItem;
        $ctrl.editItem=editItem;
        $ctrl.backToList=backToList;
        $ctrl.priceInfoDtails =null;

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
            translatePartialLoader.addPart('priceInfo');
            translatePartialLoader.addPart('priceInfoDtail');
            translatePartialLoader.addPart('global');
            translatePartialLoader=translate.refresh();
            debugger;
           if($ctrl.serviceItemId==undefined || $ctrl.serviceItemId==null)$ctrl.priceInfos=[];
            else
              $ctrl.priceInfoDtails =  PriceInfoDtailCustom.PriceInfoDtailsByServiceItem($ctrl.serviceItemId).query();
        }
        function newItem()
        {
            debugger;
            $ctrl.formState='new';
            $ctrl.priceInfoDtail = null;
            $ctrl.priceInfoDtail.capacityRatio =1;  //
            $ctrl.priceInfoDtail.title ='قیمت سرویس';  //
            $ctrl.onCreateNewPriceInfo();

        }
        function editItem(id)
        {
            debugger;
            $ctrl.formState='edit';
            //$ctrl.selectedId=id;
            //$ctrl.onSelectedPriceInfoChanged(id);

            $ctrl.priceInfoDtail =  PriceInfoDtail.get(id);

            debugger;

        }
        function backToList()
        {
            $ctrl.formState='list';
            if($ctrl.serviceItemId==undefined || $ctrl.serviceItemId==null)$ctrl.priceInfos=[];
            else
                //$ctrl.priceInfos =  PriceInfoCustom.PriceInfosByServiceItem($ctrl.serviceItemId).query();
                $ctrl.priceInfoDtails =  PriceInfoDtailCustom.PriceInfoDtailsByServiceItem($ctrl.serviceItemId).query();

        }





    }

    var ServiceItemPriceInfo = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/service-item-price-info/partial-form.service-item-price-info.html',
        controller:ServiceItemPriceInfoController,
        bindings:
        {
            serviceItemId:'<',
            priceInfoDtail:'=',
            selectedId:'=?',
            formState:'=',
            onSelectedPriceInfoChanged:'=',
            onCreateNewPriceInfo:'='


          /*  items :'<',
            displaytag:'@?',
            displayitem:'@?',
            list:'='*/

        }

    };

    angular
        .module('tikonApp')
        .component('pfServiceItemPriceInfo', ServiceItemPriceInfo)

    ServiceItemPriceInfoController.$inject = ['$scope', '$state', 'PriceInfoCustom', 'PriceInfoSearch', 'ParseLinks', 'AlertService', 'paginationConstants','$translate', '$translatePartialLoader','PriceInfoDtailCustom','PriceInfoDtail'];



})();

