"use strict";(self["webpackChunkvue"]=self["webpackChunkvue"]||[]).push([[48],{7048:function(e,t,l){l.r(t),l.d(t,{default:function(){return c}});var a=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{margin:"-5px 0 0 0"}},[t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入名称","suffix-icon":"el-icon-search"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}}),t("el-button",{staticClass:"ml-5",attrs:{type:"primary"},on:{click:e.load}},[e._v("搜索")]),t("el-button",{attrs:{type:"warning"},on:{click:e.reset}},[e._v("重置")])],1),t("div",{staticStyle:{margin:"10px 0"}},[t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.handleAdd()}}},[e._v("新增"),t("i",{staticClass:"el-icon-circle-plus-outline",staticStyle:{"margin-left":"5px"}})]),t("el-popconfirm",{staticClass:"ml-5",attrs:{"confirm-button-text":"确定","cancel-button-text":"再想想",icon:"el-icon-info","icon-color":"red",title:"您确定批量删除吗？"},on:{confirm:e.delBatch}},[t("el-button",{attrs:{slot:"reference",type:"danger"},slot:"reference"},[e._v("批量删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"5px"}})])],1)],1),t("el-table",{attrs:{data:e.tableData,border:"",stripe:"","row-key":"id","default-expand-all":""},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55"}}),t("el-table-column",{attrs:{prop:"id",label:"ID",width:"80"}}),t("el-table-column",{attrs:{prop:"name",label:"名称"}}),t("el-table-column",{attrs:{prop:"path",label:"路径"}}),t("el-table-column",{attrs:{prop:"pagePath",label:"页面路径"}}),t("el-table-column",{attrs:{label:"图标",align:"center"},scopedSlots:e._u([{key:"default",fn:function(e){return[t("span",{class:e.row.icon,staticStyle:{"font-size":"25px"}})]}}])}),t("el-table-column",{attrs:{prop:"description",label:"描述"}}),t("el-table-column",{attrs:{prop:"sortNum",label:"顺序"}}),t("el-table-column",{attrs:{label:"操作",width:"300px"},scopedSlots:e._u([{key:"default",fn:function(l){return[l.row.pid||l.row.path?e._e():t("el-button",{attrs:{type:"success"},on:{click:function(t){return e.handleAdd(l.row.id)}}},[e._v("新增子菜单"),t("i",{staticClass:"el-icon-plus",staticStyle:{"margin-left":"3px"}})]),t("el-button",{on:{click:function(t){return e.handleEdit(l.row)}}},[e._v("编辑"),t("i",{staticClass:"el-icon-edit",staticStyle:{"margin-left":"3px"}})]),t("el-button",{attrs:{slot:"reference",type:"danger"},on:{click:function(t){return e.del(l.row.id)}},slot:"reference"},[e._v("删除"),t("i",{staticClass:"el-icon-remove-outline",staticStyle:{"margin-left":"3px"}})])]}}])})],1),t("el-dialog",{attrs:{title:"菜单信息",visible:e.dialogFormVisible,width:"30%"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[t("el-form",{ref:"userForm",attrs:{"label-width":"60px",size:"small",model:e.form}},[t("el-form-item",{attrs:{label:"名称"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),t("el-form-item",{attrs:{label:"路径"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.path,callback:function(t){e.$set(e.form,"path",t)},expression:"form.path"}})],1),t("el-form-item",{attrs:{label:"页面路径"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.pagePath,callback:function(t){e.$set(e.form,"pagePath",t)},expression:"form.pagePath"}})],1),t("el-form-item",{attrs:{label:"图标"}},[t("el-select",{staticStyle:{width:"100%"},attrs:{clearable:"",placeholder:"请选择"},model:{value:e.form.icon,callback:function(t){e.$set(e.form,"icon",t)},expression:"form.icon"}},e._l(e.options,(function(l){return t("el-option",{key:l.name,attrs:{label:l.name,value:l.value}},[t("i",{class:l.value}),e._v(" "+e._s(l.name)+" ")])})),1)],1),t("el-form-item",{attrs:{label:"描述"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.description,callback:function(t){e.$set(e.form,"description",t)},expression:"form.description"}})],1),t("el-form-item",{attrs:{label:"顺序"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.sortNum,callback:function(t){e.$set(e.form,"sortNum",t)},expression:"form.sortNum"}})],1)],1),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.save}},[e._v("确 定")])],1)],1)],1)},o=[],s={name:"Employee",data(){return{tableData:[],total:0,pageNum:1,pageSize:5,name:"",dialogFormVisible:!1,multipleSelection:[],form:{},roles:[],counts:0,options:[]}},created(){this.load()},methods:{load(){this.request.get("/menu",{params:{name:this.name}}).then((e=>{this.tableData=e.data}))},handleAdd(e){this.form={},void 0!==this.$refs["userForm"]&&this.$refs["userForm"].resetFields(),this.dialogFormVisible=!0,e&&(this.form.pid=e),this.request.get("/menu/icons").then((e=>{this.options=e.data}))},save(){this.$refs["userForm"].validate((e=>{e&&this.request.post("/menu",this.form).then((e=>{"200"===e.code?(this.$message.success("保存成功"),this.dialogFormVisible=!1,this.load()):this.$message.error("保存失败")}))}))},handleEdit(e){this.form=JSON.parse(JSON.stringify(e)),this.dialogFormVisible=!0,this.request.get("/menu/icons").then((e=>{this.options=e.data}))},reset(){this.name="",this.load()},handleSelectionChange(e){this.multipleSelection=e},delBatch(){let e=this.multipleSelection.map((e=>e.id));this.request.delete("/menu/del/batch",{data:e}).then((e=>{e?(this.$message.success("批量删除成功"),this.load()):this.$message.error("批量删除失败")}))},del(e){this.$confirm("确认删除该账号?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{this.request.delete("/menu/"+e).then((e=>{"200"===e.code?(this.$message.success("删除成功"),this.load()):this.$message.error("删除失败")}))}))}}},i=s,r=l(1001),n=(0,r.Z)(i,a,o,!1,null,"140daa7e",null),c=n.exports}}]);
//# sourceMappingURL=48.1cec41ab.js.map