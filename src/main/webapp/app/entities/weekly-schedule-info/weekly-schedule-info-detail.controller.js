(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WeeklyScheduleInfoDetailController', WeeklyScheduleInfoDetailController);

    WeeklyScheduleInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WeeklyScheduleInfo', 'DatePeriod', 'TimePeriod', 'WeeklyWorkDay'];

    function WeeklyScheduleInfoDetailController($scope, $rootScope, $stateParams, entity, WeeklyScheduleInfo, DatePeriod, TimePeriod, WeeklyWorkDay) {
        var vm = this;

        vm.weeklyScheduleInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:weeklyScheduleInfoUpdate', function(event, result) {
            vm.weeklyScheduleInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
