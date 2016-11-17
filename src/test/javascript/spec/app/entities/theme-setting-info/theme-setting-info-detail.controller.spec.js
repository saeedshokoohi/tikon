'use strict';

describe('Controller Tests', function() {

    describe('ThemeSettingInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockThemeSettingInfo, MockImageData;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockThemeSettingInfo = jasmine.createSpy('MockThemeSettingInfo');
            MockImageData = jasmine.createSpy('MockImageData');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ThemeSettingInfo': MockThemeSettingInfo,
                'ImageData': MockImageData
            };
            createController = function() {
                $injector.get('$controller')("ThemeSettingInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:themeSettingInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
