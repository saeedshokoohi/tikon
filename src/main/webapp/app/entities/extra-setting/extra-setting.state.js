(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('extra-setting', {
            parent: 'entity',
            url: '/extra-setting?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.extraSetting.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-setting/extra-settings.html',
                    controller: 'ExtraSettingController',
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
                    $translatePartialLoader.addPart('extraSetting');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('extra-setting-detail', {
            parent: 'entity',
            url: '/extra-setting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.extraSetting.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extra-setting/extra-setting-detail.html',
                    controller: 'ExtraSettingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('extraSetting');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExtraSetting', function($stateParams, ExtraSetting) {
                    return ExtraSetting.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('extra-setting.new', {
            parent: 'extra-setting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-setting/extra-setting-dialog.html',
                    controller: 'ExtraSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('extra-setting', null, { reload: true });
                }, function() {
                    $state.go('extra-setting');
                });
            }]
        })
        .state('extra-setting.edit', {
            parent: 'extra-setting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-setting/extra-setting-dialog.html',
                    controller: 'ExtraSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtraSetting', function(ExtraSetting) {
                            return ExtraSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-setting', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extra-setting.delete', {
            parent: 'extra-setting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extra-setting/extra-setting-delete-dialog.html',
                    controller: 'ExtraSettingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExtraSetting', function(ExtraSetting) {
                            return ExtraSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extra-setting', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
