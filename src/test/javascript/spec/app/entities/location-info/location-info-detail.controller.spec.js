'use strict';

describe('Controller Tests', function() {

    describe('LocationInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLocationInfo, MockSelectorData;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLocationInfo = jasmine.createSpy('MockLocationInfo');
            MockSelectorData = jasmine.createSpy('MockSelectorData');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LocationInfo': MockLocationInfo,
                'SelectorData': MockSelectorData
            };
            createController = function() {
                $injector.get('$controller')("LocationInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:locationInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
