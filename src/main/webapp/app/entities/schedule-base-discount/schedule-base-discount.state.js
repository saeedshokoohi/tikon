(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('schedule-base-discount', {
            parent: 'entity',
            url: '/schedule-base-discount?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.scheduleBaseDiscount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schedule-base-discount/schedule-base-discounts.html',
                    controller: 'ScheduleBaseDiscountController',
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
                    $translatePartialLoader.addPart('scheduleBaseDiscount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('schedule-base-discount-detail', {
            parent: 'entity',
            url: '/schedule-base-discount/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.scheduleBaseDiscount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/schedule-base-discount/schedule-base-discount-detail.html',
                    controller: 'ScheduleBaseDiscountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('scheduleBaseDiscount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ScheduleBaseDiscount', function($stateParams, ScheduleBaseDiscount) {
                    return ScheduleBaseDiscount.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('schedule-base-discount.new', {
            parent: 'schedule-base-discount',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule-base-discount/schedule-base-discount-dialog.html',
                    controller: 'ScheduleBaseDiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('schedule-base-discount', null, { reload: true });
                }, function() {
                    $state.go('schedule-base-discount');
                });
            }]
        })
        .state('schedule-base-discount.edit', {
            parent: 'schedule-base-discount',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule-base-discount/schedule-base-discount-dialog.html',
                    controller: 'ScheduleBaseDiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ScheduleBaseDiscount', function(ScheduleBaseDiscount) {
                            return ScheduleBaseDiscount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schedule-base-discount', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('schedule-base-discount.delete', {
            parent: 'schedule-base-discount',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/schedule-base-discount/schedule-base-discount-delete-dialog.html',
                    controller: 'ScheduleBaseDiscountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ScheduleBaseDiscount', function(ScheduleBaseDiscount) {
                            return ScheduleBaseDiscount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('schedule-base-discount', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
