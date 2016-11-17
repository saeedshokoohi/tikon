'use strict';

describe('Controller Tests', function() {

    describe('Customer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCustomer, MockPersonInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockPersonInfo = jasmine.createSpy('MockPersonInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Customer': MockCustomer,
                'PersonInfo': MockPersonInfo
            };
            createController = function() {
                $injector.get('$controller')("CustomerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:customerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
