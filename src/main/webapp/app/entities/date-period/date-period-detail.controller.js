(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('DatePeriodDetailController', DatePeriodDetailController);

    DatePeriodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DatePeriod', 'OffDay', 'WeeklyScheduleInfo'];

    function DatePeriodDetailController($scope, $rootScope, $stateParams, entity, DatePeriod, OffDay, WeeklyScheduleInfo) {
        var vm = this;

        vm.datePeriod = entity;

        var unsubscribe = $rootScope.$on('tikonApp:datePeriodUpdate', function(event, result) {
            vm.datePeriod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
