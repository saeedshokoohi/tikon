(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CancelingInfoDetailController', CancelingInfoDetailController);

    CancelingInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CancelingInfo'];

    function CancelingInfoDetailController($scope, $rootScope, $stateParams, entity, CancelingInfo) {
        var vm = this;

        vm.cancelingInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:cancelingInfoUpdate', function(event, result) {
            vm.cancelingInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
