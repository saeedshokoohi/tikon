(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-rank', {
            parent: 'entity',
            url: '/customer-rank?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.customerRank.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-rank/customer-ranks.html',
                    controller: 'CustomerRankController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerRank');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-rank-detail', {
            parent: 'entity',
            url: '/customer-rank/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.customerRank.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-rank/customer-rank-detail.html',
                    controller: 'CustomerRankDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerRank');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerRank', function($stateParams, CustomerRank) {
                    return CustomerRank.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('customer-rank.new', {
            parent: 'customer-rank',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-rank/customer-rank-dialog.html',
                    controller: 'CustomerRankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                rankValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-rank', null, { reload: true });
                }, function() {
                    $state.go('customer-rank');
                });
            }]
        })
        .state('customer-rank.edit', {
            parent: 'customer-rank',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-rank/customer-rank-dialog.html',
                    controller: 'CustomerRankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerRank', function(CustomerRank) {
                            return CustomerRank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-rank', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-rank.delete', {
            parent: 'customer-rank',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-rank/customer-rank-delete-dialog.html',
                    controller: 'CustomerRankDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerRank', function(CustomerRank) {
                            return CustomerRank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-rank', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
