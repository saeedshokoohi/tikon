(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('OffDayDetailController', OffDayDetailController);

    OffDayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'OffDay', 'DatePeriod'];

    function OffDayDetailController($scope, $rootScope, $stateParams, entity, OffDay, DatePeriod) {
        var vm = this;

        vm.offDay = entity;

        var unsubscribe = $rootScope.$on('tikonApp:offDayUpdate', function(event, result) {
            vm.offDay = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
