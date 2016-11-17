(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('PersonInfoDetailController', PersonInfoDetailController);

    PersonInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PersonInfo', 'LocationInfo', 'SocialNetworkInfo'];

    function PersonInfoDetailController($scope, $rootScope, $stateParams, entity, PersonInfo, LocationInfo, SocialNetworkInfo) {
        var vm = this;

        vm.personInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:personInfoUpdate', function(event, result) {
            vm.personInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
