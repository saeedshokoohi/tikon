(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServiceItemListController', ServiceItemListController);

    ServiceItemListController.$inject = ['$scope', '$state', 'ServiceItem', 'ServiceItemSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','ServiceItemCustom'];

    function ServiceItemListController($scope, $state, ServiceItem, ServiceItemSearch, ParseLinks, AlertService, pagingParams, paginationConstants,ServiceItemCustom) {
        var vm = this;
        debugger;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;


        loadAll();
        loadPage();

        function loadAll () {
            debugger;
            //if (pagingParams.search) {
            //    ServiceItemSearch.query({
            //
            //        query: pagingParams.search,
            //        page: pagingParams.page - 1,
            //        size: paginationConstants.itemsPerPage,
            //        sort: sort()
            //    }, onSuccess, onError);
            //} else {
            //    ServiceItem.query({
            //        page: pagingParams.page - 1,
            //        size: paginationConstants.itemsPerPage,
            //        sort: sort()
            //    }, onSuccess, onError);
            //}



            debugger;

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.serviceItems = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage () {
            vm.serviceItems =  ServiceItemCustom.getServiceItemsByCurrentCompany().query();
            //vm.page = page;
            //vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear () {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
