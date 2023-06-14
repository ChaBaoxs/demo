<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
<!--      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline" style="margin-left: 5px"></i></el-button>-->
      <el-upload :action="'http://localhost:9090/videoFile/upload'" :show-file-list="false"
                 :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传视频<i class="el-icon-top"></i></el-button>
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
      <el-table-column prop="name" label="视频名称" ></el-table-column>
      <el-table-column prop="type" label="视频类型"></el-table-column>
      <el-table-column prop="cname" label="所属课程"></el-table-column>
      <el-table-column prop="url" label="视频封面" width="100">
        <template v-slot:default="scope">
          <el-image :src="scope.row.coverUrl"/>
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
      <el-table-column prop="createTime" label="创建时间" width="140"></el-table-column>
      <el-table-column width="220" label="操作">
        <template slot-scope="scope">
          <el-button  @click="handleEdit(scope.row)" type="success" slot="reference">分配课程<i class="el-icon-collection" style="margin-left: 3px"></i></el-button>
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

    <el-dialog title="视频信息" :visible.sync="dialogFormVisibleSelect" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="视频封面">
        <el-upload
            class="avatar-uploader"
            :action="'http://localhost:9090/videoFile/cover/upload'"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.coverUrl" :src="form.coverUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        </el-form-item>
        <el-form-item label="视频名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所选课程">
        <el-select v-model="form.courseId" filterable placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.name"
              :label="item.name"
              :value="item.id">
            {{ item.name }}
          </el-option>
        </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleSelect = false">取 消</el-button>
        <el-button type="primary" @click="select">确 定</el-button>
      </div>
    </el-dialog>

<!--    <el-dialog title="视频信息" :visible.sync="dialogFormVisible" width="30%">-->
<!--      <el-form label-width="120px" size="small" >-->
<!--        <el-form-item label="上传视频">-->
<!--        <el-upload :action="'http://localhost:9090/videoFile/upload'" :show-file-list="false"-->
<!--                   :on-success="handleFileUploadSuccess" style="display: inline-block">-->
<!--          <el-button type="primary" class="ml-5">上传视频<i class="el-icon-top"></i></el-button>-->
<!--        </el-upload>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="视频封面">-->
<!--          <el-upload-->
<!--              class="avatar-uploader"-->
<!--              :action="'http://localhost:9090/videoFile/cover/upload'"-->
<!--              :show-file-list="false"-->
<!--              :on-success="handleAvatarSuccess"-->
<!--          >-->
<!--            <img v-if="form.coverUrl" :src="form.coverUrl" class="avatar">-->
<!--            <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--          </el-upload>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="视频名称">-->
<!--          <el-input v-model="form.name" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="所属课程">-->
<!--          <el-select v-model="form.courseId" filterable placeholder="请选择">-->
<!--            <el-option-->
<!--                v-for="item in options"-->
<!--                :key="item.name"-->
<!--                :label="item.name"-->
<!--                :value="item.id">-->
<!--              {{ item.name }}-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="select">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
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
      dialogFormVisibleSelect:false,
      dialogFormVisible:false,
      form:{},
      total:0,
      pageNum:1,
      pageSize:5,
      options:[],
    }
  },
  created() {
    //请求分页
    this.load()
    //请求课程名称信息
    this.request.get("/videoFile/course").then(res => {
      this.options = res.data
    })
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
      this.request.get("/videoFile/page",{
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
      this.request.delete("/videoFile/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/videoFile/" + id).then(res => {
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
      this.request.post("/videoFile/down",row).then(res => {
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
      this.request.post("/videoFile/update",row).then(res => {
        if (res.code === '200'){
          this.$message.success("更新成功")
        }
      })
    },
    //预览
    preview(url) {
      window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
    },

    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));

      this.dialogFormVisibleSelect =true
    },
    //分配课程
    select(){
      this.request.post("/videoFile/update",this.form).then(res =>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisibleSelect = false
          this.dialogFormVisible = false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },

    handleAvatarSuccess(res) {
      this.form.coverUrl = res
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
  }
}
</script>

<style scoped>

</style>