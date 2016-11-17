(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('waiting-list', {
            parent: 'entity',
            url: '/waiting-list?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.waitingList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/waiting-list/waiting-lists.html',
                    controller: 'WaitingListController',
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
                    $translatePartialLoader.addPart('waitingList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('waiting-list-detail', {
            parent: 'entity',
            url: '/waiting-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.waitingList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/waiting-list/waiting-list-detail.html',
                    controller: 'WaitingListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('waitingList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WaitingList', function($stateParams, WaitingList) {
                    return WaitingList.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('waiting-list.new', {
            parent: 'waiting-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waiting-list/waiting-list-dialog.html',
                    controller: 'WaitingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                qty: null,
                                reserveTime: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('waiting-list', null, { reload: true });
                }, function() {
                    $state.go('waiting-list');
                });
            }]
        })
        .state('waiting-list.edit', {
            parent: 'waiting-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waiting-list/waiting-list-dialog.html',
                    controller: 'WaitingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WaitingList', function(WaitingList) {
                            return WaitingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('waiting-list', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('waiting-list.delete', {
            parent: 'waiting-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waiting-list/waiting-list-delete-dialog.html',
                    controller: 'WaitingListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WaitingList', function(WaitingList) {
                            return WaitingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('waiting-list', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
