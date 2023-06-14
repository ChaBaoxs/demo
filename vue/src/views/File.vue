<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-upload :action="'http://localhost:9090/file/upload'" :show-file-list="false"
                 :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传文件<i class="el-icon-top"></i></el-button>
      </el-upload>
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
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名称" ></el-table-column>
      <el-table-column prop="type" label="文件类型"></el-table-column>
      <el-table-column prop="size" label="文件大小（kb）"></el-table-column>

      <el-table-column prop="url" label="图片" width="100">
        <template v-slot:default="scope">
          <el-image :src="scope.row.url"/>
        </template>
      </el-table-column>

      <el-table-column label="文件预览">
        <template slot-scope="scope">
          <el-button type="primary" @click="preview(scope.row.url)">预览</el-button>
        </template>
      </el-table-column>
      <el-table-column  label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
<!--      <el-table-column prop="updateTime" label="修改时间"></el-table-column>-->
      <el-table-column width="180" label="操作">
        <template slot-scope="scope">
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
  </div>
</template>

<script>
export default {
  name: "File.vue",
  data(){
    return{
      tableData:[],
      name:'',
      multipleSelection:[],
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
      this.request.get("/file/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset(){
      this.name = ""
      this.load()
    },


    //删除
    handleSelectionChange(val){
      this.multipleSelection =val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v =>v.id) //[{},{},{}] =>[1,2,3] 扁平化处理把对象数组转化为纯id数组
      this.request.delete("/file/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/file/" + id).then(res => {
          if (res.code === '200') {
            this.$message.success("删除成功")
            this.load()
          } else {
            this.$message.error("删除失败")
          }
        })
      })
    },

    //图片上传
    handleFileUploadSuccess(res){
      console.log(res)
      this.load()
    },
    //图片下载
    download(row){
      // window.open(url)
      this.request.post("/file/down",row).then(res => {
        if (res.code === '200'){
          this.$message.success("下载获取成功")
          window.open(row.url)
        }else {
          this.$message.error(res.msg)
        }
      })
    },

    //启用
    changeEnable(row){
      this.request.post("/file/update",row).then(res => {
        if (res.code === '200'){
          this.$message.success("更新成功")
        }
      })
    },
    //预览
    preview(url) {
      window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
    }
  }
}
</script>

<style scoped>

</style>