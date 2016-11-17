(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-setting', {
            parent: 'entity',
            url: '/notification-setting?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.notificationSetting.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-setting/notification-settings.html',
                    controller: 'NotificationSettingController',
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
                    $translatePartialLoader.addPart('notificationSetting');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notification-setting-detail', {
            parent: 'entity',
            url: '/notification-setting/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tikonApp.notificationSetting.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-setting/notification-setting-detail.html',
                    controller: 'NotificationSettingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notificationSetting');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NotificationSetting', function($stateParams, NotificationSetting) {
                    return NotificationSetting.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('notification-setting.new', {
            parent: 'notification-setting',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-setting/notification-setting-dialog.html',
                    controller: 'NotificationSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sendBookingConfirmation: null,
                                sendOnCancelBooking: null,
                                sendOnMoveBooking: null,
                                sendCustomerReminder: null,
                                reminderTimeInAdvanced: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-setting', null, { reload: true });
                }, function() {
                    $state.go('notification-setting');
                });
            }]
        })
        .state('notification-setting.edit', {
            parent: 'notification-setting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-setting/notification-setting-dialog.html',
                    controller: 'NotificationSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationSetting', function(NotificationSetting) {
                            return NotificationSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-setting', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-setting.delete', {
            parent: 'notification-setting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-setting/notification-setting-delete-dialog.html',
                    controller: 'NotificationSettingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationSetting', function(NotificationSetting) {
                            return NotificationSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-setting', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
