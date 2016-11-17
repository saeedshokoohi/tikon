(function() {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('supplierPanel.servantListByCompany', {
            parent: 'supplierPanel',
            url: '/servant-by-company-list',
            data: {
                authorities: []
            },
            views: {
                'subcontent': {
                    templateUrl: 'app/entities/servant/servant-by-company.html',
                    controller: 'ServantByCompanyController',
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
                    $translatePartialLoader.addPart('servant');
                    $translatePartialLoader.addPart('personInfo');

                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })


        .state('supplierPanel.servantListByCompany.new', {
            parent: 'supplierPanel.servantListByCompany',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servant/servant-by-company-dialog.html',
                    controller: 'ServantByCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                level: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('supplierPanel.servantListByCompany', null, { reload: true });
                }, function() {
                    $state.go('supplierPanel.servantListByCompany');
                });
            }]
        })
        .state('supplierPanel.servantListByCompany.edit', {
            parent: 'supplierPanel.servantListByCompany',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servant/servant-by-company-dialog.html',
                    controller: 'ServantByCompanyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Servant', function(Servant) {
                            return Servant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('supplierPanel.servantListByCompany', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        //.state('servant.delete', {
        //    parent: 'servant',
        //    url: '/{id}/delete',
        //    data: {
        //        authorities: ['ROLE_USER']
        //    },
        //    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //        $uibModal.open({
        //            templateUrl: 'app/entities/servant/servant-delete-dialog.html',
        //            controller: 'ServantDeleteController',
        //            controllerAs: 'vm',
        //            size: 'md',
        //            resolve: {
        //                entity: ['Servant', function(Servant) {
        //                    return Servant.get({id : $stateParams.id}).$promise;
        //                }]
        //            }
        //        }).result.then(function() {
        //            $state.go('servant', null, { reload: true });
        //        }, function() {
        //            $state.go('^');
        //        });
        //    }]
        //});
    }

})();
