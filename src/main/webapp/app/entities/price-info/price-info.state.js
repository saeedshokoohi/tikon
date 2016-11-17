(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('price-info', {
            parent: 'entity',
            url: '/price-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.priceInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-info/price-infos.html',
                    controller: 'PriceInfoController',
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
                    $translatePartialLoader.addPart('priceInfo');
                    $translatePartialLoader.addPart('priceType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('price-info-detail', {
            parent: 'entity',
            url: '/price-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.priceInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-info/price-info-detail.html',
                    controller: 'PriceInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceInfo');
                    $translatePartialLoader.addPart('priceType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PriceInfo', function($stateParams, PriceInfo) {
                    return PriceInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('price-info.new', {
            parent: 'price-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-info/price-info-dialog.html',
                    controller: 'PriceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fromValidDate: null,
                                priceType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('price-info', null, { reload: true });
                }, function() {
                    $state.go('price-info');
                });
            }]
        })
        .state('price-info.edit', {
            parent: 'price-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-info/price-info-dialog.html',
                    controller: 'PriceInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceInfo', function(PriceInfo) {
                            return PriceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-info.delete', {
            parent: 'price-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-info/price-info-delete-dialog.html',
                    controller: 'PriceInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PriceInfo', function(PriceInfo) {
                            return PriceInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
