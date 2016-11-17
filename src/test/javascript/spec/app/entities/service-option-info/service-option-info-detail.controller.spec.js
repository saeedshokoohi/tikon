'use strict';

describe('Controller Tests', function() {

    describe('ServiceOptionInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceOptionInfo, MockServiceItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceOptionInfo = jasmine.createSpy('MockServiceOptionInfo');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceOptionInfo': MockServiceOptionInfo,
                'ServiceItem': MockServiceItem
            };
            createController = function() {
                $injector.get('$controller')("ServiceOptionInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:serviceOptionInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
