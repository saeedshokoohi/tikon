(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SelectorDataDetailController', SelectorDataDetailController);

    SelectorDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SelectorData', 'SelectorDataType'];

    function SelectorDataDetailController($scope, $rootScope, $stateParams, entity, SelectorData, SelectorDataType) {
        var vm = this;

        vm.selectorData = entity;

        var unsubscribe = $rootScope.$on('tikonApp:selectorDataUpdate', function(event, result) {
            vm.selectorData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
