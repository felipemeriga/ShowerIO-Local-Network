app.controller('showerController', function ($scope, showerService, $q) {

    $scope.selectedDurationTime = '';
    $scope.selectedOffTime = '';
    $scope.selectedPausedTime = '';

    $scope.changeSelectDurationTime = function () {
        console.log($scope.selectedDurationTime);
        showerService.changeSelectDurationTime($scope.selectedDurationTime);
    };

    $scope.setActualShowerTimePlus = function (plusTime) {
        console.log(plusTime);
        showerService.setActualShowerTimePlus(plusTime);
    };

    $scope.setActualShowerTimeLess = function (lessTime) {
        console.log(lessTime);
        showerService.setActualShowerTimeLess(lessTime);
    };

    $scope.changeSelectedOffTime = function () {
        console.log($scope.selectedOffTime);
        showerService.changeSelectedOffTime($scope.selectedOffTime);
    };

    $scope.setActualOffTimePlus = function (plusTime) {
        console.log(plusTime);
        showerService.setActualOffTimePlus(plusTime);
    };

    $scope.setActualOffTimeLess = function (lessTime) {
        console.log(lessTime);
        showerService.setActualOffTimeLess(lessTime);
    };

    $scope.changeSelectedPausedTime = function () {
        console.log($scope.selectedPausedTime);
        showerService.changeSelectedPausedTime($scope.selectedPausedTime);
    };

    $scope.setActualPausedTimePlus = function (plusTime) {
        console.log(plusTime);
        showerService.setActualPausedTimePlus(plusTime);
    };

    $scope.setActualPausedTimeLess = function (lessTime) {
        console.log(lessTime);
        showerService.setActualPausedTimeLess(lessTime);
    };

    $scope.resetWifiManagerSettings = function () {
        console.log('Reseting Wifi saved credentials');
        showerService.resetWifiManagerSettings();
    }

    $scope.getDurationTime = function () {
        var d = $q.defer();
        showerService.getDurationTime().then(function (response) {
            d.resolve(response);
        });
        return d.promise;
    }


    $scope.setDurationTime = function (response) {
        if (response.status == 200 || response.status == 204) {
            $scope.selectedDurationTime = response.data;
        }
    }
	
	$scope.getOffTime = function () {
        var d = $q.defer();
        showerService.getOffTime().then(function (response) {
            d.resolve(response);
        });
        return d.promise;
    }
	
	$scope.setOffTime = function (response) {
        if (response.status == 200 || response.status == 204) {
            $scope.selectedOffTime = response.data;
        }
    }
	
	$scope.getPausedTime = function () {
        var d = $q.defer();
        showerService.getPausedTime().then(function (response) {
            d.resolve(response);
        });
        return d.promise;
    }
	
	$scope.setPausedTime = function (response) {
        if (response.status == 200 || response.status == 204) {
            $scope.selectedPausedTime = response.data;
        }
    }

    $q.all([
        $scope.getDurationTime(),
		$scope.getOffTime,
		$scope.getPausedTime
		
    ]).then(function (response) {
        $scope.setDurationTime(response[0]);
		$scope.setOffTime(response[1]);
		$scope.setPausedTime(response[2]);
    });

});



