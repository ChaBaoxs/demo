<template>
  <div>
    <div style="margin: -5px 0 10px 0">
      <el-input style="width: 200px;" placeholder="请输入用户名" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px;margin-left: 10px" placeholder="请输入计划名称" suffix-icon="el-icon-search" v-model="planName"></el-input>
      <el-button style="margin-left: 10px" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="danger" @click="undis">只显示未分配</el-button>
    </div>

    <el-table :data="tableData" border stripe  >
<!--    <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="订单ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="90"></el-table-column>
      <el-table-column prop="name" label="名称" width="80"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="planName" label="所加计划" ></el-table-column>
      <el-table-column prop="period" label="计划时长" width="100px"></el-table-column>
      <el-table-column label="是否分配课程" width="100px">
        <template slot-scope="scope">
          <div :style="{ color: scope.row.isDetele =='1' ? 'green' : 'red' }">
          {{ String(scope.row.isDetele) === '0' ? '未分配' : '已分配' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="订单创建时间"></el-table-column>

      <el-table-column width="360" label="操作">
        <template slot-scope="scope">
          <el-button type="success" style=" cursor: pointer"  @click="handleEdit(scope.row)">分配课程<i class="el-icon-document" style="margin-left: 3px"></i></el-button>
          <el-button type="primary" @click="lookCourse(scope.row.studentId)">查看学生目前课程<i class="el-icon-view" style="margin-left: 3px"></i> </el-button>
          <el-button v-if="user.role === 'ROLE_ADMIN'"  @click="del(scope.row.id)" type="danger" slot="reference">删除<i class="el-icon-remove-outline" style="margin-left: 3px"></i></el-button>
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

    <el-dialog title="分配课程" :visible.sync="dialogFormVisibleUpdate" width="50%">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="courseAdd">新增<i class="el-icon-circle-plus-outline" style="margin-left: 5px"></i></el-button>
      </div>
      <el-table :data="tableData1" border stripe  >
        <el-table-column prop="username" label="学生姓名" width="90">
          <template v-slot="scope">
            <span>{{name}}</span>
          </template>
        </el-table-column>
        <el-table-column label="加入课程" width="320" >
          <template v-slot="scope">
            <el-select filterable  v-model="scope.row.courseId" placeholder="请选择" style="width: 280px;">
              <el-option
                  v-for="item in courses"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                <span style="float: left">{{ item.name }}</span>
                <span style="float: left;margin-left: 10px">{{ item.teacherName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px;margin-left: 10px">第{{ item.section }}节</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.weekday }}</span>
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="weekday" label="周几" >
          <template v-slot="scope">
            <span v-model="scope.row.weekday" v-if="scope.row.courseId" >{{ courses.find(item => item.id === scope.row.courseId).weekday}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="section" label="第几节课" >
          <template v-slot="scope">
            <span v-if="scope.row.courseId">{{ courses.find(item => item.id === scope.row.courseId).section}}</span>
          </template>
        </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button  @click="delCourse(scope.row.courseId)" type="danger" slot="reference">删除<i class="el-icon-remove-outline" style="margin-left: 3px"></i></el-button>
        </template>
      </el-table-column>
      </el-table>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisibleUpdate = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
    </el-dialog>

    <el-dialog title="课程信息" :visible.sync="vis" width="30%">
      <el-table :data="stuCourses" border stripe>
        <el-table-column prop="name" label="课程名称"></el-table-column>
        <el-table-column prop="weekday" label="周几"></el-table-column>
        <el-table-column prop="section" label="第几节"></el-table-column>
      </el-table>
    </el-dialog>

<!--    <el-button @click="sendMail">发送邮件</el-button>-->

  </div>
</template>

<script>
export default {
  name: "Student",
  data(){
    return{
      tableData:[],
      tableData1:[],
      total:0,
      pageNum:1,
      pageSize:5,
      username:"",
      planName:"",
      isDelete:1,
      dialogFormVisibleUpdate:false,
      vis:false,
      multipleSelection:[],
      form:{},
      courses: [],
      stuCourses: [],
      counts: 0,
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): {},
      name:"",
      studentId:"",
      // mail:{
      //   sendTo:'3294448907@qq.com',
      //   subject:'测试',
      //   text:'测试邮件'
      // }

    }
  },
  created() {
    //请求分页
    this.load()
  },
  methods:{
    // sendMail(){
    //   this.request.post("/send-mail/simple",this.mail)
    // },

    courseAdd(){
      this.tableData1.push({
        courseId:this.courseId,
        studentId:this.studentId
      })
    },

    //分配表格加载
    loadCourse(id){
      this.request.get("/employee/course/"+id).then(res => {
        this.tableData1 = res.data
      })
    },

    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    load(){
      this.request.get("/employee/page/student",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          isDelete: this.isDelete,
          planName: this.planName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/course/courses").then(res => {
        this.courses = res.data
      })

    },

    //修改
    handleEdit(row){
      this.loadCourse(row.studentId)
      this.form = JSON.parse(JSON.stringify(row));
      this.name = row.name
      this.studentId = row.studentId
      this.dialogFormVisibleUpdate =true
    },
     save(){
      //课程查重
      if (new Set(this.tableData1.map(v => v.courseId)).size !== this.tableData1.length){
        this.$message.warning("课程重复")
        return
      }
      //课程查重
      // if (new Set(this.tableData1.map(v => v.weekday + v.section)).size !== this.tableData1.length){
      //   this.$message.warning("课程时间冲突")
      //   return
      // }
      this.request.post("/stuCourse/courses/"+this.studentId,this.tableData1).then(res =>{
        if (res.code === '200'){
          this.updateIs(this.studentId)//load方法先运行导致显示失败
          this.$message.success("保存成功")
          this.dialogFormVisibleUpdate = false
          this.load()
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    //重置
    reset(){
      this.username = ""
      this.isDelete = 1
      this.load()
    },

    //搜索未分配
    undis(){
      this.isDelete = 0
      this.load()
    },

    //更改分配状态
    updateIs(studentId){
      this.request.get("/order/updateIs/"+studentId)
    },

    //查看课程
    lookCourse(id){
      this.request.get("/employee/course/" + id).then(res => {
        this.stuCourses = res.data
      })
      this.vis = true;
    },

    //删除课程
    delCourse(courseId){
      this.$confirm('确认删除该课程?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
      const index = this.tableData1.findIndex(v =>v.courseId === courseId)
      this.tableData1.splice(index,1)
      })
    },

    del(id) {
      this.$confirm('确认删除该订单?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'warning'
      }).then(() => {
        this.request.delete("/order/" + id).then(res => {
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