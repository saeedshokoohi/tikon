(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('canceling-info', {
            parent: 'entity',
            url: '/canceling-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.cancelingInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/canceling-info/canceling-infos.html',
                    controller: 'CancelingInfoController',
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
                    $translatePartialLoader.addPart('cancelingInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('canceling-info-detail', {
            parent: 'entity',
            url: '/canceling-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.cancelingInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/canceling-info/canceling-info-detail.html',
                    controller: 'CancelingInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cancelingInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CancelingInfo', function($stateParams, CancelingInfo) {
                    return CancelingInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('canceling-info.new', {
            parent: 'canceling-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/canceling-info/canceling-info-dialog.html',
                    controller: 'CancelingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('canceling-info', null, { reload: true });
                }, function() {
                    $state.go('canceling-info');
                });
            }]
        })
        .state('canceling-info.edit', {
            parent: 'canceling-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/canceling-info/canceling-info-dialog.html',
                    controller: 'CancelingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CancelingInfo', function(CancelingInfo) {
                            return CancelingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('canceling-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('canceling-info.delete', {
            parent: 'canceling-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/canceling-info/canceling-info-delete-dialog.html',
                    controller: 'CancelingInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CancelingInfo', function(CancelingInfo) {
                            return CancelingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('canceling-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
