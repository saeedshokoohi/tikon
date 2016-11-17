(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ImageDataDetailController', ImageDataDetailController);

    ImageDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'ImageData', 'AlbumInfo'];

    function ImageDataDetailController($scope, $rootScope, $stateParams, DataUtils, entity, ImageData, AlbumInfo) {
        var vm = this;

        vm.imageData = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('tikonApp:imageDataUpdate', function(event, result) {
            vm.imageData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
