(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-option-item', {
            parent: 'entity',
            url: '/service-option-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceOptionItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-option-item/service-option-items.html',
                    controller: 'ServiceOptionItemController',
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
                    $translatePartialLoader.addPart('serviceOptionItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-option-item-detail', {
            parent: 'entity',
            url: '/service-option-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceOptionItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-option-item/service-option-item-detail.html',
                    controller: 'ServiceOptionItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceOptionItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceOptionItem', function($stateParams, ServiceOptionItem) {
                    return ServiceOptionItem.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-option-item.new', {
            parent: 'service-option-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-option-item/service-option-item-dialog.html',
                    controller: 'ServiceOptionItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                optionName: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-option-item', null, { reload: true });
                }, function() {
                    $state.go('service-option-item');
                });
            }]
        })
        .state('service-option-item.edit', {
            parent: 'service-option-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-option-item/service-option-item-dialog.html',
                    controller: 'ServiceOptionItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceOptionItem', function(ServiceOptionItem) {
                            return ServiceOptionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-option-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-option-item.delete', {
            parent: 'service-option-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-option-item/service-option-item-delete-dialog.html',
                    controller: 'ServiceOptionItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceOptionItem', function(ServiceOptionItem) {
                            return ServiceOptionItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-option-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
