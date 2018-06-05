app.service('showerService', function($http){

		this.changeSelectDurationTime = function(time){
			$http({
			  method: 'GET',
			  url: '/selectDurationTime?time=' + time
			}).then(function successCallback(response) {
				console.log('succes! Setting the Shower Duration');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.setActualShowerTimePlus = function(plusTime){
			$http({
			  method: 'GET',
			  url: '/setActualShowerTimePlus?plusTime=' + plusTime
			}).then(function successCallback(response) {
				console.log('succes! Setting the Plus Duration Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.setActualShowerTimeLess = function(lessTime){
			$http({
			  method: 'GET',
			  url: '/setActualShowerTimeLess?lessTime=' + lessTime
			}).then(function successCallback(response) {
				console.log('succes! Setting the Less Duration Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.changeSelectedOffTime = function(time){
			$http({
			  method: 'GET',
			  url: '/selectOffTime?time=' + time
			}).then(function successCallback(response) {
				console.log('succes! Setting the Off time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.setActualOffTimePlus = function(plusTime){
			$http({
			  method: 'GET',
			  url: '/setActualOffTimePlus?plusTime=' + plusTime
			}).then(function successCallback(response) {
				console.log('succes! Setting the Plus Off Duration Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.setActualOffTimeLess = function(lessTime){
			$http({
			  method: 'GET',
			  url: '/setActualOffTimeLess?lessTime=' + lessTime
			}).then(function successCallback(response) {
				console.log('succes! Setting the Less Off Duration Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.changeSelectedPausedTime = function(time){
			$http({
			  method: 'GET',
			  url: '/selectPausedTime?time=' + time
			}).then(function successCallback(response) {
				console.log('succes! Selecting the Paused Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.setActualPausedTimePlus = function(plusTime){
			$http({
			  method: 'GET',
			  url: '/setActualPausedTimePlus?plusTime=' + plusTime
			}).then(function successCallback(response) {
				console.log('succes! Setting the Plus Paused Duration Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};
		
		this.setActualPausedTimeLess = function(lessTime){
			$http({
			  method: 'GET',
			  url: '/setActualPausedTimeLess?lessTime=' + lessTime
			}).then(function successCallback(response) {
				console.log('succes! Setting the Less Paused Duration Time of the Bath');
				
			  }, function errorCallback(response) {
				console.log('The current HTTP request could no be processed');
			  });
		
		};

});