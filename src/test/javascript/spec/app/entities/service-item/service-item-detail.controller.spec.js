'use strict';

describe('Controller Tests', function() {

    describe('ServiceItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceItem, MockServiceOptionInfo, MockDiscountInfo, MockLocationInfo, MockAlbumInfo, MockServiceCapacityInfo, MockServiceCategory, MockPriceInfo, MockScheduleInfo, MockServant, MockAgreementInfo, MockMetaTag;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceItem = jasmine.createSpy('MockServiceItem');
            MockServiceOptionInfo = jasmine.createSpy('MockServiceOptionInfo');
            MockDiscountInfo = jasmine.createSpy('MockDiscountInfo');
            MockLocationInfo = jasmine.createSpy('MockLocationInfo');
            MockAlbumInfo = jasmine.createSpy('MockAlbumInfo');
            MockServiceCapacityInfo = jasmine.createSpy('MockServiceCapacityInfo');
            MockServiceCategory = jasmine.createSpy('MockServiceCategory');
            MockPriceInfo = jasmine.createSpy('MockPriceInfo');
            MockScheduleInfo = jasmine.createSpy('MockScheduleInfo');
            MockServant = jasmine.createSpy('MockServant');
            MockAgreementInfo = jasmine.createSpy('MockAgreementInfo');
            MockMetaTag = jasmine.createSpy('MockMetaTag');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceItem': MockServiceItem,
                'ServiceOptionInfo': MockServiceOptionInfo,
                'DiscountInfo': MockDiscountInfo,
                'LocationInfo': MockLocationInfo,
                'AlbumInfo': MockAlbumInfo,
                'ServiceCapacityInfo': MockServiceCapacityInfo,
                'ServiceCategory': MockServiceCategory,
                'PriceInfo': MockPriceInfo,
                'ScheduleInfo': MockScheduleInfo,
                'Servant': MockServant,
                'AgreementInfo': MockAgreementInfo,
                'MetaTag': MockMetaTag
            };
            createController = function() {
                $injector.get('$controller')("ServiceItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tikonApp:serviceItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
