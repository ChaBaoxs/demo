<template>
<div>
  <div style="margin: -5px 0 0 0">
    <el-input style="width: 200px;" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>

  <div style="margin: 10px 0">
    <el-button type="primary" @click="handleAdd()">新增<i class="el-icon-circle-plus-outline" style="margin-left: 5px"></i></el-button>
    <el-popconfirm
        class="ml-5"
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

  <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange"  row-key="id"  default-expand-all>
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="name" label="名称"></el-table-column>
    <el-table-column prop="path" label="路径"></el-table-column>
    <el-table-column prop="pagePath" label="页面路径"></el-table-column>
    <el-table-column label="图标" align="center" >
      <template slot-scope="scope">
        <span :class="scope.row.icon" style="font-size: 25px"/>
      </template>
    </el-table-column>
    <el-table-column prop="description" label="描述" ></el-table-column>
    <el-table-column prop="sortNum" label="顺序" ></el-table-column>
<!--    <el-table-column prop="updateUser" label="修改人"></el-table-column>-->
<!--    <el-table-column prop="createTime" label="创建时间"></el-table-column>-->
<!--    <el-table-column prop="updateTime" label="修改时间"></el-table-column>-->
    <el-table-column label="操作" width="300px">
      <template slot-scope="scope">
        <el-button type="success" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单<i class="el-icon-plus" style="margin-left: 3px"></i></el-button>
        <el-button @click="handleEdit(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 3px"></i></el-button>
        <el-button  @click="del(scope.row.id)" type="danger" slot="reference">删除<i class="el-icon-remove-outline" style="margin-left: 3px"></i></el-button>
      </template>

    </el-table-column>
  </el-table>

  <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="60px" size="small" :model="form" ref="userForm">
      <el-form-item label="名称" >
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="路径" >
        <el-input v-model="form.path" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="页面路径" >
        <el-input v-model="form.pagePath" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="图标">
        <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
          <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
            <i :class="item.value" /> {{ item.name }}
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="描述" >
        <el-input v-model="form.description" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="顺序" >
        <el-input v-model="form.sortNum" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
export default {
  name: "Employee",
  data(){
    return{
      tableData:[],
      total:0,
      pageNum:1,
      pageSize:5,
      name:"",
      dialogFormVisible:false,
      // dialogFormVisibleUpdate:false,
      multipleSelection:[],
      form:{},
      roles: [],
      counts: 0,
      options:[],

      // //校验规则
      // rules: {
      //   name: [
      //     {required: true, message: '请输入昵称', trigger: 'blur'},
      //   ]
      // }

    }
  },
  created() {
    this.load()
  },
  methods:{
    // handleSizeChange(pageSize){
    //   this.pageSize = pageSize
    //   this.load()
    // },
    // handleCurrentChange(pageNum){
    //   this.pageNum = pageNum
    //   this.load()
    // },
    load(){
      this.request.get("/menu",{
        params:{
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data
      })
    },
    //新增
    handleAdd(pid){
      this.form ={}
      // //加if判断条件的目的是为了解决控制台提示对象不存在的问题
      if (this.$refs["userForm"] !== undefined) {
        this.$refs["userForm"].resetFields();
      }
      this.dialogFormVisible =true
      if(pid){
        this.form.pid = pid;
      }
      //请求图标数据
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })
    },
    save(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) { // 表单校验合法
          this.request.post("/menu",this.form).then(res =>{
            if (res.code === '200'){
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
            }else {
              this.$message.error("保存失败")
            }
          })
        }
      });
    },
    //修改
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible =true

      //请求图标数据
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })
    },
    // update(){
    //   this.request.post("/menu",this.form).then(res =>{
    //     if (res.code === '200'){
    //       this.$message.success("保存成功")
    //       this.dialogFormVisibleUpdate = false
    //       this.load()
    //     }else {
    //       this.$message.error("保存失败")
    //     }
    //   })
    // },
    reset(){
      this.name = ""
      // this.name = ""
      // this.phone = ""
      this.load()
    },

    //删除
    handleSelectionChange(val){
      this.multipleSelection =val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{},{},{}] =>[1,2,3] 扁平化处理把对象数组转化为纯id数组
      this.request.delete("/menu/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/menu/" + id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功")
            this.load()
          } else {
            this.$message.error("删除失败")
          }
        })
      })
    }
  }
}
</script>

<style scoped>

</style>