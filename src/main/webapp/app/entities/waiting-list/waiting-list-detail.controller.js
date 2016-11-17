(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('WaitingListDetailController', WaitingListDetailController);

    WaitingListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'WaitingList', 'ServiceItem'];

    function WaitingListDetailController($scope, $rootScope, $stateParams, entity, WaitingList, ServiceItem) {
        var vm = this;

        vm.waitingList = entity;

        var unsubscribe = $rootScope.$on('tikonApp:waitingListUpdate', function(event, result) {
            vm.waitingList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
