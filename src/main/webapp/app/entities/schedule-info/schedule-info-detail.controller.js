(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ScheduleInfoDetailController', ScheduleInfoDetailController);

    ScheduleInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ScheduleInfo', 'WeeklyScheduleInfo', 'ServiceItem'];

    function ScheduleInfoDetailController($scope, $rootScope, $stateParams, entity, ScheduleInfo, WeeklyScheduleInfo, ServiceItem) {
        var vm = this;

        vm.scheduleInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:scheduleInfoUpdate', function(event, result) {
            vm.scheduleInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
