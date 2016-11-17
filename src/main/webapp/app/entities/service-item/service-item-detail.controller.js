(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceItemDetailController', ServiceItemDetailController);

    ServiceItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceItem', 'ServiceOptionInfo', 'DiscountInfo', 'LocationInfo', 'AlbumInfo', 'ServiceCapacityInfo', 'ServiceCategory', 'PriceInfo', 'ScheduleInfo', 'Servant', 'AgreementInfo', 'MetaTag'];

    function ServiceItemDetailController($scope, $rootScope, $stateParams, entity, ServiceItem, ServiceOptionInfo, DiscountInfo, LocationInfo, AlbumInfo, ServiceCapacityInfo, ServiceCategory, PriceInfo, ScheduleInfo, Servant, AgreementInfo, MetaTag) {
        var vm = this;

        vm.serviceItem = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceItemUpdate', function(event, result) {
            vm.serviceItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
