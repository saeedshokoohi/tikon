(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('SocialNetworkInfoDeleteController',SocialNetworkInfoDeleteController);

    SocialNetworkInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'SocialNetworkInfo'];

    function SocialNetworkInfoDeleteController($uibModalInstance, entity, SocialNetworkInfo) {
        var vm = this;

        vm.socialNetworkInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SocialNetworkInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
