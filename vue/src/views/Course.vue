<template>
  <div>
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
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="课程名称" ></el-table-column>
      <el-table-column prop="room" label="授课地点"></el-table-column>
      <el-table-column prop="weekday" label="每周日期"></el-table-column>
      <el-table-column prop="section" label="第几节课"></el-table-column>
      <el-table-column prop="teacher" label="授课老师"></el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.state" active-color="#13ce66" inactive-color="#ccc"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
<!--      <el-table-column prop="createTime" label="创建时间"></el-table-column>-->
<!--      <el-table-column prop="updateTime" label="修改时间"></el-table-column>-->
      <el-table-column width="180" label="操作">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 3px"></i></el-button>
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

    <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="70px" size="small" :model="form"  ref="userForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="授课地点">
          <el-input v-model="form.room" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="每周日期">
          <el-select clearable v-model="form.weekday" placeholder="请选择">
            <el-option v-for="item in ['周一','周二','周三','周四','周五','周六','周日',]" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="第几节课">
          <el-select clearable v-model="form.section" placeholder="请选择">
            <el-option v-for="item in ['一','二','三','四','五']" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="老师">
          <el-select clearable v-model="form.teacherId" placeholder="请选择">
            <el-option v-for="item in teachers" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
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
      this.request.get("/course/page",{
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
      this.dialogFormVisible = true
      this.form = {}
    },
    //修改
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible =true
    },
    save() {
      this.request.post("/course", this.form).then(res => {
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
      this.request.delete("/course/del/batch",{data:ids}).then(res =>{
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
        this.request.delete("/course/" + id).then(res => {
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
      this.request.post("/course/update",row).then(res => {
        if (res.code === '200'){
          this.$message.success("更新成功")
        }
      })
    },
  }
}
</script>

<style scoped>

</style>