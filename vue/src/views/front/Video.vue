<template>
  <div style="color: #666;margin: 20px 100px 100px 100px">
    <h1 style="float: left;margin-right: 15px;color: #5392a6">知识专区:</h1>
    <div style="margin-bottom: 20px">
      <h1 class="article" @click="article()">文章讨论区</h1>
      <h1 class="video" @click="video()">视频分享区</h1>
    </div>
    <div style="margin: 10px 0">
      <el-input size="small" style="width: 300px" placeholder="请输入视频名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="found" size="small">搜索</el-button>
      <el-button type="warning" @click="reset" size="small">重置</el-button>
    </div>
    <el-card>
      <div v-for="item in videos" :key="item.id" style="margin: 10px 0; padding: 10px 0; color: #666; border-bottom: 1px dashed #ccc">
        <span><el-image :src="item.coverUrl"  style="width: 150px;height: 100px"/></span>
        <span style="font-size: 22px; cursor: pointer" class="item" @click="detail(item.id)">{{ item.name }}</span>
        <span style="float: right; font-size: 15px; margin-top: 10px;margin-right: 20px">所属课程：{{ item.name }} </span>
        <span style="float: right; font-size: 15px; margin-top: 10px;margin-right: 20px">创建时间：{{ item.createTime }} </span>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Video",
  data() {
    return {
      videos: [],
      id : this.$route.query.id,
      name:""
    }
  },
  created() {
    this.load()
    console.log(this.id)
  },
  methods: {
    detail(id) {
      this.$router.push({path: "/front/videoDetail", query: {id: id}})
    },
    load(){
      if(this.id != null){
        this.request.get("/videoFile/findAll/"+this.id).then(res => {
          this.videos = res.data
          // this.videos = res.data.filter(v => v.type === 'mp4')
        })
      }else {
        this.request.get("/videoFile/findAll").then(res => {
          console.log(res.data)
          this.videos = res.data
          // this.videos = res.data.filter(v => v.type === 'mp4')
        })
      }
    },

    article() {
      this.$router.push({path: "/front/Article"})
    },

    video() {
      this.$router.push({path: "/front/Video"})
    },
    reset() {
      this.name = ""
      this.load()
    },
    found(){
      this.request.get("/videoFile/findAll",{
        params:{
          name: this.name,
        }
      }).then(res => {
        this.videos = res.data
      })
    }
  }
}
</script>

<style>
.item{
  margin-left: 20px;
}
.item:hover{
  color: #3a8ee6;
  background: none;
}

.article{
  float: left;
  margin-right: 10px
}

.article:hover{
  color: #5392a6;
  background: none;
  cursor: pointer;
}

.video:hover{
  color: #5392a6;
  background: none;
  cursor: pointer;
}
</style>
