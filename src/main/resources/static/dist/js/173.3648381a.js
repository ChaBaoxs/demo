"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[173],{2810:function(e,t,s){s.r(t),s.d(t,{default:function(){return c}});var a=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{margin:"-5px 0 0 0"}},[t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入名称","suffix-icon":"el-icon-search"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),t("el-button",{staticClass:"ml-5",attrs:{type:"primary"},on:{click:e.load}},[e._v("搜索")]),t("el-button",{attrs:{type:"warning"},on:{click:e.reset}},[e._v("重置")])],1),t("div",{staticStyle:{margin:"10px 0"}},[t("el-button",{attrs:{type:"primary"},on:{click:e.handleAdd}},[e._v("新增"),t("i",{staticClass:"el-icon-circle-plus-outline",staticStyle:{"margin-left":"5px"}})]),t("el-popconfirm",{staticClass:"ml-5",attrs:{"confirm-button-text":"确定","cancel-button-text":"再想想",icon:"el-icon-info","icon-color":"red",title:"您确定批量删除吗？"},on:{confirm:e.delBatch}},[t("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("批量删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"5px"}})])],1)],1),t("el-table",{attrs:{data:e.tableData,border:"",stripe:""},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55"}}),t("el-table-column",{attrs:{prop:"id",label:"ID",width:"80"}}),t("el-table-column",{attrs:{prop:"name",label:"名称"}}),t("el-table-column",{attrs:{prop:"flag",label:"唯一标识"}}),t("el-table-column",{attrs:{prop:"description",label:"描述"}}),t("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(s){return[t("el-button",{attrs:{type:"success"},on:{click:function(t){return e.selectMenu(s.row.id)}}},[e._v("分配菜单"),t("i",{staticClass:"el-icon-menu"})]),t("el-button",{on:{click:function(t){return e.handleEdit(s.row)}}},[e._v("编辑"),t("i",{staticClass:"el-icon-edit",staticStyle:{"margin-left":"3px"}})]),t("el-button",{attrs:{slot:"reference",type:"danger"},on:{click:function(t){return e.del(s.row.id)}},slot:"reference"},[e._v("删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"3px"}})])]}}])})],1),t("div",{staticStyle:{padding:"10px 0"}},[t("el-pagination",{attrs:{"current-page":e.pageNum,"page-sizes":[5,10,15,20],"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),t("el-dialog",{attrs:{title:"角色信息",visible:e.dialogFormVisible,width:"30%"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[t("el-form",{ref:"userForm",attrs:{"label-width":"60px",size:"small",model:e.form,rules:e.rules}},[t("el-form-item",{attrs:{label:"名称",prop:"name"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),t("el-form-item",{attrs:{label:"唯一标识"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.flag,callback:function(t){e.$set(e.form,"flag",t)},expression:"form.flag"}})],1),t("el-form-item",{attrs:{label:"描述",prop:"description"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.description,callback:function(t){e.$set(e.form,"description",t)},expression:"form.description"}})],1)],1),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.save}},[e._v("确 定")])],1)],1),t("el-dialog",{attrs:{title:"菜单分配",visible:e.menuDialogVis,width:"30%"},on:{"update:visible":function(t){e.menuDialogVis=t}}},[t("el-tree",{ref:"tree",attrs:{props:e.props,data:e.menuData,"show-checkbox":"","node-key":"id","default-expanded-keys":e.expends,"default-checked-keys":e.checks},scopedSlots:e._u([{key:"default",fn:function({node:s,data:a}){return t("span",{staticClass:"custom-tree-node"},[t("span",[t("i",{class:a.icon}),e._v(" "+e._s(a.name))])])}}])}),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.menuDialogVis=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.saveRoleMenu}},[e._v("确 定")])],1)],1)],1)},i=[],l={name:"Employee",data(){return{tableData:[],total:0,pageNum:1,pageSize:5,name:"",dialogFormVisible:!1,menuDialogVis:!1,multipleSelection:[],form:{},roles:[],counts:0,menuData:[],props:{label:"name"},expends:[],checks:[],roleId:0,ids:[],rules:{name:[{required:!0,message:"请输入昵称",trigger:"blur"}]}}},created(){this.load()},methods:{handleSizeChange(e){this.pageSize=e,this.load()},handleCurrentChange(e){this.pageNum=e,this.load()},load(){this.request.get("/role/page",{params:{pageNum:this.pageNum,pageSize:this.pageSize,name:this.name}}).then((e=>{this.tableData=e.data.records,this.total=e.data.total}))},handleAdd(){this.form={},void 0!==this.$refs["userForm"]&&this.$refs["userForm"].resetFields(),this.dialogFormVisible=!0},save(){this.$refs["userForm"].validate((e=>{e&&this.request.post("/role",this.form).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.dialogFormVisible=!1,this.load()):this.$message.error("保存失败")}))}))},saveRoleMenu(){this.request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())).then((e=>{"200"===e.code?(this.$message.success("绑定成功"),this.menuDialogVis=!1):this.$message.error(e.msg)}))},handleEdit(e){this.form=JSON.parse(JSON.stringify(e)),this.dialogFormVisible=!0},reset(){this.name="",this.load()},handleSelectionChange(e){this.multipleSelection=e},delBatch(){let e=this.multipleSelection.map((e=>e.id));this.request.delete("/role/del/batch",{data:e}).then((e=>{e?(this.$message.success("批量删除成功"),this.load()):this.$message.error("批量删除失败")}))},del(e){this.$confirm("确认删除该账号?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{this.request.delete("/role/"+e).then((e=>{"200"===e.code?(this.$message.success("删除成功"),this.load()):this.$message.error("删除失败")}))}))},selectMenu(e){this.menuDialogVis=!0,this.roleId=e,this.request.get("/menu").then((e=>{this.menuData=e.data})),this.expends=this.menuData.map((e=>e.id)),this.request.get("/menu").then((e=>{this.menuData=e.data})),this.request.get("/role/roleMenu/"+this.roleId).then((e=>{this.checks=e.data,this.ids.forEach((e=>{this.checks.includes(e)||this.$nextTick((()=>{this.$refs.tree.setChecked(e,!1)}))})),this.menuDialogVis=!0}))}}},o=l,r=s(1001),n=(0,r.Z)(o,a,i,!1,null,"4db7c3d0",null),c=n.exports}}]);
//# sourceMappingURL=173.3648381a.js.map