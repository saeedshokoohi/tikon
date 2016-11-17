(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ImageDataDialogController', ImageDataDialogController);

    ImageDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ImageData', 'AlbumInfo'];

    function ImageDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ImageData, AlbumInfo) {
        var vm = this;

        vm.imageData = entity;
        vm.file1=entity.imageData
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.imagesrc=DataUtils.imagesrc;
        vm.save = save;
        vm.albuminfos = AlbumInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.imageData.id !== null) {
                ImageData.update(vm.imageData, onSaveSuccess, onSaveError);
            } else {
                ImageData.save(vm.imageData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tikonApp:imageDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFileData = function ($file, imageData) {
            debugger;
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        imageData.fileData = base64Data;
                        imageData.fileDataContentType = $file.type;

               //        vm.setThumbnailData(imageData.fileData,imageData.fileDataContentType);
                    });
                });
            }
        };
        vm.setThumbnailData=function(fileData,fileType)
        {
            $scope.$apply(function() {
                imageData.thumbnailData = fileData;
                imageData.thumbnailDataContentType = fileType;
            });


        }

        vm.setThumbnailData = function ($file, imageData) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        imageData.thumbnailData = base64Data;
                        imageData.thumbnailDataContentType = $file.type;
                    });
                });
            }
        };

    }
})();
