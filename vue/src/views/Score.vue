<template>
<div>
<h1 style="margin-bottom: 20px;font-size: 30px">课程评分</h1>
  <el-table :data="tableData" border stripe  style="margin-bottom: 20px">
    <el-table-column prop="name" label="课程名称" ></el-table-column>
    <el-table-column prop="teacher" label="老师名称" ></el-table-column>
    <el-table-column prop="courseScore" label="你的评分" ></el-table-column>
    <el-table-column label="课程评分" >
      <template slot-scope="scope">
      <el-button type="success" @click="handleScore(scope.row)">立即评分</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog :visible.sync="dialog" title="评分界面" width="35%">
    <h2 style="margin:-30px 0 10px 0;text-align: center">课程评分</h2>
    <el-form label-width="90px" size="small" :model="form"  ref="userForm">
      <el-form-item label="课程名称:" prop="name">
        <span style="font-weight: bold">{{form.name}}</span>
      </el-form-item>
      <el-form-item label="计划打分:">
        <el-rate
            v-model="form.courseScore"
            :colors="colors"
            :texts="texts"
            show-text>
        </el-rate>
      </el-form-item>

    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialog = false">取 消</el-button>
      <el-button type="primary" @click="saveCourse">确 定</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
export default {
  name: "Score",
  data(){
    return{
      tableData:[],
      name:"",
      multipleSelection:[],
      form:{},
      options:[],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      dialog:false,
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      texts: ['1分', '2分', '3分', '4分', '5分好评'],
      status: 2,

    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      this.request.get("/order/status/"+ this.user.id).then(res => {
            this.status = res.data
            if (this.status.status==0){
            this.request.get("/stuCourse/courses/" + this.user.id).then(res => {
              this.tableData = res.data
            })
            }
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

    handleScore(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialog =true
    },

    saveCourse(){
      this.request.post("/stuCourse/courseScore" ,this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("评价成功")
          this.dialog = false
          this.load()
          // this.isComment()
        } else {
          this.$message.error(res.msg)
        }
      })
    },




  }
}
</script>

<style scoped>

</style>