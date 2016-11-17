(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-time-session', {
            parent: 'entity',
            url: '/service-time-session?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceTimeSession.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-time-session/service-time-sessions.html',
                    controller: 'ServiceTimeSessionController',
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
                    $translatePartialLoader.addPart('serviceTimeSession');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-time-session-detail', {
            parent: 'entity',
            url: '/service-time-session/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceTimeSession.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-time-session/service-time-session-detail.html',
                    controller: 'ServiceTimeSessionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceTimeSession');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceTimeSession', function($stateParams, ServiceTimeSession) {
                    return ServiceTimeSession.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-time-session.new', {
            parent: 'service-time-session',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-time-session/service-time-session-dialog.html',
                    controller: 'ServiceTimeSessionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                startDate: null,
                                endDate: null,
                                capacity: null,
                                comment: null,
                                price: null,
                                discount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-time-session', null, { reload: true });
                }, function() {
                    $state.go('service-time-session');
                });
            }]
        })
        .state('service-time-session.edit', {
            parent: 'service-time-session',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-time-session/service-time-session-dialog.html',
                    controller: 'ServiceTimeSessionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceTimeSession', function(ServiceTimeSession) {
                            return ServiceTimeSession.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-time-session', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-time-session.delete', {
            parent: 'service-time-session',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-time-session/service-time-session-delete-dialog.html',
                    controller: 'ServiceTimeSessionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceTimeSession', function(ServiceTimeSession) {
                            return ServiceTimeSession.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-time-session', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
