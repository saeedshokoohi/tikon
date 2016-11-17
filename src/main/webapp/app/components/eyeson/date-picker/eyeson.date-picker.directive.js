(function () {
    'use strict';
    EO_PersianDatePicker.$inject = ['$scope', '$attrs', '$element'];
    function EO_PersianDatePicker($scope, $attrs, $element) {
//debugger;
        var $ctrl = this;
        $ctrl.makeOffDateListFormat=function(offDateList)
        {
            var offDateListAsString='';
            if($ctrl.offDateList && $ctrl.offDateList!=undefined && Array.isArray( $ctrl.offDateList))
                offDateListAsString=offDateList.join();
            return offDateListAsString;

        }
        $ctrl.offDays=$ctrl.makeOffDateListFormat($ctrl.offDateList);

        $ctrl.today = function() {
            $ctrl.dateValue = new Date();
        };
      //  $ctrl.today();

        $ctrl.clear = function () {
            $ctrl.dateValue = null;
        };


        // Disable weekend selection
        $ctrl.disabled = function(date, mode) {
            var hasoffDaysOfWeek=($ctrl.offDaysOfWeek && $ctrl.offDaysOfWeek!=undefined && Array.isArray( $ctrl.offDaysOfWeek));
            return ( mode === 'day' && hasoffDaysOfWeek &&   $ctrl.offDaysOfWeek.indexOf(date.getDay())>=0);
        };

        //$ctrl.toggleMin = function() {
        //    $ctrl.minDate = $ctrl.minDate ? null : new Date();
        //};
     //   $ctrl.toggleMin();

        $ctrl.openPersian = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $ctrl.persianIsOpen = true;
            $ctrl.gregorianIsOpen = false;
        };
        $ctrl.openGregorian = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $ctrl.gregorianIsOpen = true;
            $ctrl.persianIsOpen = false;
        };

        $ctrl.dateOptions = {
            formatYear: 'yy',
            startingDay: 6

        };

        $ctrl.initDate =new Date();
        $ctrl.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        $ctrl.format = $ctrl.formats[1];
        $ctrl.showWeeks=false;

    }




    var EO_PersianDatePicker = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/eyeson/date-picker/eyeson.date-picker.template.html',
        controller: EO_PersianDatePicker,
        bindings: {
            dateValue: '=',
            offDateList:'<?',
            minDate:'<?',
            maxDate:'<?',
            offDaysOfWeek:'<?'

            /*  items :'<',
             displaytag:'@?',
             displayitem:'@?',
             list:'='*/

        }

    };

    angular
        .module('tikonApp')
        .component('eoDatePicker', EO_PersianDatePicker)


})();


//
//angular.module('tikonApp').directive('eoDatePicker', function() {
//    return {
//        restrict: 'E',
//        scope: false,
//        transclude: true,
//        templateUrl: 'app/components/eyeson/date-picker/eyeson.date-picker.template.html',
//
//        link: function ($scope, $element, $attrs, $controller) {
//            $scope.today = function() {
//                $scope.dt = new Date();
//            };
//            $scope.today();
//
//            $scope.clear = function () {
//                $scope.dt = null;
//            };
//
//            // Disable weekend selection
//            $scope.disabled = function(date, mode) {
//                return ( mode === 'day' &&date.getDay() === 5  );
//            };
//
//            $scope.toggleMin = function() {
//                $scope.minDate = $scope.minDate ? null : new Date();
//            };
//            $scope.toggleMin();
//
//            $scope.openPersian = function($event) {
//                $event.preventDefault();
//                $event.stopPropagation();
//
//                $scope.persianIsOpen = true;
//                $scope.gregorianIsOpen = false;
//            };
//            $scope.openGregorian = function($event) {
//                $event.preventDefault();
//                $event.stopPropagation();
//
//                $scope.gregorianIsOpen = true;
//                $scope.persianIsOpen = false;
//            };
//
//            $scope.dateOptions = {
//                formatYear: 'yy',
//                startingDay: 6
//            };
//
//            $scope.initDate =new Date();
//            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
//            $scope.format = $scope.formats[1];
//            $scope.showWeeks=false;
//
//        }
//    }
//});
