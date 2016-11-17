(function () {
    'use strict';

    function ServiceItemSchedulesController($scope, $state, TimingInfo, ScheduleInfoCustom, ScheduleInfoSearch, ScheduleInfo, ParseLinks, AlertService, paginationConstants, translate, translatePartialLoader) {
        var $ctrl = this;
        var pagingParams = {};
        $ctrl.loadPage = loadPage;
        $ctrl.selectedId = '';
        $ctrl.formState = 'list';
        $ctrl.newItem = newItem;
        $ctrl.editItem = editItem;
        $ctrl.backToList = backToList;
        $ctrl.generateTimeInfo = generateTimeInfo;
        $ctrl.showTimingInfo=showTimingInfo;
        $ctrl.backToScheduleInfo=backToScheduleInfo;
        $ctrl.step=0;


        loadAll();

        loadPage();
        function  showTimingInfo()
        {
            $ctrl.timingInfo=$ctrl.selectedScheduleInfo.timingInfo;
            $ctrl.step=1;
        }
        function generateTimeInfo() {


             TimingInfo.generateTimingInfo.save($ctrl.selectedScheduleInfo,function(timingInfo){
                 debugger;
                 $ctrl.timingInfo=timingInfo;$ctrl.selectedScheduleInfo.timingInfo=timingInfo; });
            $ctrl.step=1;
        }
        function backToScheduleInfo()
        {
            $ctrl.step=0;
        }

        function loadAll() {


            function onSuccess(data, headers) {

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            translatePartialLoader.addPart('scheduleInfo');
            translatePartialLoader.addPart('global');
            translatePartialLoader = translate.refresh();
            if ($ctrl.serviceItemId == undefined || $ctrl.serviceItemId == null)$ctrl.scheduleInfos = [];
            else
                $ctrl.scheduleInfos = ScheduleInfoCustom.primaryScheduleInfosByServiceItem($ctrl.serviceItemId).query();


        }

        function newItem() {
            $ctrl.formState = 'new';
            $ctrl.selectedScheduleInfo = {};
        }

        function editItem(id) {
            debugger;
            $ctrl.formState = 'edit';
            $ctrl.selectedId = id;
            $ctrl.selectedScheduleInfo = ScheduleInfo.get({id: id.id});
        }

        function backToList() {

            $ctrl.formState = 'list';
            $ctrl.selectedScheduleId = '';
            if ($ctrl.serviceItemId == undefined || $ctrl.serviceItemId == null)$ctrl.scheduleInfos = [];
            else
                $ctrl.scheduleInfos = ScheduleInfoCustom.primaryScheduleInfosByServiceItem($ctrl.serviceItemId).query();
        }



    }

    var ServiceItemSchedules = {
        // template:'tets:{{$ctrl.testStr}}',
        templateUrl: 'app/components/partial-forms/service-item-schedules/partial-form.service-item-schedules.html',
        controller: ServiceItemSchedulesController,
        bindings: {
            serviceItemId: '<',
            selectedScheduleInfo: '='
            /*  items :'<',
             displaytag:'@?',
             displayitem:'@?',
             list:'='*/

        }

    };

    angular
        .module('tikonApp')
        .component('pfServiceItemSchedules', ServiceItemSchedules)

    //  ServiceItemSchedulesController.$inject = ['$scope','$attrs','$element'];
    ServiceItemSchedulesController.$inject = ['$scope', '$state', 'TimingInfo', 'ScheduleInfoCustom', 'ScheduleInfoSearch', 'ScheduleInfo', 'ParseLinks', 'AlertService', 'paginationConstants', '$translate', '$translatePartialLoader'];


})();

