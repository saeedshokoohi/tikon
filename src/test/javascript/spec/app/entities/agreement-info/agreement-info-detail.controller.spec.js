'use strict';

describe('Controller Tests', function() {

    describe('AgreementInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAgreementInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAgreementInfo = jasmine.createSpy('MockAgreementInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AgreementInfo': MockAgreementInfo
            };
            createController = function() {
                $injector.get('$controller')("AgreementInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:agreementInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
