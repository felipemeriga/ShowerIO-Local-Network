app.controller('showerController', function($scope, showerService) {
	
  $scope.selectedDurationTime = '';
  $scope.selectedOffTime = '';
  $scope.selectedPausedTime = '';
  
  $scope.changeSelectDurationTime = function(){
	console.log($scope.selectedDurationTime);
	showerService.changeSelectDurationTime($scope.selectedDurationTime);
  };
  
  $scope.setActualShowerTimePlus = function(plusTime){
	  console.log(plusTime);
	  showerService.setActualShowerTimePlus(plusTime);
  };
  
  $scope.setActualShowerTimeLess = function(lessTime){
	  console.log(lessTime);
	  showerService.setActualShowerTimeLess(lessTime);
  };
  
  $scope.changeSelectedOffTime = function(){
	console.log($scope.selectedOffTime);
	showerService.changeSelectedOffTime($scope.selectedOffTime);
  };
  
  $scope.setActualOffTimePlus = function(plusTime){
	  console.log(plusTime);
	  showerService.setActualOffTimePlus(plusTime);
  };
  
  $scope.setActualOffTimeLess = function(lessTime){
	  console.log(lessTime);
	  showerService.setActualOffTimeLess(lessTime);
  };
  
  $scope.changeSelectedPausedTime = function(){
	console.log($scope.selectedPausedTime);
	showerService.changeSelectedPausedTime($scope.selectedPausedTime);  
  };
  
  $scope.setActualPausedTimePlus = function(plusTime){
	  console.log(plusTime);
	  showerService.setActualPausedTimePlus(plusTime);
  };
  
  $scope.setActualPausedTimeLess = function(lessTime){
	  console.log(lessTime);
	  showerService.setActualPausedTimeLess(lessTime);
  };
  
});



