'use strict';

describe('Controller Tests', function() {

    describe('ScheduleBaseDiscount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScheduleBaseDiscount, MockScheduleInfo, MockDiscountInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScheduleBaseDiscount = jasmine.createSpy('MockScheduleBaseDiscount');
            MockScheduleInfo = jasmine.createSpy('MockScheduleInfo');
            MockDiscountInfo = jasmine.createSpy('MockDiscountInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ScheduleBaseDiscount': MockScheduleBaseDiscount,
                'ScheduleInfo': MockScheduleInfo,
                'DiscountInfo': MockDiscountInfo
            };
            createController = function() {
                $injector.get('$controller')("ScheduleBaseDiscountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:scheduleBaseDiscountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
