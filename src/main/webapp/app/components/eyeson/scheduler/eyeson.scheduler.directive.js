
angular.module('tikonApp').directive('eoScheduler', function () {
    return {
        restrict: 'A',
        scope: {timingInfo: '=?'},
        transclude: true,
        templateUrl: 'app/components/eyeson/scheduler/eyeson.scheduler.template.html',

        link: function ($scope, $element, $attrs, $controller) {
            var scheduleIsLoaded=false;
            //adjust size of a scheduler
            $scope.$watch(function () {
                return $element[0].offsetWidth + "." + $element[0].offsetHeight;
            }, function () {
                scheduler.setCurrentView();
            });
            $scope.$watch('timingInfo', function (timingInfo) {
                if(!scheduleIsLoaded)
                regenerateEvents(timingInfo);
            });
            initSchedule();
            function regenerateEvents(timingInfo) {

              //  debugger;
                var first_hour=0;
                var last_hour=23;
                var limit_start=new Date('2000-01-01');
                var limit_end=new Date('3000-01-01');
                if(timingInfo && timingInfo.startTime)
                    first_hour=(new Date(timingInfo.startTime)).getHours();
                if(timingInfo && timingInfo.endTime)
                    last_hour=(new Date(timingInfo.endTime)).getHours();
                if(timingInfo && timingInfo.fromDate)
                limit_start=new Date(timingInfo.fromDate);
                if(timingInfo && timingInfo.toDate)
                    limit_end=new Date(timingInfo.toDate);
                scheduler.config.first_hour =first_hour ;
                scheduler.config.last_hour = last_hour;
                scheduler.config.limit_start = limit_start;
                scheduler.config.limit_end = limit_end;
                scheduler.config.limit_view = true;





                clearSchedule();
                if (timingInfo != undefined && timingInfo.availableTimes != undefined) {
                   //adding offtimes
                    angular.forEach(timingInfo.offDates, function (date) {

                        var eid = scheduler.addMarkedTimespan({
                            days:    new Date(date),
                           // end_date:    new Date(date),
                            //  type: "dhx_time_block"
                            type: "dhx_offTime_block",
                            css:"sch-offDate",
                            zones:"fullday"
                            }
                        );
                    });
                    angular.forEach(timingInfo.offDaysOfWeek, function (day) {
                        debugger;
                          var offdays=[];
                        offdays.push(convertWeekNameToOrder(day));
                        var eid = scheduler.addMarkedTimespan({
                                days:   offdays,
                                // end_date:    new Date(date),
                            //  type: "dhx_time_block"
                            type: "dhx_offTime_block",
                            css:"sch-offDate",
                                zones:"fullday"
                            }
                        );
                    });
                    angular.forEach(timingInfo.offTimes, function (time) {
                      //  debugger;
                        var zones=[];
                        var startTime=new Date(time.startTime);
                        var endTime=new Date(time.endTime);
                        var startMin=startTime.getMinutes();
                        var endMin=endTime.getMinutes();

                        zones.push(startTime.getHours()*60+startMin);
                        zones.push(endTime.getHours()*60+endMin);
                        console.log(zones);
                        var eid = scheduler.addMarkedTimespan({
                                zones:zones,
                            days:"fullweek",
                            css:"sch-offDate",
                                // end_date:    new Date(date),
                              //  type: "dhx_time_block"
                                type: "dhx_offTime_block"

                            }
                        );
                    });
                    scheduler.updateView();
                    //adding events
                    var events=[];
                    angular.forEach(timingInfo.availableTimes, function (time) {
                        debugger;
                        var eid = scheduler.addEvent(
                            new Date(time.startTime),
                            new Date(time.endTime),
                            time.comment,   //userdata
                            time.id
                        );
                        time.id=eid;
                    //    events.push(time);
                      //  console.log('event added : ' + eid);

                    });

                    scheduler.updateView();
                    scheduleIsLoaded=true;
                }
            }
            function  onEventCreated(id,e)
            {
                refreshEvents();
            }
            function onEventDeleted(id,e) {
                refreshEvents();
            }

            function onEventChanged() {
                refreshEvents();
            }

            function onEventAdded() {
                refreshEvents();
            }
            function refreshEvents()
            {
                console.log('refresh called');
                if(scheduleIsLoaded) {
                    debugger;
                    var evs = scheduler.getEvents();
                    var evsDto = [];

                    angular.forEach(evs, function (e) {
                        debugger;
                        var newE = {
                            startTime: e.start_date,
                            endTime: e.end_date,
                            id: e.id,
                            comment: e.text
                        };
                        evsDto.push(newE);
                    });
                    $scope.timingInfo.availableTimes = evsDto;
                }

            }
            function convertWeekNameToOrder(day) {
                var index=DAYS_VALUES.indexOf(day);
                if(index==0)return 6;
                return index-1;

            }
            function clearSchedule() {
                scheduler.clearAll();
                scheduler.deleteMarkedTimespan();
            }

            function initSchedule() {
                //styling for dhtmlx scheduler
                $element.addClass("dhx_cal_container");

                scheduler.config.start_on_monday = false;
                //init scheduler
                // debugger;
                scheduler.init($element[0], new JDate(), "week");

                scheduler.attachEvent("onEventCreated",onEventCreated );
                scheduler.attachEvent("onEventAdded",onEventAdded );
                scheduler.attachEvent("onEventDeleted",onEventDeleted );
                scheduler.attachEvent("onEventChanged",onEventChanged );


                scheduler.updateView();
            }
        }
    }
});
