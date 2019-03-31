//自定义服务层: 发起请求的
app.service("brandService",function ($http) {
    //查询所有的品牌
    //this代表当前服务层即service
    this.findAll = function () {
        return $http.get("/brand/findAll.do");
    };

    //根据条件查询分页对象
    this.search = function (currentPage,itemsPerPage,searchEntity) {
        return  $http.post("/brand/search.do?pageNum="+currentPage +"&pageSize="+itemsPerPage,searchEntity);

    };

    //查询分页对象
    this.findAllByPage = function (currentPage,itemsPerPage) {
        return $http.get("/brand/findAllByPage.do?pageNum="+currentPage +"&pageSize="+itemsPerPage);
    };

    //新增
    this.save = function (entity) {
        return $http.post("/brand/save.do",entity);
    };

    //修改
    this.update = function (entity) {
        return $http.post("/brand/update.do",entity);
    };

    //根据id查询一个品牌并回显
    this.findById = function (id) {
        return $http.get("/brand/findById.do?id="+id);
    };

    //删除
    this.delete = function (selectedIds) {
        return $http.get("/brand/deletes.do?ids="+selectedIds);
    };

    //品牌下拉列表数据
    this.selectOptionList = function(){
        return $http.get("../brand/selectOptionList.do");//url 入参：无(因为查询所有)  返回值：List<Map>
    }

});