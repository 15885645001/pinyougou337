app.controller("contentController",function($scope,contentService){
	$scope.contentList = [];
	// 根据分类ID查询广告的方法:
	//入参: categoryId即分类id  1为轮播图  2为今日推荐  ...
	$scope.findByCategoryId = function(categoryId){
		contentService.findByCategoryId(categoryId).success(function(response){
			//console.log(response);
			$scope.contentList[categoryId] = response;
		});
	}
	
	//搜索  （传递参数）  #?代表页面之间的交互
	$scope.search=function(){
		location.href="http://localhost:9103/search.html#?keywords="+$scope.keywords;
	}
	
});