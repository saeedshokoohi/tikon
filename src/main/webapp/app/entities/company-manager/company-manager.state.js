(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-manager', {
            parent: 'entity',
            url: '/company-manager?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.companyManager.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-manager/company-managers.html',
                    controller: 'CompanyManagerController',
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
                    $translatePartialLoader.addPart('companyManager');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-manager-detail', {
            parent: 'entity',
            url: '/company-manager/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.companyManager.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-manager/company-manager-detail.html',
                    controller: 'CompanyManagerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companyManager');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanyManager', function($stateParams, CompanyManager) {
                    return CompanyManager.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('company-manager.new', {
            parent: 'company-manager',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-manager/company-manager-dialog.html',
                    controller: 'CompanyManagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                managerType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-manager', null, { reload: true });
                }, function() {
                    $state.go('company-manager');
                });
            }]
        })
        .state('company-manager.edit', {
            parent: 'company-manager',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-manager/company-manager-dialog.html',
                    controller: 'CompanyManagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanyManager', function(CompanyManager) {
                            return CompanyManager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-manager', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-manager.delete', {
            parent: 'company-manager',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-manager/company-manager-delete-dialog.html',
                    controller: 'CompanyManagerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanyManager', function(CompanyManager) {
                            return CompanyManager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-manager', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
