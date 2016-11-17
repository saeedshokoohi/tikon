(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-social-network-info', {
            parent: 'entity',
            url: '/company-social-network-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.companySocialNetworkInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-social-network-info/company-social-network-infos.html',
                    controller: 'CompanySocialNetworkInfoController',
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
                    $translatePartialLoader.addPart('companySocialNetworkInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('company-social-network-info-detail', {
            parent: 'entity',
            url: '/company-social-network-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.companySocialNetworkInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-social-network-info/company-social-network-info-detail.html',
                    controller: 'CompanySocialNetworkInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('companySocialNetworkInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CompanySocialNetworkInfo', function($stateParams, CompanySocialNetworkInfo) {
                    return CompanySocialNetworkInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('company-social-network-info.new', {
            parent: 'company-social-network-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-social-network-info/company-social-network-info-dialog.html',
                    controller: 'CompanySocialNetworkInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orderNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('company-social-network-info', null, { reload: true });
                }, function() {
                    $state.go('company-social-network-info');
                });
            }]
        })
        .state('company-social-network-info.edit', {
            parent: 'company-social-network-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-social-network-info/company-social-network-info-dialog.html',
                    controller: 'CompanySocialNetworkInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CompanySocialNetworkInfo', function(CompanySocialNetworkInfo) {
                            return CompanySocialNetworkInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-social-network-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('company-social-network-info.delete', {
            parent: 'company-social-network-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/company-social-network-info/company-social-network-info-delete-dialog.html',
                    controller: 'CompanySocialNetworkInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CompanySocialNetworkInfo', function(CompanySocialNetworkInfo) {
                            return CompanySocialNetworkInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('company-social-network-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
