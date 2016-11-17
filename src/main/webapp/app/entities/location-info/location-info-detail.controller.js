(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('LocationInfoDetailController', LocationInfoDetailController);

    LocationInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LocationInfo', 'SelectorData'];

    function LocationInfoDetailController($scope, $rootScope, $stateParams, entity, LocationInfo, SelectorData) {
        var vm = this;

        vm.locationInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:locationInfoUpdate', function(event, result) {
            vm.locationInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
