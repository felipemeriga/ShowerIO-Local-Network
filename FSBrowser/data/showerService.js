app.service('showerService', function ($http) {

    this.changeSelectDurationTime = function (time) {
        $http({
            method: 'POST',
            url: '/selectDurationTime?time=' + time
        }).then(function successCallback(response) {
            console.log('succes! Setting the Shower Duration');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.setActualShowerTimePlus = function (plusTime) {
        $http({
            method: 'POST',
            url: '/setActualShowerTimePlus?plusTime=' + plusTime
        }).then(function successCallback(response) {
            console.log('succes! Setting the Plus Duration Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.setActualShowerTimeLess = function (lessTime) {
        $http({
            method: 'POST',
            url: '/setActualShowerTimeLess?lessTime=' + lessTime
        }).then(function successCallback(response) {
            console.log('succes! Setting the Less Duration Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.changeSelectedOffTime = function (time) {
        $http({
            method: 'POST',
            url: '/selectOffTime?time=' + time
        }).then(function successCallback(response) {
            console.log('succes! Setting the Off time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.setActualOffTimePlus = function (plusTime) {
        $http({
            method: 'POST',
            url: '/setActualOffTimePlus?plusTime=' + plusTime
        }).then(function successCallback(response) {
            console.log('succes! Setting the Plus Off Duration Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.setActualOffTimeLess = function (lessTime) {
        $http({
            method: 'POST',
            url: '/setActualOffTimeLess?lessTime=' + lessTime
        }).then(function successCallback(response) {
            console.log('succes! Setting the Less Off Duration Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.changeSelectedPausedTime = function (time) {
        $http({
            method: 'POST',
            url: '/selectPausedTime?time=' + time
        }).then(function successCallback(response) {
            console.log('succes! Selecting the Paused Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.setActualPausedTimePlus = function (plusTime) {
        $http({
            method: 'POST',
            url: '/setActualPausedTimePlus?plusTime=' + plusTime
        }).then(function successCallback(response) {
            console.log('succes! Setting the Plus Paused Duration Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.setActualPausedTimeLess = function (lessTime) {
        $http({
            method: 'POST',
            url: '/setActualPausedTimeLess?lessTime=' + lessTime
        }).then(function successCallback(response) {
            console.log('succes! Setting the Less Paused Duration Time of the Bath');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };

    this.resetWifiManagerSettings = function () {
        $http({
            method: 'POST ',
            url: '/reset'
        }).then(function successCallback(response) {
            console.log('succes! Reseting the Wifi credentials');

        }, function errorCallback(response) {
            console.log('The current HTTP request could no be processed');
        });

    };
    this.getDurationTime = function () {
        return $http({
            method: 'GET',
            url: '/getDurationTime'
        });
    };
	
	this.getOffTime = function () {
        return $http({
            method: 'GET',
            url: '/getOffTime'
        });
    };
	
	this.getPausedTime = function () {
        return $http({
            method: 'GET',
            url: '/getPausedTime'
        });
    };

});