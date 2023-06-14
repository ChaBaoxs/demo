<template>

<div>
  <div style="color: #666;font-size: 40px;">
    <b>欢迎进入培训教育后台管理系统！
      <span v-if="user.role === 'ROLE_ADMIN'" style="color: #3a8ee6;text-decoration: none;">{{ user.name }}</span>
      <span v-if="user.role === 'ROLE_STUDENT'" style="color: #3a8ee6;text-decoration: none;">{{ user.name }}</span>
      <span v-if="user.role === 'ROLE_TEACHER'" style="color: #3a8ee6;text-decoration: none;">{{ user.name }}</span>
      <el-badge :value="isDelete" class="item" style="margin-top: -7px" v-if="user.role === 'ROLE_MANAGER'">
      <router-link to="/student" style="color: #3a8ee6;text-decoration: none;">{{ user.name }}</router-link>
      </el-badge>
    </b>
  </div>



  <div style="color: #666;font-size: 30px;margin-top: 20px">
    <b>培训计划展示</b>
  </div>
  <div style="margin: 10px 0px">
    <el-row :gutter="40">
      <el-col :span="6" v-for="item in files" :key="item.id" style="margin-bottom: 10px">
        <el-card style="border: 1px solid #ccc; padding-bottom: 10px">
          <img :src="item.coverUrl" alt="" style="width: 250px;height: 250px">
          <b style="color: #666; font-size: 20px">{{ item.name }}</b>
          <div>培训价格：{{ item.price }}元</div>
          <div>培训时长：{{ item.period }}</div>
          <div>开始时间：{{ item.beginTime }}</div>
          <div>内容介绍：{{ item.introduce }}</div>
          <div>参加人数：{{ item.trainNumber }}人</div>
          <el-button @click="save(item.id)" type="success" class="enroll">我要报名<i class="el-icon-thumb" style="margin-left: 3px"></i></el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>

  <el-dialog :visible.sync="dialog1" title="评价界面" width="35%">
      <h2 style="margin:-30px 0 10px 0;text-align: center">计划评价</h2>
      <el-form label-width="90px" size="small" :model="form"  ref="userForm">
        <el-form-item label="计划名称:" prop="name">
          <span style="font-weight: bold">{{form.name}}</span>
        </el-form-item>
        <el-form-item label="计划打分:">
          <el-rate
              v-model="form.score"
              :colors="colors"
              :texts="texts"
              show-text>
          </el-rate>
        </el-form-item>
        <el-form-item label="评价话语：">
          <el-input
              type="textarea"
              placeholder="请输入内容"
              v-model="form.comment"
              maxlength="30"
              show-word-limit
          >
          </el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog1 = false">取 消</el-button>
        <el-button type="primary" @click="savePlan">确 定</el-button>
      </div>
  </el-dialog>


</div>


</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      files: [],
      msg: [],
      isDelete: 0,
      isScore: 0,
      status: 2,
      dialog1:false,
      form:{},
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      texts: ['1分', '2分', '3分', '4分', '5分好评'],
      tableData:[],
      courses: [],
    }
  },
  created() {
    this.load()

  },

  mounted () {
    //监听鼠标点击事件
    document.addEventListener('mouseup', (e) => {
      let _track = document.getElementById('messageTrack1');
      if (_track) {
        if (_track.contains(e.target)) {
          this.toMy1();//事件
        }
      }
    });

    //监听鼠标点击事件
    document.addEventListener('mouseup', (e) => {
      let _track = document.getElementById('messageTrack2');
      if (_track) {
        if (_track.contains(e.target)) {
          this.toMy2();//事件
        }
      }
    });
  },

  methods:{
    async load(){

      this.request.get("/plan").then(res => {
        this.files = res.data
      })

      this.request.get("/order/isDelete").then(res => {
        this.isDelete = res.data
        if (this.isDelete>0 && this.user.role === 'ROLE_MANAGER'){
          this.open()
        }
      })

      await this.getIsScore()

      await this.getStatus()
      this.$forceUpdate();//刷新页面
    },

    getIsScore(){
      this.request.get("/stuCourse/isScore/"+ this.user.id).then(res => {
        console.log("=========="+ res.data)
        this.isScore = res.data
      })
    },

    getStatus(){
      this.request.get("/order/status/"+ this.user.id).then(res => {
        this.status = res.data
        if (this.user.role === 'ROLE_STUDENT'){

          if (this.status.status==0 && this.status.isComment==0 ){
            this.open1()
            this.form.name = this.status.name
            this.form.planId = this.status.id
            this.form.studentId =  this.user.id

            if(this.isScore>0){
              this.open2()
              console.log("********"+this.isScore)
            }
          }


        }
      })
    },

    save(planId) {
      this.$confirm('请注意时间信息，确认选择该计划?', '提示', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        'type': 'success'
      }).then(() => {
        this.request.post("/plan/studentPlan/"+ planId + "/" + this.user.id ).then(res => {
          if (res.code === '200') {
            this.$message.success("加入成功")
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      })
    },

    savePlan(){
      this.request.post("/planComment/planScore",this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("评价成功")
          this.dialog1 = false
          // this.load()
          this.isComment()
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    //更改是否评价
    isComment(){
      this.request.get("/order/isComment/" +this.user.id)
    },



    open() {
      const h = this.$createElement;

      this.$notify({
        title: '有新学生参与培训计划',
        message: h('i', { style: 'color: teal'}, '请尽快分配课程,或查看是否有学生还未分配课程'),
        type: 'success'
      });
    },

    open1() {
      const h = this.$createElement;

      this.$notify({
        title: '你有参与的计划已结束',
        // message: h('i', { style: 'color: teal'}, '请尽快分配课程,或查看是否有学生还未分配课程'),
        message:'点击为计划评分 <span style="font-style: normal;color: #17e1e1;text-decoration: underline;cursor: pointer;" id="messageTrack1">我要评分</span>',
        type: 'success',
        duration:8000,
        dangerouslyUseHTMLString: true,
      });
    },

    toMy1 () {
      this.dialog1 = true
    },

    open2() {
      const h = this.$createElement;

      this.$notify({
        title: '你有参与的课程已结束',
        // message: h('i', { style: 'color: teal'}, '请尽快分配课程,或查看是否有学生还未分配课程'),
        message:'点击为课程评分 <span style="font-style: normal;color: #67C23A;text-decoration: underline;cursor: pointer;" id="messageTrack2">我要评分</span>',
        type: 'success',
        offset: 80,
        duration:8000,
        dangerouslyUseHTMLString: true,
      });
    },

    toMy2 () {
      this.$router.push({path: "/score"})
    },




  }
}
</script>

<style>
.enroll{
  margin-top: 10px;
  font-size: 16px;
  font-weight: bold;
}

</style>
