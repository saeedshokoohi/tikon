(function() {
    'use strict';
    function ImageUploadController($scope,$attrs,$element) {
//debugger;
        var $ctrl = this;


    }
    var ImageUpload = {
       // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/eyeson/image-upload/eyeson.image-upload.html',
        controller:ImageUploadController,
        bindings:
        {
          /*  items :'<',
            displaytag:'@?',
            displayitem:'@?',
            list:'='*/

        }

    };

    angular
        .module('tikonApp')
        .component('eoImageUpload', ImageUpload)

    ImageUploadController.$inject = ['$scope','$attrs','$element'];


})();

