(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-item', {
            parent: 'entity',
            url: '/service-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-item/service-items.html',
                    controller: 'ServiceItemController',
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
                    $translatePartialLoader.addPart('serviceItem');
                    $translatePartialLoader.addPart('serviceItemType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-item-detail', {
            parent: 'entity',
            url: '/service-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-item/service-item-detail.html',
                    controller: 'ServiceItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceItem');
                    $translatePartialLoader.addPart('serviceItemType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceItem', function($stateParams, ServiceItem) {
                    return ServiceItem.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-item.new', {
            parent: 'service-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-item/service-item-dialog.html',
                    controller: 'ServiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                itemTitle: null,
                                description: null,
                                minPreReserveTime: null,
                                maxPreReserveTime: null,
                                hasWaitingList: null,
                                mustGetParticipantInfo: null,
                                canBeCanceled: null,
                                minPreCancelTime: null,
                                paymentType: null,
                                serviceType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-item', null, { reload: true });
                }, function() {
                    $state.go('service-item');
                });
            }]
        })
        .state('service-item.edit', {
            parent: 'service-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-item/service-item-dialog.html',
                    controller: 'ServiceItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceItem', function(ServiceItem) {
                            return ServiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-item.delete', {
            parent: 'service-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-item/service-item-delete-dialog.html',
                    controller: 'ServiceItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceItem', function(ServiceItem) {
                            return ServiceItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
