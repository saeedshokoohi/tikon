(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OffTimeDetailController', OffTimeDetailController);

    OffTimeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OffTime', 'TimePeriod'];

    function OffTimeDetailController($scope, $rootScope, $stateParams, entity, OffTime, TimePeriod) {
        var vm = this;

        vm.offTime = entity;

        var unsubscribe = $rootScope.$on('tikonApp:offTimeUpdate', function(event, result) {
            vm.offTime = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
