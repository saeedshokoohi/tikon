(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ScheduleBaseDiscountDetailController', ScheduleBaseDiscountDetailController);

    ScheduleBaseDiscountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ScheduleBaseDiscount', 'ScheduleInfo', 'DiscountInfo'];

    function ScheduleBaseDiscountDetailController($scope, $rootScope, $stateParams, entity, ScheduleBaseDiscount, ScheduleInfo, DiscountInfo) {
        var vm = this;

        vm.scheduleBaseDiscount = entity;

        var unsubscribe = $rootScope.$on('tikonApp:scheduleBaseDiscountUpdate', function(event, result) {
            vm.scheduleBaseDiscount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
