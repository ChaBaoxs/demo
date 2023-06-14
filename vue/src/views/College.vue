<template>
<div>
  <div style="margin: -5px 0 10px 0">
    <el-input style="width: 200px;" placeholder="请输入机构名" suffix-icon="el-icon-search" v-model="name"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
    <el-popconfirm
        style="margin-left: 10px"
        confirm-button-text='确定'
        cancel-button-text='再想想'
        icon="el-icon-info"
        icon-color="red"
        title="您确定批量删除吗？"
        @confirm="delBatch"
    >
      <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline" style="margin-left: 5px"></i></el-button>
    </el-popconfirm>
  </div>

  <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="name" label="第几分店" width="120"></el-table-column>
    <el-table-column prop="address" label="机构地址" width="260"></el-table-column>
    <el-table-column prop="phone" label="电话"></el-table-column>
    <el-table-column prop="createTime" label="创建时间"></el-table-column>
    <el-table-column label="启用">
      <template slot-scope="scope">
        <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                   @change="changeEnable(scope.row)"></el-switch>
      </template>
    </el-table-column>
    <el-table-column width="200" label="操作">
      <template slot-scope="scope">
        <el-button  @click="handleEdit(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 3px"></i></el-button>
        <el-button  @click="del(scope.row.id)" type="danger" slot="reference">删除<i class="el-icon-remove-outline" style="margin-left: 3px"></i></el-button>
      </template>

    </el-table-column>
  </el-table>
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>


  <el-dialog title="机构信息" :visible.sync="dialogFormVisibleUpdate" width="60%">
    <el-form label-width="80px" size="small">
      <el-form-item label="第几分店">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="修改地址:">
        <span v-model="form.address">{{placeAddress}}</span>
        <span v-model="form.addressLng">{{placeLng}}</span>
        <span v-model="form.addressLat">{{placeLat}}</span>
<!--        <br>-->
<!--        <span>{{form.address}}</span>-->

        <div id="container" style="width: 100%; height: calc(95vh - 100px);margin-top: 10px"></div>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisibleUpdate = false">取 消</el-button>
      <el-button type="primary" @click="update">确 定</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'
window._AMapSecurityConfig = {
  securityJsCode: 'bb1f932883d837cc53c75d65d70dcc16'
}

export default {
  name: "College",
  data(){
    return{
      tableData:[],
      total:0,
      pageNum:1,
      pageSize:5,
      name:"",
      phone:"",
      status:"",
      dialogFormVisibleUpdate:false,
      multipleSelection:[],
      form:{},
      roles: [],
      courses: [],
      counts: 0,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): {},
      map:null,
      marker:null,
      oldMarker:null,
      autoOptions: {
        input: ''
      },
      searchPlaceInput:'',
      auto: null,
      placeSearch:null,
      college:[],
      arr:[],
      placeAddress:'',
      placeLocation:'',
      placeLng:'',
      placeLat:'',
      collegeAddress:'',
      flag:0,



    }
  },
  created() {
    //请求分页
    this.load()

  },

  watch: {
    searchPlaceInput(newValue) {
      if (newValue != null) {
        // console.log(newValue)
        this.placeSearch.search(newValue)
      }
    }
  },

  methods:{
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    load(){
      this.request.get("/college/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

    },
    //修改
    handleEdit(row){
      this.initMap()
      this.placeAddress = ""
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisibleUpdate =true
    },
    update(){
      if(this.placeAddress != ""){
        this.form.address = this.placeAddress
        this.form.addressLng = this.placeLng
        this.form.addressLat = this.placeLat
      }

      this.request.post("/college",this.form).then(res =>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisibleUpdate = false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    reset(){
      this.name = ""
      this.load()
    },

    //删除
    handleSelectionChange(val){
      this.multipleSelection =val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{},{},{}] =>[1,2,3] 扁平化处理把对象数组转化为纯id数组
      this.request.delete("/college/del/batch",{data:ids}).then(res =>{
        if (res){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("批量删除失败")
        }
      })
    },
    del(id) {
      this.$confirm('确认删除该账号?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
        this.request.delete("/college/" + id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功")
            this.load()
          } else {
            this.$message.error("删除失败")
          }
        })
      })
    },

    //启用
    changeEnable(row){
      this.request.post("/college/update",row).then(res => {
        if (res.code === '200'){
          this.$message.success("更新成功")
        }
      })
    },

    async initMap(){
      var that=this

      AMapLoader.load({
        key:"dbeb4ce6db4f24beaec8217d5ffdb847",             // 申请好的Web端开发者Key，首次调用 load 时必填
        version:"2.0",      // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins:['AMap.ToolBar', 'AMap.Geolocation','AMap.AutoComplete','AMap.PlaceSearch','AMap.Geocoder'],       // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      }).then((AMap)=>{
        this.map = new AMap.Map("container",{  //设置地图容器id
          viewMode:"3D",    //是否为3D地图模式
          zoom:5,           //初始化地图级别
          // center:[121.48769,29.891447], //初始化地图中心点位置，没有定义会自动定位IP的地址获取中心点
        });
        this.map.addControl(new AMap.ToolBar())

        this.auto = new AMap.AutoComplete(this.autoOptions)//搜索栏关键词搜索
        //poi搜索
        this.placeSearch = new AMap.PlaceSearch({
          map: this.map
        }) //构造地点查询类
        this.auto.on('select', this.select)

        this.map.on('click', e =>{
          let lat = e.lnglat.lat
          let lng = e.lnglat.lng
          this.getLngLatService(lng,lat)
        })

        //信息窗口实例
        var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});

        // 循环所有的标记点
        for( let i =0; i <  this.college.length; i++) {
          let marker = new AMap.Marker({
            // position: this.arr[i], // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
            position:new AMap.LngLat(this.college[i].addressLng,this.college[i].addressLat),
            map:this.map,
            offset: new AMap.Pixel(-15, -20),
          });
          // 将创建的点标记添加到已有的地图实例：
          this.map.add([marker]);
          // marker.content = '我是第' + (i + 1) + '个Marker';
          marker.content = '机构' + this.college[i].name + '店';
          marker.content = '机构' + this.college[i].name + '店';
          marker.on('click', markerClick);
          // marker.emit('click', {target: marker});//默认初始化不出现信息窗体,打开初始化就出现信息窗体
        }

        function markerClick(e) {
          infoWindow.setContent(e.target.content);
          infoWindow.open(that.map, e.target.getPosition());
        }

      }).catch(e=>{
        console.log(e);
      })
    },



    select(e) {
      this.placeSearch.setCity(e.poi.adcode)
      this.placeSearch.search(e.poi.name) //关键字查询查询
    },



    //逆向地理编码服务
    getLngLatService(lng,lat) {
      let pos = [lng,lat]
      let lnglat = new AMap.LngLat(lng,lat)
      let geocoder = new AMap.Geocoder({
        // city 指定进行编码查询的城市，支持传入城市名、adcode 和 citycode
        city: '全国'
      })
      //1.点击地图任意位置生成一个marker
      let marker = new AMap.Marker({
        position:new AMap.LngLat(lng,lat),
      })
      this.map.add(marker)
      //2.将位置转化为坐标点-->地理位置信息
      //3.根据地理位置信息搜索获取详细信息
      let address = ''
      geocoder.getAddress(lnglat, (status, result) => {
        if (status === 'complete'&&result.regeocode) {
          address = result.regeocode.formattedAddress;
          var collegeAddress = {
            addressLng : lng,
            addressLat : lat,
            address : address
          }
          this.collegeAddress = collegeAddress
          console.log(this.collegeAddress)
          this.placeAddress =address
          this.placeLocation =pos
          this.placeLng =lng
          this.placeLat =lat

          if (this.flag>0){
            console.log("++++++++++++"+this.flag)
            this.map.remove(this.oldMarker);
          }
          this.oldMarker = marker;
          this.flag +=1
        }else{
          log.error('根据经纬度查询地址失败')
        }
      })
    },
  },

}
</script>

<style scoped>

</style>