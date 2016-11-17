(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ParticipantPersonDetailController', ParticipantPersonDetailController);

    ParticipantPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ParticipantPerson', 'PersonInfo', 'OrderBagServiceItemDtail'];

    function ParticipantPersonDetailController($scope, $rootScope, $stateParams, entity, ParticipantPerson, PersonInfo, OrderBagServiceItemDtail) {
        var vm = this;

        vm.participantPerson = entity;

        var unsubscribe = $rootScope.$on('tikonApp:participantPersonUpdate', function(event, result) {
            vm.participantPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
