'use strict';

describe('Controller Tests', function() {

    describe('WeeklyWorkDay Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWeeklyWorkDay, MockWeeklyScheduleInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWeeklyWorkDay = jasmine.createSpy('MockWeeklyWorkDay');
            MockWeeklyScheduleInfo = jasmine.createSpy('MockWeeklyScheduleInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'WeeklyWorkDay': MockWeeklyWorkDay,
                'WeeklyScheduleInfo': MockWeeklyScheduleInfo
            };
            createController = function() {
                $injector.get('$controller')("WeeklyWorkDayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:weeklyWorkDayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
