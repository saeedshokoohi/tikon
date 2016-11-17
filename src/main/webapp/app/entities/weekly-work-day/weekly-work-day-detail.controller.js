(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WeeklyWorkDayDetailController', WeeklyWorkDayDetailController);

    WeeklyWorkDayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WeeklyWorkDay', 'WeeklyScheduleInfo'];

    function WeeklyWorkDayDetailController($scope, $rootScope, $stateParams, entity, WeeklyWorkDay, WeeklyScheduleInfo) {
        var vm = this;

        vm.weeklyWorkDay = entity;

        var unsubscribe = $rootScope.$on('tikonApp:weeklyWorkDayUpdate', function(event, result) {
            vm.weeklyWorkDay = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
