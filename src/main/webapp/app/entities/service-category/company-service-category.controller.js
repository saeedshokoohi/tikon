(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('CompanyServiceCategoryController', CompanyServiceCategoryController);

    CompanyServiceCategoryController.$inject = ['$scope', '$state', 'ServiceCategory', 'ServiceCategorySearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','ServiceCategoryCustom'];

    function CompanyServiceCategoryController ($scope, $state, ServiceCategory, ServiceCategorySearch, ParseLinks, AlertService, pagingParams, paginationConstants,ServiceCategoryCustom) {
        var vm = this;

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
            //if (pagingParams.search) {
            //    ServiceCategorySearch.query({
            //        query: pagingParams.search,
            //        page: pagingParams.page - 1,
            //        size: paginationConstants.itemsPerPage,
            //        sort: sort()
            //    }, onSuccess, onError);
            //} else {
            //    ServiceCategory.query({
            //        page: pagingParams.page - 1,
            //        size: paginationConstants.itemsPerPage,
            //        sort: sort()
            //    }, onSuccess, onError);
            //}
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
                vm.serviceCategories = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage () {
            vm.serviceCategories =ServiceCategoryCustom.getServiceCategoriesByCurrentCompany().query();
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
