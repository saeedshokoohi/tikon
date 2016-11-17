(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('financial-setting', {
            parent: 'entity',
            url: '/financial-setting?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.financialSetting.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/financial-setting/financial-settings.html',
                    controller: 'FinancialSettingController',
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
                    $translatePartialLoader.addPart('financialSetting');
                    $translatePartialLoader.addPart('currency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('financial-setting-detail', {
            parent: 'entity',
            url: '/financial-setting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.financialSetting.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/financial-setting/financial-setting-detail.html',
                    controller: 'FinancialSettingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('financialSetting');
                    $translatePartialLoader.addPart('currency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FinancialSetting', function($stateParams, FinancialSetting) {
                    return FinancialSetting.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('financial-setting.new', {
            parent: 'financial-setting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/financial-setting/financial-setting-dialog.html',
                    controller: 'FinancialSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                taxPercentage: null,
                                hasDiscount: null,
                                currency: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('financial-setting', null, { reload: true });
                }, function() {
                    $state.go('financial-setting');
                });
            }]
        })
        .state('financial-setting.edit', {
            parent: 'financial-setting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/financial-setting/financial-setting-dialog.html',
                    controller: 'FinancialSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FinancialSetting', function(FinancialSetting) {
                            return FinancialSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('financial-setting', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('financial-setting.delete', {
            parent: 'financial-setting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/financial-setting/financial-setting-delete-dialog.html',
                    controller: 'FinancialSettingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FinancialSetting', function(FinancialSetting) {
                            return FinancialSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('financial-setting', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
