'use strict';

describe('Controller Tests', function() {

    describe('CompanyManager Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompanyManager, MockCompany, MockPersonInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompanyManager = jasmine.createSpy('MockCompanyManager');
            MockCompany = jasmine.createSpy('MockCompany');
            MockPersonInfo = jasmine.createSpy('MockPersonInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CompanyManager': MockCompanyManager,
                'Company': MockCompany,
                'PersonInfo': MockPersonInfo
            };
            createController = function() {
                $injector.get('$controller')("CompanyManagerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:companyManagerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
