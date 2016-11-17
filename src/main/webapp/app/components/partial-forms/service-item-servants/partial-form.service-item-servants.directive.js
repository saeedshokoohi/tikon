(function () {
    'use strict';

    function ServiceItemServantsController($scope, $state, ServantCustom, ServantSearch, ParseLinks, AlertService, paginationConstants, translate, translatePartialLoader) {
        debugger;
        var $ctrl = this;
        var pagingParams = {};
        $ctrl.loadPage = loadPage;
        $ctrl.selectedId = '';
        $ctrl.formState = 'list';
        $ctrl.newItem = newItem;
        $ctrl.removeServant = removeServant;
        $ctrl.servants = null;
        $ctrl.servantsByCompany = ServantCustom.findServantsByCompany().query();
        $ctrl.servantToAdd = null;

        loadAll();

        loadPage();

        function loadAll() {


            function onSuccess(data, headers) {

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            translatePartialLoader.addPart('servant');
            translatePartialLoader.addPart('personInfo');
            translatePartialLoader.addPart('global');
            translatePartialLoader = translate.refresh();
            debugger;
            if ($ctrl.serviceItemId == undefined || $ctrl.serviceItemId == null)$ctrl.servants = [];
            else
                $ctrl.servants = ServantCustom.findServantsByServiceItem($ctrl.serviceItemId).query();
        }

        function newItem() {
            if ($ctrl.servantToAdd) {
                var filtered = $ctrl.servants.filter(function (v) {
                    return (v.id == $ctrl.servantToAdd.id)
                });
                if (filtered.length == 0) {
                    $ctrl.servants.push({
                        id: $ctrl.servantToAdd.id,
                        title: $ctrl.servantToAdd.title,
                        level: $ctrl.servantToAdd.level,
                        personInfo: {
                            fisrtName: $ctrl.servantToAdd.personInfo.fisrtName,
                            lastName: $ctrl.servantToAdd.personInfo.lastName,
                            phoneNumber: $ctrl.servantToAdd.personInfo.phoneNumber
                        }
                    });
                    $ctrl.servantToAdd= null;
                }
            }
        }

        function removeServant(id) {

            debugger;
            if (id) {
                $ctrl.servants= $ctrl.servants.filter(function (v) {
                    return (v.id != id)
                }
                );
            }

        }



    }

    var ServiceItemServants = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/service-item-servants/partial-form.service-item-servants.html',
        controller: ServiceItemServantsController,
        bindings: {
            serviceItemId: '<',
            servants: '=',
            selectedId: '=?',
            formState: '=',
        }

    };

    angular
        .module('tikonApp')
        .component('pfServiceItemServants', ServiceItemServants)

    ServiceItemServantsController.$inject = ['$scope', '$state', 'ServantCustom', 'ServantSearch', 'ParseLinks', 'AlertService', 'paginationConstants', '$translate', '$translatePartialLoader'];


})();

