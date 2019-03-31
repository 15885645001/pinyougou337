//注: 只有控制器有scope域, 控制器和视图之间是通过数据视图双向绑定的, 控制器和服务层之间是通过依赖注入的
//自定义控制器
//brandService是作为依赖注入而注进来
app.controller("brandController",function($controller,$scope,brandService){
    //继承父的  要指定父的名字
    //{$scope:$scope}: 代表继承父的内容
    $controller("baseController",{$scope:$scope});

    //查询所有的品牌
    $scope.findAll = function () {
        //发请求
        brandService.findAll().success(

            function (response) {
                $scope.list = response;

                //注: 普通查询, 加上 ng-init="findAll()" 初始化数据

            }
        )

    };



    //初始化数据
    $scope.searchEntity = {};
    //根据条件查询分页对象  传入的参数: 当前页  每页的条数  条件查询: 条件可能是多个 , 所以为条件对象 post请求
    $scope.search = function (currentPage,itemsPerPage) {
        //发请求
       brandService.search(currentPage,itemsPerPage,$scope.searchEntity).success(
            function(response) {
                //每页展示的数据
                $scope.list = response.rows;
                //总条数
                $scope.paginationConf.totalItems = response.total;

            }
        )
    };

    //查询分页对象是公共的, 可以进行抽取  因为根据条件进行查询的也是查询分页对象
    //传入的参数: 当前页  每页的条数
    $scope.findAllByPage = function (currentPage,itemsPerPage) {
        //当前页 (传入的参数)
        //每页显示条数 (传入的参数)
        //  上面两项都是从前端传递到后端, 即不是由后端决定的
        //URL  自定义的
        //返回值：分页对象（总条数 结果集--即每页展示的数据  当前页和每页显示的条数都是从前端传来的,已展示,即不需要返回）
        brandService.findAllByPage(currentPage,itemsPerPage).success(
            function(response){//response代表的就是PageResult
                //每页展示的数据
                $scope.list = response.rows;
                //总条数
                $scope.paginationConf.totalItems = response.total;//更新总条数

                //注: 分页查询时 本身具有初始化数据, 因此要去掉 ng-init 指令的调用
            }

        )

    };


    //保存品牌  又要做修改品牌   区别是有没有id
    $scope.save = function(){
        //根据区别id进行判断  默认是保存
        var methodName = brandService.save($scope.entity);

        if ($scope.entity.id != null){
            methodName = brandService.update($scope.entity);
        }

        //发请求
        methodName.success(
            function (response) {

                //判断是否成功
                if(response.flag){
                    //刷新页面
                    //提示
                    alert(response.message);
                    $scope.reloadList();//重新加载
                }else{
                    //提示
                    alert(response.message);
                }
            }
        )
    };

    //根据id查询一个品牌并回显
    $scope.findById = function(id){
        brandService.findById(id).success(
            function(response){
                $scope.entity = response;
            }
        )
    };



    //删除
    $scope.delete = function () {
        //发请求
        brandService.delete($scope.selectedIds).success(
            function (response) {

                //判断是否成功
                if(response.flag){
                    //刷新页面
                    //提示
                    alert(response.message);
                    $scope.reloadList();//重新加载
                }else{
                    //提示
                    alert(response.message);
                }
            }
        )
    }

});
