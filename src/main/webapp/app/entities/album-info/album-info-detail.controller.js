(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('AlbumInfoDetailController', AlbumInfoDetailController);

    AlbumInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'AlbumInfo'];

    function AlbumInfoDetailController($scope, $rootScope, $stateParams, entity, AlbumInfo) {
        var vm = this;

        vm.albumInfo = entity;

        var unsubscribe = $rootScope.$on('tikonApp:albumInfoUpdate', function(event, result) {
            vm.albumInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
