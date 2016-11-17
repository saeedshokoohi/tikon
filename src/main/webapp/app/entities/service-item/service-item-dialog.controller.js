(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceItemDialogController', ServiceItemDialogController);

    ServiceItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceItem', 'ServiceOptionInfo', 'DiscountInfo', 'LocationInfo', 'AlbumInfo', 'ServiceCapacityInfo', 'ServiceCategory', 'PriceInfo', 'ScheduleInfo', 'Servant', 'AgreementInfo', 'MetaTag'];

    function ServiceItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceItem, ServiceOptionInfo, DiscountInfo, LocationInfo, AlbumInfo, ServiceCapacityInfo, ServiceCategory, PriceInfo, ScheduleInfo, Servant, AgreementInfo, MetaTag) {
        var vm = this;

        vm.serviceItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.serviceoptioninfos = ServiceOptionInfo.query();
        vm.discountinfos = DiscountInfo.query();
        vm.locationinfos = LocationInfo.query();
        vm.albuminfos = AlbumInfo.query();
        vm.servicecapacityinfos = ServiceCapacityInfo.query();
        vm.servicecategories = ServiceCategory.query();
        vm.priceinfos = PriceInfo.query();
        vm.scheduleinfos = ScheduleInfo.query();
        vm.servants = Servant.query();
        vm.agreementinfos = AgreementInfo.query();
        vm.metatags = MetaTag.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceItem.id !== null) {
                ServiceItem.update(vm.serviceItem, onSaveSuccess, onSaveError);
            } else {
                ServiceItem.save(vm.serviceItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:serviceItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
