(function () {
    'use strict';

    angular
        .module('tikonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('supplierPanel', {
                parent: 'app',
                url: '/supplierPanel',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/supplier/supplierPanel.html',
                        controller: 'SupplierPanelController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplierPanel');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierPanel.companyLocationInfo', {
                parent: 'supplierPanel',
                url: '/companyLocationInfo',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/location-info/company-location-info.html',
                        controller: 'CompanyLocationInfoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('locationInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierPanel.companyAgreementInfo', {
                parent: 'supplierPanel',
                url: '/companyAgreementInfo',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/agreement-info/company-agreement-info.html',
                        controller: 'CompanyAgreementInfoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('agreementInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierPanel.companySettingInfo', {
                parent: 'supplierPanel',
                url: '/companySettingInfo',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/setting-info/company-setting-info.html',
                        controller: 'CompanySettingInfoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('settingInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierPanel.generalServiceItem', {
                parent: 'supplierPanel',
                url: '/generalServiceItem/{id}',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/service-item/general-service-item.html',
                        controller: 'GeneralServiceItemController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('serviceItem');
                        $translatePartialLoader.addPart('supplierPanel');
                        $translatePartialLoader.addPart('home');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ServiceItem', function ($stateParams, ServiceItem) {
                        //debugger;
                        if($stateParams.id!='')
                        return ServiceItem.get({id: $stateParams.id}).$promise;
                        return {};
                    }]
                }
            })
            .state('supplierPanel.generalServiceItem.view', {
                parent: 'supplierPanel',
                url: '/generalServiceItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tikonApp.serviceItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/service-item/general-service-item.html',
                        controller: 'GeneralServiceItemController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('serviceItem');
                        $translatePartialLoader.addPart('supplierPanel');
                        $translatePartialLoader.addPart('home');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ServiceItem', function($stateParams, ServiceItem) {
                        return ServiceItem.get({id : $stateParams.id}).$promise;
                    }]
                }
            })
            .state('supplierPanel.serviceItemList', {
                parent: 'supplierPanel',
                url: '/serviceItemList',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/service-item/service-item-list.html',
                        controller: 'ServiceItemListController',
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
                        $translatePartialLoader.addPart('serviceItem');
                        $translatePartialLoader.addPart('serviceItemType');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('home');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierPanel.companyServiceCategoryList', {
                parent: 'supplierPanel',
                url: '/company-service-category-list',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/service-category/company-service-category-list.html',
                        controller: 'CompanyServiceCategoryController',
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
                        $translatePartialLoader.addPart('serviceCategory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierPanel.companyServiceCategoryList.new', {
                parent: 'supplierPanel.companyServiceCategoryList',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/service-category/company-service-category-dialog.html',
                        controller: 'CompanyServiceCategoryDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    categoryName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('supplierPanel.companyServiceCategoryList', null, { reload: true });
                    }, function() {
                        $state.go('supplierPanel.companyServiceCategoryList');
                    });
                }]
            })
            .state('supplierPanel.companyServiceCategoryList.edit', {
                parent: 'supplierPanel.companyServiceCategoryList',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/service-category/company-service-category-dialog.html',
                        controller: 'CompanyServiceCategoryDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['ServiceCategory', function(ServiceCategory) {
                                return ServiceCategory.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('supplierPanel.companyServiceCategoryList', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('supplierPanel.companyInfo', {
                parent: 'supplierPanel',
                url: '/companyInfo',
                data: {
                    authorities: []
                },
                views: {
                    'subcontent': {
                        templateUrl: 'app/entities/company/company-info.html',
                        controller: 'CompanyInfoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('company');
                        $translatePartialLoader.addPart('activityType');
                        return $translate.refresh();
                    }]
                    //entity: ['$stateParams', 'Company', function ($stateParams, Company) {
                    //    return Company.get({id: $stateParams.id}).$promise;
                    //}]
                }
            })
            //.state('supplierPanel.servantListByCompany', {
            //    parent: 'supplierPanel',
            //    url: '/servant-by-company-list',
            //    data: {
            //        authorities: []
            //    },
            //    views: {
            //        'subcontent': {
            //            templateUrl: 'app/entities/servant/servant-by-company.html',
            //            controller: 'ServantByCompanyController',
            //            controllerAs: 'vm'
            //        }
            //    },
            //    params: {
            //        page: {
            //            value: '1',
            //            squash: true
            //        },
            //        sort: {
            //            value: 'id,asc',
            //            squash: true
            //        },
            //        search: null
            //    },
            //    resolve: {
            //        pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
            //            return {
            //                page: PaginationUtil.parsePage($stateParams.page),
            //                sort: $stateParams.sort,
            //                predicate: PaginationUtil.parsePredicate($stateParams.sort),
            //                ascending: PaginationUtil.parseAscending($stateParams.sort),
            //                search: $stateParams.search
            //            };
            //        }],
            //        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            //            $translatePartialLoader.addPart('servant');
            //            $translatePartialLoader.addPart('global');
            //            return $translate.refresh();
            //        }]
            //    }
            //})


        ;

    }
})();
