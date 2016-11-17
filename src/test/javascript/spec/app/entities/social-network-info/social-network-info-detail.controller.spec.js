'use strict';

describe('Controller Tests', function() {

    describe('SocialNetworkInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSocialNetworkInfo, MockPersonInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSocialNetworkInfo = jasmine.createSpy('MockSocialNetworkInfo');
            MockPersonInfo = jasmine.createSpy('MockPersonInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SocialNetworkInfo': MockSocialNetworkInfo,
                'PersonInfo': MockPersonInfo
            };
            createController = function() {
                $injector.get('$controller')("SocialNetworkInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:socialNetworkInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
