"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[970],{1970:function(e,t,a){a.r(t),a.d(t,{default:function(){return d}});var o=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{margin:"-5px 0 10px 0"}},[t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入机构名","suffix-icon":"el-icon-search"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),t("el-button",{staticClass:"ml-5",attrs:{type:"primary"},on:{click:e.load}},[e._v("搜索")]),t("el-button",{attrs:{type:"warning"},on:{click:e.reset}},[e._v("重置")]),t("el-popconfirm",{staticStyle:{"margin-left":"10px"},attrs:{"confirm-button-text":"确定","cancel-button-text":"再想想",icon:"el-icon-info","icon-color":"red",title:"您确定批量删除吗？"},on:{confirm:e.delBatch}},[t("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("批量删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"5px"}})])],1)],1),t("el-table",{attrs:{data:e.tableData,border:"",stripe:""},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55"}}),t("el-table-column",{attrs:{prop:"name",label:"第几分店",width:"120"}}),t("el-table-column",{attrs:{prop:"address",label:"机构地址",width:"260"}}),t("el-table-column",{attrs:{prop:"phone",label:"电话"}}),t("el-table-column",{attrs:{prop:"createTime",label:"创建时间"}}),t("el-table-column",{attrs:{label:"启用"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#ccc"},on:{change:function(t){return e.changeEnable(a.row)}},model:{value:a.row.enable,callback:function(t){e.$set(a.row,"enable",t)},expression:"scope.row.enable"}})]}}])}),t("el-table-column",{attrs:{width:"200",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{on:{click:function(t){return e.handleEdit(a.row)}}},[e._v("编辑"),t("i",{staticClass:"el-icon-edit",staticStyle:{"margin-left":"3px"}})]),t("el-button",{attrs:{slot:"reference",type:"danger"},on:{click:function(t){return e.del(a.row.id)}},slot:"reference"},[e._v("删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"3px"}})])]}}])})],1),t("div",{staticStyle:{padding:"10px 0"}},[t("el-pagination",{attrs:{"current-page":e.pageNum,"page-sizes":[5,10,15,20],"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),t("el-dialog",{attrs:{title:"机构信息",visible:e.dialogFormVisibleUpdate,width:"60%"},on:{"update:visible":function(t){e.dialogFormVisibleUpdate=t}}},[t("el-form",{attrs:{"label-width":"80px",size:"small"}},[t("el-form-item",{attrs:{label:"第几分店"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),t("el-form-item",{attrs:{label:"电话"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.phone,callback:function(t){e.$set(e.form,"phone",t)},expression:"form.phone"}})],1),t("el-form-item",{attrs:{label:"修改地址:"}},[t("span",{model:{value:e.form.address,callback:function(t){e.$set(e.form,"address",t)},expression:"form.address"}},[e._v(e._s(e.placeAddress))]),t("span",{model:{value:e.form.addressLng,callback:function(t){e.$set(e.form,"addressLng",t)},expression:"form.addressLng"}},[e._v(e._s(e.placeLng))]),t("span",{model:{value:e.form.addressLat,callback:function(t){e.$set(e.form,"addressLat",t)},expression:"form.addressLat"}},[e._v(e._s(e.placeLat))]),t("div",{staticStyle:{width:"100%",height:"calc(95vh - 100px)","margin-top":"10px"},attrs:{id:"container"}})])],1),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.dialogFormVisibleUpdate=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.update}},[e._v("确 定")])],1)],1)],1)},n=[],i=a(7634),l=a.n(i);window._AMapSecurityConfig={securityJsCode:"bb1f932883d837cc53c75d65d70dcc16"};var s={name:"College",data(){return{tableData:[],total:0,pageNum:1,pageSize:5,name:"",phone:"",status:"",dialogFormVisibleUpdate:!1,multipleSelection:[],form:{},roles:[],courses:[],counts:0,user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{},map:null,marker:null,oldMarker:null,autoOptions:{input:""},searchPlaceInput:"",auto:null,placeSearch:null,college:[],arr:[],placeAddress:"",placeLocation:"",placeLng:"",placeLat:"",collegeAddress:"",flag:0}},created(){this.load()},watch:{searchPlaceInput(e){null!=e&&this.placeSearch.search(e)}},methods:{handleSizeChange(e){this.pageSize=e,this.load()},handleCurrentChange(e){this.pageNum=e,this.load()},load(){this.request.get("/college/page",{params:{pageNum:this.pageNum,pageSize:this.pageSize,name:this.name}}).then((e=>{this.tableData=e.data.records,this.total=e.data.total}))},handleEdit(e){this.initMap(),this.placeAddress="",this.form=JSON.parse(JSON.stringify(e)),this.dialogFormVisibleUpdate=!0},update(){""!=this.placeAddress&&(this.form.address=this.placeAddress,this.form.addressLng=this.placeLng,this.form.addressLat=this.placeLat),this.request.post("/college",this.form).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.dialogFormVisibleUpdate=!1,this.load()):this.$message.error("保存失败")}))},reset(){this.name="",this.load()},handleSelectionChange(e){this.multipleSelection=e},delBatch(){let e=this.multipleSelection.map((e=>e.id));this.request.delete("/college/del/batch",{data:e}).then((e=>{e?(this.$message.success("批量删除成功"),this.load()):this.$message.error("批量删除失败")}))},del(e){this.$confirm("确认删除该账号?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{this.request.delete("/college/"+e).then((e=>{"200"===e.code?(this.$message.success("删除成功"),this.load()):this.$message.error("删除失败")}))}))},changeEnable(e){this.request.post("/college/update",e).then((e=>{"200"===e.code&&this.$message.success("更新成功")}))},async initMap(){var e=this;l().load({key:"dbeb4ce6db4f24beaec8217d5ffdb847",version:"2.0",plugins:["AMap.ToolBar","AMap.Geolocation","AMap.AutoComplete","AMap.PlaceSearch","AMap.Geocoder"]}).then((t=>{this.map=new t.Map("container",{viewMode:"3D",zoom:5}),this.map.addControl(new t.ToolBar),this.auto=new t.AutoComplete(this.autoOptions),this.placeSearch=new t.PlaceSearch({map:this.map}),this.auto.on("select",this.select),this.map.on("click",(e=>{let t=e.lnglat.lat,a=e.lnglat.lng;this.getLngLatService(a,t)}));var a=new t.InfoWindow({offset:new t.Pixel(0,-30)});for(let e=0;e<this.college.length;e++){let a=new t.Marker({position:new t.LngLat(this.college[e].addressLng,this.college[e].addressLat),map:this.map,offset:new t.Pixel(-15,-20)});this.map.add([a]),a.content="机构"+this.college[e].name+"店",a.content="机构"+this.college[e].name+"店",a.on("click",o)}function o(t){a.setContent(t.target.content),a.open(e.map,t.target.getPosition())}})).catch((e=>{console.log(e)}))},select(e){this.placeSearch.setCity(e.poi.adcode),this.placeSearch.search(e.poi.name)},getLngLatService(e,t){let a=[e,t],o=new AMap.LngLat(e,t),n=new AMap.Geocoder({city:"全国"}),i=new AMap.Marker({position:new AMap.LngLat(e,t)});this.map.add(i);let l="";n.getAddress(o,((o,n)=>{if("complete"===o&&n.regeocode){l=n.regeocode.formattedAddress;var s={addressLng:e,addressLat:t,address:l};this.collegeAddress=s,console.log(this.collegeAddress),this.placeAddress=l,this.placeLocation=a,this.placeLng=e,this.placeLat=t,this.flag>0&&(console.log("++++++++++++"+this.flag),this.map.remove(this.oldMarker)),this.oldMarker=i,this.flag+=1}else log.error("根据经纬度查询地址失败")}))}}},r=s,c=a(1001),p=(0,c.Z)(r,o,n,!1,null,"6d214b2c",null),d=p.exports},7634:function(e,t,a){a(7658),a(1703),function(t,a){e.exports=a()}(0,(function(){function e(e){var o=[];return e.AMapUI&&o.push(t(e.AMapUI)),e.Loca&&o.push(a(e.Loca)),Promise.all(o)}function t(e){return new Promise((function(t,a){var s=[];if(e.plugins)for(var r=0;r<e.plugins.length;r+=1)-1==n.AMapUI.plugins.indexOf(e.plugins[r])&&s.push(e.plugins[r]);if(i.AMapUI===o.failed)a("前次请求 AMapUI 失败");else if(i.AMapUI===o.notload){i.AMapUI=o.loading,n.AMapUI.version=e.version||n.AMapUI.version,r=n.AMapUI.version;var c=document.body||document.head,p=document.createElement("script");p.type="text/javascript",p.src="https://webapi.amap.com/ui/"+r+"/main.js",p.onerror=function(e){i.AMapUI=o.failed,a("请求 AMapUI 失败")},p.onload=function(){if(i.AMapUI=o.loaded,s.length)window.AMapUI.loadUI(s,(function(){for(var e=0,a=s.length;e<a;e++){var o=s[e].split("/").slice(-1)[0];window.AMapUI[o]=arguments[e]}for(t();l.AMapUI.length;)l.AMapUI.splice(0,1)[0]()}));else for(t();l.AMapUI.length;)l.AMapUI.splice(0,1)[0]()},c.appendChild(p)}else i.AMapUI===o.loaded?e.version&&e.version!==n.AMapUI.version?a("不允许多个版本 AMapUI 混用"):s.length?window.AMapUI.loadUI(s,(function(){for(var e=0,a=s.length;e<a;e++){var o=s[e].split("/").slice(-1)[0];window.AMapUI[o]=arguments[e]}t()})):t():e.version&&e.version!==n.AMapUI.version?a("不允许多个版本 AMapUI 混用"):l.AMapUI.push((function(e){e?a(e):s.length?window.AMapUI.loadUI(s,(function(){for(var e=0,a=s.length;e<a;e++){var o=s[e].split("/").slice(-1)[0];window.AMapUI[o]=arguments[e]}t()})):t()}))}))}function a(e){return new Promise((function(t,a){if(i.Loca===o.failed)a("前次请求 Loca 失败");else if(i.Loca===o.notload){i.Loca=o.loading,n.Loca.version=e.version||n.Loca.version;var s=n.Loca.version,r=n.AMap.version.startsWith("2"),c=s.startsWith("2");if(r&&!c||!r&&c)a("JSAPI 与 Loca 版本不对应！！");else{r=n.key,c=document.body||document.head;var p=document.createElement("script");p.type="text/javascript",p.src="https://webapi.amap.com/loca?v="+s+"&key="+r,p.onerror=function(e){i.Loca=o.failed,a("请求 AMapUI 失败")},p.onload=function(){for(i.Loca=o.loaded,t();l.Loca.length;)l.Loca.splice(0,1)[0]()},c.appendChild(p)}}else i.Loca===o.loaded?e.version&&e.version!==n.Loca.version?a("不允许多个版本 Loca 混用"):t():e.version&&e.version!==n.Loca.version?a("不允许多个版本 Loca 混用"):l.Loca.push((function(e){e?a(e):a()}))}))}if(!window)throw Error("AMap JSAPI can only be used in Browser.");var o;(function(e){e.notload="notload",e.loading="loading",e.loaded="loaded",e.failed="failed"})(o||(o={}));var n={key:"",AMap:{version:"1.4.15",plugins:[]},AMapUI:{version:"1.1",plugins:[]},Loca:{version:"1.3.2"}},i={AMap:o.notload,AMapUI:o.notload,Loca:o.notload},l={AMap:[],AMapUI:[],Loca:[]},s=[],r=function(e){"function"==typeof e&&(i.AMap===o.loaded?e(window.AMap):s.push(e))};return{load:function(t){return new Promise((function(a,l){if(i.AMap==o.failed)l("");else if(i.AMap==o.notload){var c=t.key,p=t.version,d=t.plugins;c?(window.AMap&&"lbs.amap.com"!==location.host&&l("禁止多种API加载方式混用"),n.key=c,n.AMap.version=p||n.AMap.version,n.AMap.plugins=d||n.AMap.plugins,i.AMap=o.loading,p=document.body||document.head,window.___onAPILoaded=function(n){if(delete window.___onAPILoaded,n)i.AMap=o.failed,l(n);else for(i.AMap=o.loaded,e(t).then((function(){a(window.AMap)}))["catch"](l);s.length;)s.splice(0,1)[0]()},d=document.createElement("script"),d.type="text/javascript",d.src="https://webapi.amap.com/maps?callback=___onAPILoaded&v="+n.AMap.version+"&key="+c+"&plugin="+n.AMap.plugins.join(","),d.onerror=function(e){i.AMap=o.failed,l(e)},p.appendChild(d)):l("请填写key")}else if(i.AMap==o.loaded)if(t.key&&t.key!==n.key)l("多个不一致的 key");else if(t.version&&t.version!==n.AMap.version)l("不允许多个版本 JSAPI 混用");else{if(c=[],t.plugins)for(p=0;p<t.plugins.length;p+=1)-1==n.AMap.plugins.indexOf(t.plugins[p])&&c.push(t.plugins[p]);c.length?window.AMap.plugin(c,(function(){e(t).then((function(){a(window.AMap)}))["catch"](l)})):e(t).then((function(){a(window.AMap)}))["catch"](l)}else if(t.key&&t.key!==n.key)l("多个不一致的 key");else if(t.version&&t.version!==n.AMap.version)l("不允许多个版本 JSAPI 混用");else{var u=[];if(t.plugins)for(p=0;p<t.plugins.length;p+=1)-1==n.AMap.plugins.indexOf(t.plugins[p])&&u.push(t.plugins[p]);r((function(){u.length?window.AMap.plugin(u,(function(){e(t).then((function(){a(window.AMap)}))["catch"](l)})):e(t).then((function(){a(window.AMap)}))["catch"](l)}))}}))},reset:function(){delete window.AMap,delete window.AMapUI,delete window.Loca,n={key:"",AMap:{version:"1.4.15",plugins:[]},AMapUI:{version:"1.1",plugins:[]},Loca:{version:"1.3.2"}},i={AMap:o.notload,AMapUI:o.notload,Loca:o.notload},l={AMap:[],AMapUI:[],Loca:[]}}}}))}}]);
//# sourceMappingURL=970.f767c837.js.map