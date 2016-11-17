'use strict';

describe('Controller Tests', function() {

    describe('PersonInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPersonInfo, MockLocationInfo, MockSocialNetworkInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPersonInfo = jasmine.createSpy('MockPersonInfo');
            MockLocationInfo = jasmine.createSpy('MockLocationInfo');
            MockSocialNetworkInfo = jasmine.createSpy('MockSocialNetworkInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PersonInfo': MockPersonInfo,
                'LocationInfo': MockLocationInfo,
                'SocialNetworkInfo': MockSocialNetworkInfo
            };
            createController = function() {
                $injector.get('$controller')("PersonInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:personInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
