(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('DiscountInfoDetailController', DiscountInfoDetailController);

    DiscountInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DiscountInfo'];

    function DiscountInfoDetailController($scope, $rootScope, $stateParams, entity, DiscountInfo) {
        var vm = this;

        vm.discountInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:discountInfoUpdate', function(event, result) {
            vm.discountInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
