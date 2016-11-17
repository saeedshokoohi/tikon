(function () {
    'use strict';

    function TimePeriodOffTimesController($scope, $state, translate, translatePartialLoader, AlertService) {
        var $ctrl = this;

        $ctrl.offTimes = getOfftimes();
        $ctrl.onAddtime = onAddtime;
        $ctrl.onRemovetime = onRemovetime;


        function setOffTime() {
            if ($ctrl.selectedTimePeriod != undefined) {
                $ctrl.selectedTimePeriod.offtimes = [];
                angular.forEach($ctrl.offTimes, function (val) {
                    $ctrl.selectedTimePeriod.offtimes.push({fromTime: val.fromTime, toTime: val.toTime});
                });
            }
        }

        function onAddtime() {
            debugger;
            if ($ctrl.fromTime != null && $ctrl.toTime != null) {


                var isValid = true;
                angular.forEach($ctrl.offTimes, function (val) {
                    if (timesHasConflict(val, {fromTime: $ctrl.fromTime, toTime: $ctrl.toTime}))
                        isValid = false;
                });
                if (isValid) {
                    $ctrl.offTimes.push({
                        name: TimeDurationAsString({fromTime: $ctrl.fromTime, toTime: $ctrl.toTime}),
                        fromTime: $ctrl.fromTime,
                        toTime: $ctrl.toTime
                    });

                }
            }
            setOffTime();

        }
        $scope.$watch('$ctrl.selectedTimePeriod',function(){
            //   if( $ctrl.selectedDatePeriod.offdays!=undefined) {
            $scope.$watch('$ctrl.selectedTimePeriod.offtimes', function () {

                $ctrl.offTimes = getOfftimes();

            },true);
            //   }


        },true);
        function timesHasConflict(t1, t2) {
            var hasConflict = ((t1.fromTime > t1.toTime ) || (t2.fromTime > t2.toTime ) || (t1.fromTime < t2.fromTime && t2.fromTime < t1.toTime) ||
            (t1.fromTime < t2.toTime && t2.toTime < t1.toTime)
            );
            if (hasConflict)AlertService.warning('در زمانهای اضاقه شده تداخل وجود دارد');
            return hasConflict;
        }

        function onRemovetime(date) {
            if ($ctrl.offTimes != undefined)
                $ctrl.offTimes = $ctrl.offTimes.filter(function (item) {
                    debugger;
                    return item.fromTime !== date.fromTime || item.toTime !== date.toTime;
                });
            setOffTime();

        }

        function getOfftimes() {
            debugger;
            var retoffTimes = [];

            if ($ctrl.selectedTimePeriod != undefined && $ctrl.selectedTimePeriod.offtimes != undefined) {
                var offtimes = $ctrl.selectedTimePeriod.offtimes;
                angular.forEach(offtimes, function (val) {
                    var persianDate = TimeDurationAsString({fromTime: new Date(val.fromTime), toTime:new Date( val.toTime)});
                    retoffTimes.push({name: persianDate, fromTime: new Date(val.fromTime), toTime: new Date(val.toTime)});
                });
            }
            return retoffTimes;
        }

        function TimeDurationAsString(duration) {
            return duration.fromTime.getHours() + ':' + duration.fromTime.getMinutes() + ' - ' + duration.toTime.getHours() + ':' + duration.toTime.getMinutes();
        }

    }

    var TimePeriodOffTimes = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/time-period-off-times/partial-form.time-period-off-times.html',
        controller: TimePeriodOffTimesController,
        bindings: {
            selectedTimePeriod: '=?'
        }

    };

    angular
        .module('tikonApp')
        .component('pfTimePeriodOffTimes', TimePeriodOffTimes)

    //  ServiceItemSchedulesController.$inject = ['$scope','$attrs','$element'];
    TimePeriodOffTimesController.$inject = ['$scope', '$state', '$translate', '$translatePartialLoader', 'AlertService'];


})();

