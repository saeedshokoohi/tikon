(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-category', {
            parent: 'entity',
            url: '/service-category?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-category/service-categories.html',
                    controller: 'ServiceCategoryController',
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
                    $translatePartialLoader.addPart('serviceCategory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-category-detail', {
            parent: 'entity',
            url: '/service-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-category/service-category-detail.html',
                    controller: 'ServiceCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceCategory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceCategory', function($stateParams, ServiceCategory) {
                    return ServiceCategory.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-category.new', {
            parent: 'service-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-category/service-category-dialog.html',
                    controller: 'ServiceCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                categoryName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-category', null, { reload: true });
                }, function() {
                    $state.go('service-category');
                });
            }]
        })
        .state('service-category.edit', {
            parent: 'service-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-category/service-category-dialog.html',
                    controller: 'ServiceCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceCategory', function(ServiceCategory) {
                            return ServiceCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-category', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-category.delete', {
            parent: 'service-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-category/service-category-delete-dialog.html',
                    controller: 'ServiceCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceCategory', function(ServiceCategory) {
                            return ServiceCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-category', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
