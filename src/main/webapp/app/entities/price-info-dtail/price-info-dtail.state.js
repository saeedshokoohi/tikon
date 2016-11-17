(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('price-info-dtail', {
            parent: 'entity',
            url: '/price-info-dtail?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.priceInfoDtail.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-info-dtail/price-info-dtails.html',
                    controller: 'PriceInfoDtailController',
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
                    $translatePartialLoader.addPart('priceInfoDtail');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('price-info-dtail-detail', {
            parent: 'entity',
            url: '/price-info-dtail/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.priceInfoDtail.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-info-dtail/price-info-dtail-detail.html',
                    controller: 'PriceInfoDtailDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceInfoDtail');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PriceInfoDtail', function($stateParams, PriceInfoDtail) {
                    return PriceInfoDtail.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('price-info-dtail.new', {
            parent: 'price-info-dtail',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-info-dtail/price-info-dtail-dialog.html',
                    controller: 'PriceInfoDtailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                capacityRatio: null,
                                price: null,
                                coworkerPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('price-info-dtail', null, { reload: true });
                }, function() {
                    $state.go('price-info-dtail');
                });
            }]
        })
        .state('price-info-dtail.edit', {
            parent: 'price-info-dtail',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-info-dtail/price-info-dtail-dialog.html',
                    controller: 'PriceInfoDtailDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceInfoDtail', function(PriceInfoDtail) {
                            return PriceInfoDtail.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-info-dtail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-info-dtail.delete', {
            parent: 'price-info-dtail',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-info-dtail/price-info-dtail-delete-dialog.html',
                    controller: 'PriceInfoDtailDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PriceInfoDtail', function(PriceInfoDtail) {
                            return PriceInfoDtail.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-info-dtail', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
