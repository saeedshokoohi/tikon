'use strict';

describe('Controller Tests', function() {

    describe('CompanySocialNetworkInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompanySocialNetworkInfo, MockCompany, MockSocialNetworkInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompanySocialNetworkInfo = jasmine.createSpy('MockCompanySocialNetworkInfo');
            MockCompany = jasmine.createSpy('MockCompany');
            MockSocialNetworkInfo = jasmine.createSpy('MockSocialNetworkInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CompanySocialNetworkInfo': MockCompanySocialNetworkInfo,
                'Company': MockCompany,
                'SocialNetworkInfo': MockSocialNetworkInfo
            };
            createController = function() {
                $injector.get('$controller')("CompanySocialNetworkInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:companySocialNetworkInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
