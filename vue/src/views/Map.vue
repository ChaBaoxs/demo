<template>
  <div>
    <div style="display: flex">
    <div style="flex: 6">
      <el-input  v-model="inputObject.userInput" :id="inputObject.inputId" placeholder="请输入你要查找的关键词" style="margin-top: -5px;width: 81%"></el-input>
      <el-button type="primary" style="margin-top: -5px;float: right;width: 120px"@click="send">查询</el-button>
    </div>
    <div style="flex: 4;margin-left: 20px;margin-top: -10px;">
      <div>
        <span>点击地址：</span>
        <span>{{placeAddress}}</span>
      </div>
      <div>
        <span>点击坐标：</span>
        <span>{{placeLocation}}</span>
      </div>
    </div>
      <div style="flex: 1">
        <el-button type="success" style="margin-top: -5px;float: right;width: 120px"@click="update"  v-if="user.role === 'ROLE_ADMIN'">确认地址</el-button>
      </div>
    </div>
    <div id="container" style="width: 100%; height: calc(95vh - 100px);margin-top: 10px"></div>

    <el-dialog title="确认地址信息" :visible.sync="dialogFrom" width="30%">
      <el-form label-width="70px" size="small">
<!--        <el-form-item label="机构名称">-->
<!--          <el-input v-model="form.name" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="地址：">
          <span v-model="form.address">{{placeAddress}}</span>
        </el-form-item>
        <el-form-item label="经度：">
          <span v-model="form.addressLng">{{placeLng}}</span>
        </el-form-item>
        <el-form-item label="纬度：">
          <span v-model="form.addressLat">{{placeLat}}</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFrom = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'
window._AMapSecurityConfig = {
  securityJsCode: 'bb1f932883d837cc53c75d65d70dcc16'
}

var content = [
  "<div style='font-size: 14px; color: red; width: 200px; height: 50px'>这是信息窗口</div>"
];

export default {
  name: "Map",
  data() {
    return {
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): {},
      map:null,
      marker:null,
      oldMarker:null,
      autoOptions: {
        input: ''
      },
      searchPlaceInput:'',
      auto: null,
      inputObject:{
        userInput:'',
        inputId:'searchInput',
      },
      placeSearch:null,
      college:[],
      arr:[],
      placeAddress:'',
      placeLocation:'',
      placeLng:'',
      placeLat:'',
      collegeAddress:'',
      flag:0,
      dialogFrom:false,
      form:{},
    }
  },
  created() {
    this.autoOptions.input = this.inputObject.inputId
    this.getMap()


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
    async initMap(){
      var that=this

      AMapLoader.load({
        key:"dbeb4ce6db4f24beaec8217d5ffdb847",             // 申请好的Web端开发者Key，首次调用 load 时必填
        version:"2.0",      // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins:['AMap.ToolBar', 'AMap.Scale', 'AMap.HawkEye', 'AMap.MapType', 'AMap.Geolocation','AMap.AutoComplete','AMap.PlaceSearch','AMap.Geocoder'],       // 需要使用的的插件列表，如比例尺'AMap.Scale'等
      }).then((AMap)=>{
        this.map = new AMap.Map("container",{  //设置地图容器id
          viewMode:"3D",    //是否为3D地图模式
          zoom:5,           //初始化地图级别
          // center:[121.48769,29.891447], //初始化地图中心点位置，没有定义会自动定位IP的地址获取中心点
        });
        this.map.addControl(new AMap.Scale())
        this.map.addControl(new AMap.ToolBar())
        this.map.addControl(new AMap.HawkEye())
        this.map.addControl(new AMap.MapType())
        this.map.addControl(new AMap.Geolocation())
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


          // var marker = new AMap.Marker({
          //   position: new AMap.LngLat(121.48769,29.891447),   // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
          //   title: '培训机构',
          //   icon: '//vdata.amap.com/icons/b18/1/2.png',
          //   // content: '<div style="font-size: 12px; width: 100px">这是我自定义的内容</div>'
          // })

        // // 将创建的点标记添加到已有的地图实例：
        // this.map.add(marker);
        // console.log(this.arr=="[117.517694,32.414674],[121.48769,29.891447]")
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

    //搜索
    send() {
      this.searchPlaceInput = this.inputObject.userInput
      // console.log(this.inputObject);
    },

    select(e) {
      this.placeSearch.setCity(e.poi.adcode)
      this.placeSearch.search(e.poi.name) //关键字查询查询
    },

    getMap(){
       this.request.get("/college").then(res => {
        this.college= res.data
        for( let i =0; i <  this.college.length; i++) {
          this.arr.push(this.college[i].address)
        }
        console.log("========"+this.arr)
        console.log("***************"+this.college[0].addressLat)
         this.initMap()
      })
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
    // update(){
    //   this.$confirm('确认该地址为机构地址?', '提示', {
    //
    //     'confirmButtonText': '确定',
    //     'cancelButtonText': '取消',
    //     'type': 'warning'
    //   }).then(() => {
    //   this.request.post("/college",this.collegeAddress).then(res =>{
    //     if (res.code === '200'){
    //       this.$message.success("保存成功")
    //     }else {
    //       this.$message.error("保存失败")
    //     }
    //   })
    //   })
    // },

    update(){
      this.form ={}
      this.dialogFrom= true
    },
    save(){
      this.request.post("/college",this.collegeAddress).then(res =>{
            if (res.code === '200'){
              this.$message.success("保存成功")
              this.dialogFrom= false
            }else {
              this.$message.error("保存失败")
            }
          })
    }
  },
  mounted() {
    //DOM初始化完成进行地图初始化
    // this.initMap()
    this.send()

  }
}
</script>

<style>

</style>
