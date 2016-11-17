'use strict';

describe('Controller Tests', function() {

    describe('ExtraSetting Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockExtraSetting, MockSettingInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockExtraSetting = jasmine.createSpy('MockExtraSetting');
            MockSettingInfo = jasmine.createSpy('MockSettingInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ExtraSetting': MockExtraSetting,
                'SettingInfo': MockSettingInfo
            };
            createController = function() {
                $injector.get('$controller')("ExtraSettingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:extraSettingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
