(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('related-service-item', {
            parent: 'entity',
            url: '/related-service-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.relatedServiceItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/related-service-item/related-service-items.html',
                    controller: 'RelatedServiceItemController',
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
                    $translatePartialLoader.addPart('relatedServiceItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('related-service-item-detail', {
            parent: 'entity',
            url: '/related-service-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.relatedServiceItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/related-service-item/related-service-item-detail.html',
                    controller: 'RelatedServiceItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('relatedServiceItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RelatedServiceItem', function($stateParams, RelatedServiceItem) {
                    return RelatedServiceItem.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('related-service-item.new', {
            parent: 'related-service-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/related-service-item/related-service-item-dialog.html',
                    controller: 'RelatedServiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('related-service-item', null, { reload: true });
                }, function() {
                    $state.go('related-service-item');
                });
            }]
        })
        .state('related-service-item.edit', {
            parent: 'related-service-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/related-service-item/related-service-item-dialog.html',
                    controller: 'RelatedServiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RelatedServiceItem', function(RelatedServiceItem) {
                            return RelatedServiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('related-service-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('related-service-item.delete', {
            parent: 'related-service-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/related-service-item/related-service-item-delete-dialog.html',
                    controller: 'RelatedServiceItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RelatedServiceItem', function(RelatedServiceItem) {
                            return RelatedServiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('related-service-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
