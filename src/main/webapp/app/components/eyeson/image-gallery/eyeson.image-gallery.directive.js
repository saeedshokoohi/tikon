(function () {
    'use strict';
    ImageGalleryController.$inject = ['$scope', '$attrs', '$element', 'AlbumInfo', 'AlbumInfoExtend','DataUtils'];
    function ImageGalleryController($scope, $attrs, $element, AlbumInfo, AlbumInfoExtend,DataUtils) {
//debugger;
        var $ctrl = this;

        var albumInfoEntity;

        var imageList = [];
        if ($ctrl.albumId !== null) {
            debugger;
            albumInfoEntity = AlbumInfo.get({id: $ctrl.albumId}).$promise;
            imageList = AlbumInfoExtend.findAlbumImages($ctrl.albumId).query().$promise;
        }
        else {
            albumInfoEntity = {caption: $ctrl.albumCaption};
            AlbumInfo.save(albumInfoEntity, function(d){console.log(d);}, function(e){console.log(e)});

        }
        $ctrl.albumInfoEntity = albumInfoEntity;
        $ctrl.imageList = imageList;
        $ctrl.currentImage={};
        $ctrl.imagesrc= function () {
 // debugger;
         return   DataUtils.imagesrc($ctrl.currentImage.fileDataContentType,$ctrl.currentImage.fileData);
        }
        function addImage() {

        }

        function save() {
            $ctrl.isSaving = true;
            if (albumInfoEntity.id !== null) {
                AlbumInfo.update($ctrl.albumInfo, onSaveSuccess, onSaveError);
            } else {
                AlbumInfo.save($ctrl.albumInfo, onSaveSuccess, onSaveError);
            }
        }

        $ctrl.setFileData = function ($file) {
        //    debugger;
            if ($file) {
                $ctrl.file=$file;
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        $ctrl.currentImage.fileData = base64Data;
                        $ctrl.currentImage.fileDataContentType = $file.type;
                    });
                });
            }
        };
    }

    /*   SingleAlbumInfoController.$inject = ['$timeout', '$scope', '$stateParams', 'entity', 'AlbumInfo'];

     function SingleAlbumInfoController ($timeout, $scope, $stateParams, entity, AlbumInfo) {
     var vm = this;

     vm.albumInfo = entity;
     vm.clear = clear;
     vm.save = save;

     $timeout(function (){
     angular.element('.form-group:eq(1)>input').focus();
     });

     function save () {
     vm.isSaving = true;
     if (vm.albumInfo.id !== null) {
     AlbumInfo.update(vm.albumInfo, onSaveSuccess, onSaveError);
     } else {
     AlbumInfo.save(vm.albumInfo, onSaveSuccess, onSaveError);
     }
     }

     function onSaveSuccess (result) {
     $scope.$emit('tikonApp:albumInfoUpdate', result);
     vm.isSaving = false;
     }

     function onSaveError () {
     vm.isSaving = false;
     }


     }*/
    var ImageGallery = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/eyeson/image-gallery/eyeson.image-gallery.html',
        controller: ImageGalleryController,
        bindings: {
            albumId: '=',
            albumCaption: '<'
            /*  items :'<',
             displaytag:'@?',
             displayitem:'@?',
             list:'='*/

        }

    };

    angular
        .module('tikonApp')
        .component('eoImageGallery', ImageGallery)


})();

