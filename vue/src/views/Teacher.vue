<template>
<div>
  <div style="margin: -5px 0 0 0">
    <el-input style="width: 200px;" placeholder="请输入用户名" suffix-icon="el-icon-search" v-model="username"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>

  <div style="margin: 10px 0">
    <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline" style="margin-left: 5px"></i></el-button>
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
    <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom" style="margin-left: 5px"></i></el-button>
    <el-button type="primary">导出<i class="el-icon-top" style="margin-left: 5px"></i></el-button>
  </div>

  <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
<!--    <el-table-column prop="id" label="ID" width="80"></el-table-column>-->
    <el-table-column prop="username" label="用户名" width="90"></el-table-column>
    <el-table-column prop="name" label="名称" width="120"></el-table-column>
    <el-table-column prop="idNumber" label="身份证号码"></el-table-column>
    <el-table-column prop="phone" label="电话"></el-table-column>
    <!--          <el-table-column prop="status" label="状态"></el-table-column>-->
    <el-table-column label="账号状态">
      <template slot-scope="scope">
        {{ String(scope.row.status) === '0' ? '已禁用' : '正常' }}
        <el-button
            type="text"
            size="small"
            class="delBut non"
            @click="statusHandle(scope.row)"
            v-if="user.role === 'ROLE_ADMIN'">


          {{ scope.row.status == '1' ? '禁用此账号' : '启用此账号' }}
        </el-button>
      </template>
    </el-table-column>
    <el-table-column prop="updateUser" label="修改人"></el-table-column>
    <el-table-column width="440" label="操作">
      <template slot-scope="scope">
        <el-button  type="primary" @click="lookCourse(scope.row.courses)">查看课程<i class="el-icon-document" style="margin-left: 3px"></i></el-button>
        <el-button type="success" style=" cursor: pointer"  @click="detail(scope.row.id)">查看视频教材<i class="el-icon-document" style="margin-left: 3px"></i></el-button>
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

  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="60px" size="small" :model="form" :rules="rules" ref="userForm">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色">
        <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="昵称" prop="name">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="身份证号码" prop="idNumber">
        <el-input v-model="form.idNumber" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.sex" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>

  <el-dialog title="用户信息" :visible.sync="dialogFormVisibleUpdate" width="30%">
    <el-form label-width="60px" size="small">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="角色">
        <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="身份证号码">
        <el-input v-model="form.idNumber" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.sex" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisibleUpdate = false">取 消</el-button>
      <el-button type="primary" @click="update">确 定</el-button>
    </div>
  </el-dialog>

  <el-dialog title="课程信息" :visible.sync="vis" width="30%">
    <el-table :data="courses" border stripe>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="weekday" label="周几"></el-table-column>
      <el-table-column prop="section" label="第几节"></el-table-column>
    </el-table>
  </el-dialog>
</div>
</template>

<script>
export default {
  name: "Teacher",
  data(){
    return{
      tableData:[],
      total:0,
      pageNum:1,
      pageSize:5,
      username:"",
      name:"",
      sex:"",
      email:"",
      phone:"",
      status:"",
      dialogFormVisible:false,
      dialogFormVisibleUpdate:false,
      vis:false,
      multipleSelection:[],
      form:{},
      roles: [],
      courses: [],
      counts: 0,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): {},

      //校验规则
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        idNumber: [
          {required: true, message: '请输入身份证号码', trigger: 'blur'},
          {pattern:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '输入正确身份证号码', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: '请输入电话号码', trigger: 'blur'},
          {pattern:/^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/, message: '输入正确电话号码', trigger: 'blur'}
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: "blur" },
          { pattern:/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: '输入正确邮箱类型', trigger: "blur"}
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
      this.request.get("/employee/page/teacher",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      // this.request.get("/role").then(res => {
      //   this.roles = res.data
      // })
    },
    lookCourse(courses){
      this.courses = courses;
      this.vis = true;
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
          this.request.post("/employee",this.form).then(res =>{
            if (res.code === '200'){
              this.$message.success("新增成功")
              this.dialogFormVisible = false
              this.load()
            }else {
              this.$message.error("新增失败")
            }
          })
        }
      });
    },
    //修改
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisibleUpdate =true
    },
    update(){
      this.request.post("/employee",this.form).then(res =>{
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
      this.username = ""
      // this.name = ""
      // this.phone = ""
      this.load()
    },


    //状态修改
    statusHandle (row) {
      this.id = row.id
      this.status = row.status
      this.$confirm('确认调整该账号的状态?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
        this.request.put("/employee",{ 'id': this.id, 'status': !this.status ? 1 : 0 }).then(res => {
          console.log('enableOrDisableEmployee',res)
          if (res.code === '200') {
            this.$message.success('账号状态更改成功！')
            this.load()
          }
        }).catch(err => {
          this.$message.error('请求出错了：' + err)
        })
      })
    },

    //删除
    handleSelectionChange(val){
      this.multipleSelection =val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{},{},{}] =>[1,2,3] 扁平化处理把对象数组转化为纯id数组
      this.request.delete("/employee/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/employee/" + id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功")
            this.load()
          } else {
            this.$message.error("删除失败")
          }
        })
      })
    },

    detail(id) {
      this.$router.push({path: "/video", query: {id: id}})
    }
  }
}
</script>

<style scoped>

</style>