'use strict';

describe('Controller Tests', function() {

    describe('FinancialSetting Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFinancialSetting;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFinancialSetting = jasmine.createSpy('MockFinancialSetting');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FinancialSetting': MockFinancialSetting
            };
            createController = function() {
                $injector.get('$controller')("FinancialSettingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:financialSettingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
