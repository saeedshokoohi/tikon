(function() {
    'use strict';




    angular
        .module('tikonApp')
        .controller('GeneralServiceItemController', GeneralServiceItemController);

    GeneralServiceItemController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'ServiceItem', 'ServiceOptionInfo', 'DiscountInfo', 'LocationInfo', 'AlbumInfo', 'ServiceCapacityInfo', 'ServiceCategory', 'PriceInfo', 'ScheduleInfo', 'Servant', 'AgreementInfo', 'MetaTag','ServiceCategoryCustom'];

    function GeneralServiceItemController ($timeout, $scope, $stateParams,  entity, ServiceItem, ServiceOptionInfo, DiscountInfo, LocationInfo, AlbumInfo, ServiceCapacityInfo, ServiceCategory, PriceInfo, ScheduleInfo, Servant, AgreementInfo, MetaTag,ServiceCategoryCustom) {
        var vm = this;
       vm.scheduleFormState='list';
        vm.priceInfoFormState="'list'";
        vm.serviceOptionInfoFormState="'list'";
        vm.servantFormState="'list'";
        vm.serviceItem = entity;
        //vm.clear = clear;
        vm.save = save;
        //vm.serviceoptioninfos = ServiceOptionInfo.query();
        vm.discountinfos = DiscountInfo.query();
        vm.locationinfos = LocationInfo.query();
        vm.albuminfos = AlbumInfo.query();
        vm.servicecapacityinfos = ServiceCapacityInfo.query();
        //vm.servicecategories = ServiceCategory.query();
        debugger;
        vm.servicecategories = ServiceCategoryCustom.getServiceCategoriesByCurrentCompany().query();
        vm.priceinfos = PriceInfo.query();
        vm.scheduleinfos = ScheduleInfo.query();
        vm.servants = Servant.query();
        //vm.agreementinfos = AgreementInfo.query();
        vm.metatags = MetaTag.query();
     //   vm.currentScheduleInfo= vm.serviceItem.scheduleInfo;
     //   vm.backToList=backToList;
     //   vm.onSelectedScheduleChanged=onSelectedScheduleChanged;
     //   vm.onCreateNewSchedule=onCreateNewSchedule;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        debugger;
        //function clear () {
        //    $uibModalInstance.dismiss('cancel');
        //}

        function save () {
            vm.isSaving = true;
            debugger;
            if (vm.serviceItem.id !== null) {
                ServiceItem.update(vm.serviceItem, onSaveSuccess, onSaveError);
            } else {
                ServiceItem.save(vm.serviceItem, onSaveSuccess, onSaveError);

            }
        }

        function onSaveSuccess (result) {
            debugger;
            $scope.$emit('tikonApp:serviceItemUpdate', result);
            vm.serviceItem=result;
            //console.log(result.id);
            //console.log( JSON.stringify(vm.serviceItem));
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        }
        function backToList()
        {

            vm.scheduleFormState='list';
            vm.selectedScheduleId='';
        }
        function  onSelectedScheduleChanged(data)
        {

            vm.serviceItem.scheduleInfo= ScheduleInfo.get({id : data.id});

        }
        function  onCreateNewSchedule()
        {

            vm.serviceItem.scheduleInfo= {};

        }

        function  onCreateNewPriceInfo()
        {
            debugger;

            vm.serviceItem.priceInfo= {};

        }
        function  onSelectedPriceInfoChanged(data)
        {
            debugger;
            //vm.serviceItem.priceInfo= PriceInfo.get({id : data.id});

        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
