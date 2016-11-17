'use strict';

describe('Controller Tests', function() {

    describe('ServiceCategory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceCategory, MockSettingInfo, MockCompany, MockServant, MockAlbumInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceCategory = jasmine.createSpy('MockServiceCategory');
            MockSettingInfo = jasmine.createSpy('MockSettingInfo');
            MockCompany = jasmine.createSpy('MockCompany');
            MockServant = jasmine.createSpy('MockServant');
            MockAlbumInfo = jasmine.createSpy('MockAlbumInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceCategory': MockServiceCategory,
                'SettingInfo': MockSettingInfo,
                'Company': MockCompany,
                'Servant': MockServant,
                'AlbumInfo': MockAlbumInfo
            };
            createController = function() {
                $injector.get('$controller')("ServiceCategoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:serviceCategoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
