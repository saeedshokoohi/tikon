(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-time-session-info', {
            parent: 'entity',
            url: '/service-time-session-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceTimeSessionInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-time-session-info/service-time-session-infos.html',
                    controller: 'ServiceTimeSessionInfoController',
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
                    $translatePartialLoader.addPart('serviceTimeSessionInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-time-session-info-detail', {
            parent: 'entity',
            url: '/service-time-session-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceTimeSessionInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-time-session-info/service-time-session-info-detail.html',
                    controller: 'ServiceTimeSessionInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceTimeSessionInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceTimeSessionInfo', function($stateParams, ServiceTimeSessionInfo) {
                    return ServiceTimeSessionInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-time-session-info.new', {
            parent: 'service-time-session-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-time-session-info/service-time-session-info-dialog.html',
                    controller: 'ServiceTimeSessionInfoDialogController',
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
                    $state.go('service-time-session-info', null, { reload: true });
                }, function() {
                    $state.go('service-time-session-info');
                });
            }]
        })
        .state('service-time-session-info.edit', {
            parent: 'service-time-session-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-time-session-info/service-time-session-info-dialog.html',
                    controller: 'ServiceTimeSessionInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceTimeSessionInfo', function(ServiceTimeSessionInfo) {
                            return ServiceTimeSessionInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-time-session-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-time-session-info.delete', {
            parent: 'service-time-session-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-time-session-info/service-time-session-info-delete-dialog.html',
                    controller: 'ServiceTimeSessionInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceTimeSessionInfo', function(ServiceTimeSessionInfo) {
                            return ServiceTimeSessionInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-time-session-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
