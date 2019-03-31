app.controller("indexController",function($scope,loginService){
	
	$scope.showName = function(){
		loginService.showName().success(function(response){
			$scope.loginName = response.username;//返回值: Map 通用
			$scope.time = response.currentTime;
		});
	}
	
});