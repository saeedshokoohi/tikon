'use strict';

describe('Controller Tests', function() {

    describe('WeeklyScheduleInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWeeklyScheduleInfo, MockDatePeriod, MockTimePeriod, MockWeeklyWorkDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWeeklyScheduleInfo = jasmine.createSpy('MockWeeklyScheduleInfo');
            MockDatePeriod = jasmine.createSpy('MockDatePeriod');
            MockTimePeriod = jasmine.createSpy('MockTimePeriod');
            MockWeeklyWorkDay = jasmine.createSpy('MockWeeklyWorkDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WeeklyScheduleInfo': MockWeeklyScheduleInfo,
                'DatePeriod': MockDatePeriod,
                'TimePeriod': MockTimePeriod,
                'WeeklyWorkDay': MockWeeklyWorkDay
            };
            createController = function() {
                $injector.get('$controller')("WeeklyScheduleInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:weeklyScheduleInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
