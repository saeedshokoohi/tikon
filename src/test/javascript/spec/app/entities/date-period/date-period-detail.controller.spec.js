'use strict';

describe('Controller Tests', function() {

    describe('DatePeriod Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDatePeriod, MockOffDay, MockWeeklyScheduleInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDatePeriod = jasmine.createSpy('MockDatePeriod');
            MockOffDay = jasmine.createSpy('MockOffDay');
            MockWeeklyScheduleInfo = jasmine.createSpy('MockWeeklyScheduleInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DatePeriod': MockDatePeriod,
                'OffDay': MockOffDay,
                'WeeklyScheduleInfo': MockWeeklyScheduleInfo
            };
            createController = function() {
                $injector.get('$controller')("DatePeriodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:datePeriodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
