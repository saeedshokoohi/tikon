'use strict';

describe('Controller Tests', function() {

    describe('TimePeriod Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTimePeriod, MockOffTime, MockWeeklyScheduleInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTimePeriod = jasmine.createSpy('MockTimePeriod');
            MockOffTime = jasmine.createSpy('MockOffTime');
            MockWeeklyScheduleInfo = jasmine.createSpy('MockWeeklyScheduleInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TimePeriod': MockTimePeriod,
                'OffTime': MockOffTime,
                'WeeklyScheduleInfo': MockWeeklyScheduleInfo
            };
            createController = function() {
                $injector.get('$controller')("TimePeriodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:timePeriodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
