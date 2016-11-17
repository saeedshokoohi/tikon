'use strict';

describe('Controller Tests', function() {

    describe('InvoiceInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInvoiceInfo, MockOrderBag, MockPaymentLog, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInvoiceInfo = jasmine.createSpy('MockInvoiceInfo');
            MockOrderBag = jasmine.createSpy('MockOrderBag');
            MockPaymentLog = jasmine.createSpy('MockPaymentLog');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InvoiceInfo': MockInvoiceInfo,
                'OrderBag': MockOrderBag,
                'PaymentLog': MockPaymentLog,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("InvoiceInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:invoiceInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
