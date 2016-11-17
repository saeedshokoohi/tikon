'use strict';

describe('Controller Tests', function() {

    describe('Company Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompany, MockSettingInfo, MockAgreementInfo, MockLocationInfo, MockMetaTag;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompany = jasmine.createSpy('MockCompany');
            MockSettingInfo = jasmine.createSpy('MockSettingInfo');
            MockAgreementInfo = jasmine.createSpy('MockAgreementInfo');
            MockLocationInfo = jasmine.createSpy('MockLocationInfo');
            MockMetaTag = jasmine.createSpy('MockMetaTag');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Company': MockCompany,
                'SettingInfo': MockSettingInfo,
                'AgreementInfo': MockAgreementInfo,
                'LocationInfo': MockLocationInfo,
                'MetaTag': MockMetaTag
            };
            createController = function() {
                $injector.get('$controller')("CompanyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:companyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
