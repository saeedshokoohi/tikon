(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('AgreementInfoDetailController', AgreementInfoDetailController);

    AgreementInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'AgreementInfo'];

    function AgreementInfoDetailController($scope, $rootScope, $stateParams, entity, AgreementInfo) {
        var vm = this;

        vm.agreementInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:agreementInfoUpdate', function(event, result) {
            vm.agreementInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
