<template>
  <div style="padding: 10px">
    <el-card>
      <div v-for="item in videos" :key="item.id" style="margin: 10px 0; padding: 10px 0; color: #666; border-bottom: 1px dashed #ccc">
        <span>视频封面：<el-image :src="item.coverUrl"  style="width: 100px;height: 100px"/></span>
        <span style="font-size: 18px; cursor: pointer" class="item" @click="detail(item.id)">{{ item.name }}</span>
        <span style="float: right; font-size: 12px; margin-top: 10px">文件大小：{{ item.size }} kb</span>
        <span style="float: right; font-size: 12px; margin-top: 10px;margin-right: 20px">所属课程：{{ item.cname }} </span>
        <span style="float: right; font-size: 12px; margin-top: 10px;margin-right: 20px">创建时间：{{ item.createTime }} </span>
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
      id : this.$route.query.id
    }
  },
  created() {
    this.load()
    console.log(this.id)
  },
  methods: {
    detail(id) {
      this.$router.push({path: "/videoDetail", query: {id: id}})
    },
    load(){
      if(this.id != null){
        this.request.get("/videoFile/findAll/"+this.id).then(res => {
          console.log(res.data)
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
</style>
