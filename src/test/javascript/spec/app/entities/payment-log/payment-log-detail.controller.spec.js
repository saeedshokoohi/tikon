'use strict';

describe('Controller Tests', function() {

    describe('PaymentLog Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPaymentLog, MockInvoiceInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPaymentLog = jasmine.createSpy('MockPaymentLog');
            MockInvoiceInfo = jasmine.createSpy('MockInvoiceInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PaymentLog': MockPaymentLog,
                'InvoiceInfo': MockInvoiceInfo
            };
            createController = function() {
                $injector.get('$controller')("PaymentLogDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:paymentLogUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
