(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-option-info', {
            parent: 'entity',
            url: '/service-option-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceOptionInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-option-info/service-option-infos.html',
                    controller: 'ServiceOptionInfoController',
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
                    $translatePartialLoader.addPart('serviceOptionInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-option-info-detail', {
            parent: 'entity',
            url: '/service-option-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.serviceOptionInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-option-info/service-option-info-detail.html',
                    controller: 'ServiceOptionInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceOptionInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceOptionInfo', function($stateParams, ServiceOptionInfo) {
                    return ServiceOptionInfo.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service-option-info.new', {
            parent: 'service-option-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-option-info/service-option-info-dialog.html',
                    controller: 'ServiceOptionInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service-option-info', null, { reload: true });
                }, function() {
                    $state.go('service-option-info');
                });
            }]
        })
        .state('service-option-info.edit', {
            parent: 'service-option-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-option-info/service-option-info-dialog.html',
                    controller: 'ServiceOptionInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceOptionInfo', function(ServiceOptionInfo) {
                            return ServiceOptionInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-option-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-option-info.delete', {
            parent: 'service-option-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-option-info/service-option-info-delete-dialog.html',
                    controller: 'ServiceOptionInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceOptionInfo', function(ServiceOptionInfo) {
                            return ServiceOptionInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-option-info', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
