(function() {
    'use strict';
    function ActionBarController($scope,$attrs,$element) {
//debugger;
        var $ctrl = this;

    }
    var actionbar = {
    //    template:'TTETTET',
        templateUrl: 'app/components/action-bar/action-bar.template.html',
        controller:ActionBarController,
        transclude:true,
        bindings:
        {

        }

    };

    angular
        .module('tikonApp')
        .component('actionBar', actionbar);

    ActionBarController.$inject = ['$scope','$attrs','$element'];


})();




