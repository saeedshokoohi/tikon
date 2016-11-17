var DAYS_NAMES = ['شنبه', 'یکشنبه', 'دوشنبه', 'سه‌شنبه', 'چهارشنبه', 'پنج‌شنبه', 'جمعه'];
var DAYS_VALUES = [ 'SATURDAY','SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY'];
(function () {
    'use strict';

    function WeeklyWorkDaysController($scope, $state, translate, translatePartialLoader) {
        var $ctrl = this;

        $ctrl.checkedDays=checkAllAsDefault();
        $ctrl.checkedDays = getCheckedDays();
        $ctrl.checkChanged=onCheckChanged;
        $ctrl.allWeekDays = getAllWeekDays();
        //
        $scope.$watch('$ctrl.selectedWeeklyScheduleInfo',function(){
     //       if($ctrl.selectedWeeklyScheduleInfo.weekdays!=undefined) {
                $scope.$watch('$ctrl.selectedWeeklyScheduleInfo.weekdays', function () {

                    $ctrl.checkedDays = getCheckedDays();
                    $ctrl.allWeekDays = getAllWeekDays();

                },true);
       //     }


        },true);
        function onCheckChanged()
        {


            setCheckedDays();
        }
        function checkAllAsDefault()
        {
            var retChekDays=[];
            angular.forEach(DAYS_VALUES, function (val) {
                retChekDays.push(val);
            });
            return retChekDays;
        }
        function getCheckedDays() {


            if($ctrl.selectedWeeklyScheduleInfo!=undefined && $ctrl.selectedWeeklyScheduleInfo.weekdays!=undefined ) {
                var weekDays = $ctrl.selectedWeeklyScheduleInfo.weekdays;
                if(weekDays.length>0) {
                    var retChekDays=[];
                    angular.forEach(weekDays, function (val) {
                        retChekDays.push(val.weekday);
                    });
                    return retChekDays;

                }
            }
           return $ctrl.checkedDays;
        }
        function setCheckedDays() {

            $ctrl.selectedWeeklyScheduleInfo.weekdays=[];
              angular.forEach( $ctrl.allWeekDays,function(val){
                  if(val.checked) {

                      $ctrl.selectedWeeklyScheduleInfo.weekdays.push({weekday:val.value});
                  }
              });


        }

        function getAllWeekDays() {
            var retList = [];

            for (var i = 0; i < 7; i++) {
                var hasChecked = $ctrl.checkedDays.indexOf(DAYS_VALUES[i])>-1;
                var weekDay = {value: DAYS_VALUES[i], name: DAYS_NAMES[i], checked: hasChecked};
                retList.push(weekDay);
            }
            return retList;
        }


    }

    var WeeklyWorkDays = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/weekly-work-days/partial-form.weekly-work-days.html',
        controller: WeeklyWorkDaysController,
        bindings: {
            selectedWeeklyScheduleInfo: '=?'
        }

    };

    angular
        .module('tikonApp')
        .component('pfWeeklyWorkDays', WeeklyWorkDays)

    //  ServiceItemSchedulesController.$inject = ['$scope','$attrs','$element'];
    WeeklyWorkDaysController.$inject = ['$scope', '$state', '$translate', '$translatePartialLoader'];


})();

