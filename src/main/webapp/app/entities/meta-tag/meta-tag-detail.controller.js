(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('MetaTagDetailController', MetaTagDetailController);

    MetaTagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MetaTag', 'Company', 'ServiceItem'];

    function MetaTagDetailController($scope, $rootScope, $stateParams, entity, MetaTag, Company, ServiceItem) {
        var vm = this;

        vm.metaTag = entity;

        var unsubscribe = $rootScope.$on('tikonApp:metaTagUpdate', function(event, result) {
            vm.metaTag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
