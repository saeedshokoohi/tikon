(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-capacity-info', {
            parent: 'entity',
            url: '/service-capacity-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceCapacityInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-capacity-info/service-capacity-infos.html',
                    controller: 'ServiceCapacityInfoController',
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
                    $translatePartialLoader.addPart('serviceCapacityInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-capacity-info-detail', {
            parent: 'entity',
            url: '/service-capacity-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceCapacityInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-capacity-info/service-capacity-info-detail.html',
                    controller: 'ServiceCapacityInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceCapacityInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceCapacityInfo', function($stateParams, ServiceCapacityInfo) {
                    return ServiceCapacityInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-capacity-info.new', {
            parent: 'service-capacity-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-capacity-info/service-capacity-info-dialog.html',
                    controller: 'ServiceCapacityInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                qty: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-capacity-info', null, { reload: true });
                }, function() {
                    $state.go('service-capacity-info');
                });
            }]
        })
        .state('service-capacity-info.edit', {
            parent: 'service-capacity-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-capacity-info/service-capacity-info-dialog.html',
                    controller: 'ServiceCapacityInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceCapacityInfo', function(ServiceCapacityInfo) {
                            return ServiceCapacityInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-capacity-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-capacity-info.delete', {
            parent: 'service-capacity-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-capacity-info/service-capacity-info-delete-dialog.html',
                    controller: 'ServiceCapacityInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceCapacityInfo', function(ServiceCapacityInfo) {
                            return ServiceCapacityInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-capacity-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
