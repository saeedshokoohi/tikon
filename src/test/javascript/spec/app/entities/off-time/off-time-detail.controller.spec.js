'use strict';

describe('Controller Tests', function() {

    describe('OffTime Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOffTime, MockTimePeriod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOffTime = jasmine.createSpy('MockOffTime');
            MockTimePeriod = jasmine.createSpy('MockTimePeriod');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OffTime': MockOffTime,
                'TimePeriod': MockTimePeriod
            };
            createController = function() {
                $injector.get('$controller')("OffTimeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:offTimeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
