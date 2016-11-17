(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SelectorDataTypeDetailController', SelectorDataTypeDetailController);

    SelectorDataTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'SelectorDataType'];

    function SelectorDataTypeDetailController($scope, $rootScope, $stateParams, entity, SelectorDataType) {
        var vm = this;

        vm.selectorDataType = entity;

        var unsubscribe = $rootScope.$on('tikonApp:selectorDataTypeUpdate', function(event, result) {
            vm.selectorDataType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
