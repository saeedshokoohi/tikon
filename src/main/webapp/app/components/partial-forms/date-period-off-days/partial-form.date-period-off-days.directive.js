(function () {
    'use strict';

    function DatePeriodOffDaysController($scope, $state, translate, translatePartialLoader) {
        var $ctrl = this;
        $ctrl.newDate = null;
        $ctrl.offDates=getOffDates();
        $ctrl.onAdddate = onAdddate;
        $ctrl.onRemoveDate=onRemoveDate;

        Date.prototype.toLocalISOString = function() {
            var mm = this.getMonth() + 1; // getMonth() is zero-based
            var dd = this.getDate();
            if(mm<10)mm='0'+mm;
            if(dd<10)dd='0'+dd;

            return [this.getFullYear(),mm, dd].join('-'); // padding
        };


        function setOffDate()
        {
            debugger;
            if($ctrl.selectedDatePeriod!=undefined)
            {
                $ctrl.selectedDatePeriod.offdays=[];
            angular.forEach($ctrl.offDates,function(val){
                var dateTemp=new Date(val.value);
                if(dateTemp)
                    $ctrl.selectedDatePeriod.offdays.push({offDate:dateTemp.toLocalISOString()});
            });
            }
        }
        function onAdddate() {
            if ($ctrl.newDate != null) {

                var persianDate = new JDate($ctrl.newDate);
                var hasAddedBefore=false;
                angular.forEach($ctrl.offDates,function(val){
                    if(val.value.toLocaleString()==$ctrl.newDate.toLocaleString())hasAddedBefore=true;
                });
                if(!hasAddedBefore)
                $ctrl.offDates.push({name: persianDate.format('DD/ MMMM/YYYY'), value: $ctrl.newDate});
            }
            setOffDate();

        }
        function onRemoveDate( date) {
            if($ctrl.offDates!=undefined)
            $ctrl.offDates = $ctrl.offDates.filter(function(item) {
                return item.value !== date.value;
            });
            setOffDate();

        }

        $scope.$watch('$ctrl.selectedDatePeriod',function(){
         //   if( $ctrl.selectedDatePeriod.offdays!=undefined) {
                $scope.$watch('$ctrl.selectedDatePeriod.offdays', function () {

                    $ctrl.offDates = getOffDates();

                },true);
         //   }


        },true);
        function getOffDates() {
            var retOffDates = [];

            if ($ctrl.selectedDatePeriod != undefined && $ctrl.selectedDatePeriod.offdays != undefined) {
                var offDays = $ctrl.selectedDatePeriod.offdays;
                angular.forEach(offDays, function (val) {
                    var persianDate = new JDate(new Date(val.offDate));
                    retOffDates.push({name: persianDate.format('DD/ MMMM/YYYY'), value: val.offDate});
                });
            }
            return retOffDates;
        }

    }

    var DatePeriodOffDays = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/date-period-off-days/partial-form.date-period-off-days.html',
        controller: DatePeriodOffDaysController,
        bindings: {
            selectedDatePeriod: '=?'
        }

    };

    angular
        .module('tikonApp')
        .component('pfDatePeriodOffDays', DatePeriodOffDays)

    //  ServiceItemSchedulesController.$inject = ['$scope','$attrs','$element'];
    DatePeriodOffDaysController.$inject = ['$scope', '$state', '$translate', '$translatePartialLoader'];


})();

