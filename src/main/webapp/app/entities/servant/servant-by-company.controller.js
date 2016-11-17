(function() {
    'use strict';

    angular
        .module('tikonApp')
        .controller('ServantByCompanyController', ServantByCompanyController);

    ServantByCompanyController.$inject = ['$scope', '$state', 'Servant', 'ServantSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','ServantCustom'];

    function ServantByCompanyController ($scope, $state, Servant, ServantSearch, ParseLinks, AlertService, pagingParams, paginationConstants,ServantCustom) {
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
            //    ServantSearch.query({
            //        query: pagingParams.search,
            //        page: pagingParams.page - 1,
            //        size: paginationConstants.itemsPerPage,
            //        sort: sort()
            //    }, onSuccess, onError);
            //} else {
            //    Servant.query({
            //        page: pagingParams.page - 1,
            //        size: paginationConstants.itemsPerPage,
            //        sort: sort()
            //    }, onSuccess, onError);
            //}
            //function sort() {
            //    var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            //    if (vm.predicate !== 'id') {
            //        result.push('id');
            //    }
            //    return result;
            //}
            function onSuccess(data, headers) {
                //vm.links = ParseLinks.parse(headers('link'));
                //vm.totalItems = headers('X-Total-Count');
                //vm.queryCount = vm.totalItems;
                vm.servants = data;
                //vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            //vm.page = page;
            //vm.transition();

            //if($ctrl.companyId==undefined || $ctrl.companyId==null)$ctrl.priceInfos=[];
            //else
                vm.servants =  ServantCustom.findServantsByCompany().query();

            debugger;
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
