(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceTimeSessionInfoDetailController', ServiceTimeSessionInfoDetailController);

    ServiceTimeSessionInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ServiceTimeSessionInfo', 'ServiceItem', 'ScheduleInfo', 'ServiceTimeSession'];

    function ServiceTimeSessionInfoDetailController($scope, $rootScope, $stateParams, entity, ServiceTimeSessionInfo, ServiceItem, ScheduleInfo, ServiceTimeSession) {
        var vm = this;

        vm.serviceTimeSessionInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:serviceTimeSessionInfoUpdate', function(event, result) {
            vm.serviceTimeSessionInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
