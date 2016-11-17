(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('discount-info', {
            parent: 'entity',
            url: '/discount-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.discountInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount-info/discount-infos.html',
                    controller: 'DiscountInfoController',
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
                    $translatePartialLoader.addPart('discountInfo');
                    $translatePartialLoader.addPart('discountType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('discount-info-detail', {
            parent: 'entity',
            url: '/discount-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.discountInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount-info/discount-info-detail.html',
                    controller: 'DiscountInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discountInfo');
                    $translatePartialLoader.addPart('discountType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DiscountInfo', function($stateParams, DiscountInfo) {
                    return DiscountInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('discount-info.new', {
            parent: 'discount-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-info/discount-info-dialog.html',
                    controller: 'DiscountInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedDiscount: null,
                                discountType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('discount-info', null, { reload: true });
                }, function() {
                    $state.go('discount-info');
                });
            }]
        })
        .state('discount-info.edit', {
            parent: 'discount-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-info/discount-info-dialog.html',
                    controller: 'DiscountInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiscountInfo', function(DiscountInfo) {
                            return DiscountInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount-info.delete', {
            parent: 'discount-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-info/discount-info-delete-dialog.html',
                    controller: 'DiscountInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DiscountInfo', function(DiscountInfo) {
                            return DiscountInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
