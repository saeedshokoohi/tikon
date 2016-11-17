(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('TimePeriodDetailController', TimePeriodDetailController);

    TimePeriodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TimePeriod', 'OffTime', 'WeeklyScheduleInfo'];

    function TimePeriodDetailController($scope, $rootScope, $stateParams, entity, TimePeriod, OffTime, WeeklyScheduleInfo) {
        var vm = this;

        vm.timePeriod = entity;

        var unsubscribe = $rootScope.$on('tikonApp:timePeriodUpdate', function(event, result) {
            vm.timePeriod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
