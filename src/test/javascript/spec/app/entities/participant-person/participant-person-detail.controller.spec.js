'use strict';

describe('Controller Tests', function() {

    describe('ParticipantPerson Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockParticipantPerson, MockPersonInfo, MockOrderBagServiceItemDtail;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockParticipantPerson = jasmine.createSpy('MockParticipantPerson');
            MockPersonInfo = jasmine.createSpy('MockPersonInfo');
            MockOrderBagServiceItemDtail = jasmine.createSpy('MockOrderBagServiceItemDtail');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ParticipantPerson': MockParticipantPerson,
                'PersonInfo': MockPersonInfo,
                'OrderBagServiceItemDtail': MockOrderBagServiceItemDtail
            };
            createController = function() {
                $injector.get('$controller')("ParticipantPersonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:participantPersonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
