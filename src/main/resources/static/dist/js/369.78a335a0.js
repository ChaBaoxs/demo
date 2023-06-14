"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[369],{9369:function(e,t,l){l.r(t),l.d(t,{default:function(){return c}});var a=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{margin:"10px 0"}},[t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入名称","suffix-icon":"el-icon-search"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),t("el-button",{staticClass:"ml-5",attrs:{type:"primary"},on:{click:e.load}},[e._v("搜索")]),t("el-button",{attrs:{type:"warning"},on:{click:e.reset}},[e._v("重置")])],1),t("div",{staticStyle:{margin:"10px 0"}},[t("el-upload",{staticStyle:{display:"inline-block"},attrs:{action:"http://localhost:9090/videoFile/upload","show-file-list":!1,"on-success":e.handleFileUploadSuccess}},[t("el-button",{staticClass:"ml-5",attrs:{type:"primary"}},[e._v("上传视频"),t("i",{staticClass:"el-icon-top"})])],1),t("el-popconfirm",{staticClass:"ml-5",attrs:{"confirm-button-text":"确定","cancel-button-text":"再想想",icon:"el-icon-info","icon-color":"red",title:"您确定批量删除吗？"},on:{confirm:e.delBatch}},[t("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("批量删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"5px"}})])],1)],1),t("el-table",{attrs:{data:e.tableData,border:"",stripe:""},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55"}}),t("el-table-column",{attrs:{prop:"id",label:"ID",width:"80"}}),t("el-table-column",{attrs:{prop:"name",label:"视频名称"}}),t("el-table-column",{attrs:{prop:"type",label:"视频类型"}}),t("el-table-column",{attrs:{prop:"cname",label:"所属课程"}}),t("el-table-column",{attrs:{prop:"url",label:"视频封面",width:"100"},scopedSlots:e._u([{key:"default",fn:function(e){return[t("el-image",{attrs:{src:e.row.coverUrl}})]}}])}),t("el-table-column",{attrs:{label:"文件预览"},scopedSlots:e._u([{key:"default",fn:function(l){return[t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.preview(l.row.url)}}},[e._v("预览")])]}}])}),t("el-table-column",{attrs:{label:"下载"},scopedSlots:e._u([{key:"default",fn:function(l){return[t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.download(l.row)}}},[e._v("下载")])]}}])}),t("el-table-column",{attrs:{label:"启用"},scopedSlots:e._u([{key:"default",fn:function(l){return[t("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#ccc"},on:{change:function(t){return e.changeEnable(l.row)}},model:{value:l.row.enable,callback:function(t){e.$set(l.row,"enable",t)},expression:"scope.row.enable"}})]}}])}),t("el-table-column",{attrs:{prop:"createTime",label:"创建时间",width:"140"}}),t("el-table-column",{attrs:{width:"220",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(l){return[t("el-button",{attrs:{slot:"reference",type:"success"},on:{click:function(t){return e.handleEdit(l.row)}},slot:"reference"},[e._v("分配课程"),t("i",{staticClass:"el-icon-collection",staticStyle:{"margin-left":"3px"}})]),t("el-button",{attrs:{slot:"reference",type:"danger"},on:{click:function(t){return e.del(l.row.id)}},slot:"reference"},[e._v("删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"3px"}})])]}}])})],1),t("div",{staticStyle:{padding:"10px 0"}},[t("el-pagination",{attrs:{"current-page":e.pageNum,"page-sizes":[5,10,15,20],"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),t("el-dialog",{attrs:{title:"视频信息",visible:e.dialogFormVisibleSelect,width:"30%"},on:{"update:visible":function(t){e.dialogFormVisibleSelect=t}}},[t("el-form",{attrs:{"label-width":"80px",size:"small"}},[t("el-form-item",{attrs:{label:"视频封面"}},[t("el-upload",{staticClass:"avatar-uploader",attrs:{action:"http://localhost:9090/videoFile/cover/upload","show-file-list":!1,"on-success":e.handleAvatarSuccess}},[e.form.coverUrl?t("img",{staticClass:"avatar",attrs:{src:e.form.coverUrl}}):t("i",{staticClass:"el-icon-plus avatar-uploader-icon"})])],1),t("el-form-item",{attrs:{label:"视频名称"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),t("el-form-item",{attrs:{label:"所选课程"}},[t("el-select",{attrs:{filterable:"",placeholder:"请选择"},model:{value:e.form.courseId,callback:function(t){e.$set(e.form,"courseId",t)},expression:"form.courseId"}},e._l(e.options,(function(l){return t("el-option",{key:l.name,attrs:{label:l.name,value:l.id}},[e._v(" "+e._s(l.name)+" ")])})),1)],1)],1),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.dialogFormVisibleSelect=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.select}},[e._v("确 定")])],1)],1)],1)},o=[],s=(l(2801),{name:"File.vue",data(){return{tableData:[],name:"",multipleSelection:[],dialogFormVisibleSelect:!1,dialogFormVisible:!1,form:{},total:0,pageNum:1,pageSize:5,options:[]}},created(){this.load(),this.request.get("/videoFile/course").then((e=>{this.options=e.data}))},methods:{handleSizeChange(e){this.pageSize=e,this.load()},handleCurrentChange(e){this.pageNum=e,this.load()},load(){this.request.get("/videoFile/page",{params:{pageNum:this.pageNum,pageSize:this.pageSize,name:this.name}}).then((e=>{this.tableData=e.data.records,this.total=e.data.total}))},reset(){this.name="",this.load()},handleSelectionChange(e){this.multipleSelection=e},delBatch(){let e=this.multipleSelection.map((e=>e.id));this.request.delete("/videoFile/del/batch",{data:e}).then((e=>{e?(this.$message.success("批量删除成功"),this.load()):this.$message.error("批量删除失败")}))},del(e){this.$confirm("确认删除该账号?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{this.request.delete("/videoFile/"+e).then((e=>{"200"===e.code?(this.$message.success("删除成功"),this.load()):this.$message.error("删除失败")}))}))},handleFileUploadSuccess(e){console.log(e),this.load()},download(e){this.request.post("/videoFile/down",e).then((t=>{"200"===t.code?(this.$message.success("下载获取成功"),window.open(e.url)):this.$message.error(t.msg)}))},changeEnable(e){this.request.post("/videoFile/update",e).then((e=>{"200"===e.code&&this.$message.success("更新成功")}))},preview(e){window.open("https://file.keking.cn/onlinePreview?url="+encodeURIComponent(window.btoa(e)))},handleEdit(e){this.form=JSON.parse(JSON.stringify(e)),this.dialogFormVisibleSelect=!0},select(){this.request.post("/videoFile/update",this.form).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.dialogFormVisibleSelect=!1,this.dialogFormVisible=!1,this.load()):this.$message.error("保存失败")}))},handleAvatarSuccess(e){this.form.coverUrl=e},handleAdd(){this.dialogFormVisible=!0,this.form={}}}}),i=s,n=l(1001),r=(0,n.Z)(i,a,o,!1,null,"3a28701f",null),c=r.exports}}]);
//# sourceMappingURL=369.78a335a0.js.map