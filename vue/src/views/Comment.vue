<template>
<div>
  <div style="margin: -5px 0 10px 0">
    <el-select v-model="articleId1" filterable placeholder="请选择文章发布人">
      <el-option
          v-for="item in options"
          :key="item.user"
          :label="item.user"
          :value="item.id">
      </el-option>
    </el-select>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
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

  <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="content" label="评论内容"></el-table-column>
    <el-table-column prop="contentName" label="评论人"></el-table-column>
    <el-table-column prop="articleName" label="文章名称"></el-table-column>
    <el-table-column prop="username" label="文章发布人"></el-table-column>
    <el-table-column prop="createTime" label="创建时间"></el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
<!--        <el-button @click="handleEdit(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 3px"></i></el-button>-->
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

  <el-dialog title="评论信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="60px" size="small" :model="form" :rules="rules" ref="userForm">
      <el-form-item label="内容" prop="name">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="文字名称">
        <el-input v-model="form.flag" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" autocomplete="off"></el-input>
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
      articleId1:"",
      dialogFormVisible:false,
      multipleSelection:[],
      form:{},
      props:{
        label: 'name'
      },
      ids: [],
      options:[],

      //校验规则
      rules: {
        name: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
        ]
      }

    }
  },
  created() {
    //请求分页
    this.load()
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
      this.request.get("/comment/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          articleId1: this.articleId1,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      //请求发布人信息
      this.request.get("/article").then(res => {
        this.options = res.data
      })
    },
    //新增
    handleAdd(){
      this.form ={}
      //加if判断条件的目的是为了解决控制台提示对象不存在的问题
      if (this.$refs["userForm"] !== undefined) {
        this.$refs["userForm"].resetFields();
      }
      this.dialogFormVisible =true
    },
    save(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) { // 表单校验合法
          this.request.post("/comment",this.form).then(res =>{
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
    saveRoleMenu(){
      this.request.post("/comment/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())).then(res => {
        if (res.code === '200') {
          this.$message.success("绑定成功")
          this.menuDialogVis = false

          // 操作管理员角色后需要重新登录
          // if (this.roleFlag === 'ROLE_ADMIN') {
          //   this.$store.commit("logout")
          // }

        } else {
          this.$message.error(res.msg)
        }
      })
    },
    //修改
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible =true
    },

    reset(){
      this.articleId1 = ""
      this.load()
    },

    //删除
    handleSelectionChange(val){
      this.multipleSelection =val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{},{},{}] =>[1,2,3] 扁平化处理把对象数组转化为纯id数组
      this.request.delete("/comment/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/comment/" + id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功")
            this.load()
          } else {
            this.$message.error("删除失败")
          }
        })
      })
    },



  }
}
</script>

<style scoped>

</style>