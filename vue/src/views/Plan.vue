<template>
  <div>
<!--    <img v-if="form.coverUrl" :src="form.coverUrl" class="avatar">-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
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
    </div>

    <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
<!--      <el-table-column prop="id" label="ID" width="80"></el-table-column>-->
      <el-table-column prop="name" label="计划名称" ></el-table-column>
      <el-table-column prop="price" label="价格"></el-table-column>
      <el-table-column prop="beginTime" label="开始时间"></el-table-column>
      <el-table-column prop="period" label="培训时长"></el-table-column>
      <el-table-column prop="introduce" label="介绍"></el-table-column>
      <el-table-column prop="trainNumber" label="培训人数"></el-table-column>
      <el-table-column prop="url" label="视频封面" width="100">
        <template v-slot:default="scope">
          <el-image :src="scope.row.coverUrl"/>
        </template>
      </el-table-column>
      <el-table-column label="是否展示">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="计划状态">
        <template slot-scope="scope">
          {{ String(scope.row.status) === '0' ? '已结束' : '进行中' }}
          <el-button
              type="text"
              size="small"
              class="delBut non"
              @click="statusHandle(scope.row)">


            {{ scope.row.status == '1' ? '结束此计划' : '启用此计划' }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column width="270" label="操作">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 3px"></i></el-button>
          <el-button  @click="del(scope.row.id)" type="danger" slot="reference">删除<i class="el-icon-remove-outline" style="margin-left: 3px"></i></el-button>
<!--          <el-button @click="report(scope.row)" type="success" v-if="user.role === 'ROLE_ADMIN'|| user.role === 'ROLE_MANAGER'">生成报告</el-button>-->
          <el-button @click="report(scope.row)" type="success" v-if="scope.row.status=='0' ">生成报告</el-button>
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

    <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="form"  ref="userForm">
        <el-form-item label="计划名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" autocomplete="off" placeholder="单位/元"></el-input>
        </el-form-item>
        <el-form-item label="活动时间">
            <el-date-picker value-format="yyyy-MM-dd" type="date" placeholder="选择日期" v-model="form.beginTime" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="培训时长">
          <el-input v-model="form.period" autocomplete="off" placeholder="单位/周"></el-input>
        </el-form-item>
        <el-form-item label="介绍">
          <el-input v-model="form.introduce" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计划封面">
          <el-upload
              class="avatar-uploader"
              :action="'http://localhost:9090/file/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.coverUrl" :src="form.coverUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
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
  name: "course.vue",
  data(){
    return{
      tableData:[],
      name:'',
      multipleSelection:[],
      teachers:[],
      form:{},
      dialogFormVisible:false,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): {},
      total:0,
      pageNum:1,
      pageSize:5
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
      this.request.get("/plan/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/employee/role/ROLE_TEACHER").then(res => {
        this.teachers = res.data
      })
    },
    reset(){
      this.name = ""
      this.load()
    },
    handleAdd() {
      this.form = {}
      this.dialogFormVisible = true
    },
    //修改
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible =true
    },
    save() {
      this.request.post("/plan", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    //删除
    handleSelectionChange(val){
      this.multipleSelection =val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{},{},{}] =>[1,2,3] 扁平化处理把对象数组转化为纯id数组
      this.request.delete("/plan/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/plan/" + id).then(res => {
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
      this.request.post("/plan/update",row).then(res => {
        if (res.code === '200'){
          this.$message.success("更新成功")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.coverUrl = res
      console.log("*****"+this.form.coverUrl)
    },

    //状态修改
    statusHandle (row) {
      this.id = row.id
      this.status = row.status
      this.$confirm('确认调整该计划的状态?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
        this.request.put("/plan",{ 'id': this.id, 'status': !this.status ? 1 : 0 }).then(res => {
          if (res.code === '200') {
            this.$message.success('计划状态更改成功！')
            this.load()
          }
        }).catch(err => {
          this.$message.error('请求出错了：' + err)
        })
      })
    },

    //生成报告
    report(row){
      this.request.post("/example/createPdfLocal",row).then(res => {
        if (res.code === '200'){
          this.$message.success("综合报告生成成功")
        }
      })
    },

    //上传校验
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>