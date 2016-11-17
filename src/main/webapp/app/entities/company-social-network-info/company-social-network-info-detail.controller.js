(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanySocialNetworkInfoDetailController', CompanySocialNetworkInfoDetailController);

    CompanySocialNetworkInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CompanySocialNetworkInfo', 'Company', 'SocialNetworkInfo'];

    function CompanySocialNetworkInfoDetailController($scope, $rootScope, $stateParams, entity, CompanySocialNetworkInfo, Company, SocialNetworkInfo) {
        var vm = this;

        vm.companySocialNetworkInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:companySocialNetworkInfoUpdate', function(event, result) {
            vm.companySocialNetworkInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
