(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SocialNetworkInfoDetailController', SocialNetworkInfoDetailController);

    SocialNetworkInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SocialNetworkInfo', 'PersonInfo'];

    function SocialNetworkInfoDetailController($scope, $rootScope, $stateParams, entity, SocialNetworkInfo, PersonInfo) {
        var vm = this;

        vm.socialNetworkInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:socialNetworkInfoUpdate', function(event, result) {
            vm.socialNetworkInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
