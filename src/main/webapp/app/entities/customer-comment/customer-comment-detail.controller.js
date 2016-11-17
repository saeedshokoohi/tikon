(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CustomerCommentDetailController', CustomerCommentDetailController);

    CustomerCommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CustomerComment', 'Customer', 'ServiceItem'];

    function CustomerCommentDetailController($scope, $rootScope, $stateParams, entity, CustomerComment, Customer, ServiceItem) {
        var vm = this;

        vm.customerComment = entity;

        var unsubscribe = $rootScope.$on('tikonApp:customerCommentUpdate', function(event, result) {
            vm.customerComment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
