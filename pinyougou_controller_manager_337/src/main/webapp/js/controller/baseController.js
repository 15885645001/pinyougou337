//父控制器
app.controller("baseController",function($scope){
    //分页控件配置
    //onChange : 改变事件(更改页面时触发事件)
    $scope.paginationConf = {
        currentPage: 1, //当前页
        totalItems: 0,  //总条数
        itemsPerPage: 5,  //每页显示的条数
        perPageOptions: [5,10, 15, 20],  //每页显示多少条下拉列表
        onChange: function(){ // 当页码、每页显示多少条下拉列表发生变化的时候，自动触发了
            $scope.reloadList();//重新加载
        }
    };
    //重新加载
    $scope.reloadList = function(){
        //查询分页对象
        //$scope.findAllByPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };


    //定义数组 将其放到域中
    $scope.selectedIds = [];

    //选择或取消选择复选框
    $scope.updateSelected = function ($event,id) {
        //判断
        if($event.target.checked){
            //复选框选中, 把id存到数组当中
            $scope.selectedIds.push(id);
        }else{
            //取消选择复选框 根据索引
            //获取索引
            var index = $scope.selectedIds.indexOf(id);

            //取消  因为这里id是唯一的, 所以只有1个
            $scope.selectedIds.splice(index,1);
        }

        //测试打印
        console.log($scope.selectedIds);
    };


    // 定义方法：获取JSON字符串中的某个key对应值的集合
        //jsonStr :  [{"id":41,"text":"奥义"},{"id":42,"text":"金啦啦"}]  类型 字符串  格式 json
        //key : 'text'  取出上面json格式字符串中的键对应的值
    $scope.jsonToString = function(jsonStr,key){
        // 将字符串转成JSOn对象:  类似数组
        var jsonObj = JSON.parse(jsonStr);

        var value = "";

        for(var i=0;i<jsonObj.length;i++){

            if(i>0){
                value += ",";
            }
              //{"id":41,"text":"奥义"}
            value += jsonObj[i][key];
        }
        return value;
    }

});