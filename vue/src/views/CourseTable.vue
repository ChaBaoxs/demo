<template>
<div>
  <div style="font-size: 24px;margin: 0px 0 20px 0 ">{{user.name}}的课表</div>

  <el-table border stripe :data="tableData" :header-cell-class-name="'headerBg'">
    <el-table-column label="时间/课程">
      <template v-slot="scope">
        <div v-if="scope.row.section">
          <div style="margin: 10px 0;color:#304156;font-weight: bold">{{scope.row.section.num}}</div>
          <div style="margin: 10px 0;color:#304156;font-weight: bold">{{scope.row.section.time}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周一">
      <template v-slot="scope">
        <div v-if="scope.row.mon">
          <div style="margin: 10px 0;">{{scope.row.mon.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.mon.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.mon.teacherName}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周二">
      <template v-slot="scope">
        <div v-if="scope.row.tue">
          <div style="margin: 10px 0;">{{scope.row.tue.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.tue.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.tue.teacherName}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周三">
      <template v-slot="scope">
        <div v-if="scope.row.wes">
          <div style="margin: 10px 0;">{{scope.row.wes.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.wes.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.wes.teacherName}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周四">
      <template v-slot="scope">
        <div v-if="scope.row.thu">
          <div style="margin: 10px 0;">{{scope.row.thu.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.thu.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.thu.teacherName}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周五">
      <template v-slot="scope">
        <div v-if="scope.row.fri">
          <div style="margin: 10px 0;">{{scope.row.fri.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.fri.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.fri.teacherName}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周六">
      <template v-slot="scope">
        <div v-if="scope.row.sat">
          <div style="margin: 10px 0;">{{scope.row.sat.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.sat.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.sat.teacherName}}</div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="周日">
      <template v-slot="scope">
        <div v-if="scope.row.sun">
          <div style="margin: 10px 0;">{{scope.row.sun.name}}</div>
          <div style="margin: 10px 0;">{{scope.row.sun.room}}</div>
          <div style="margin: 10px 0;">{{scope.row.sun.teacherName}}</div>
        </div>
      </template>
    </el-table-column>

  </el-table>
</div>
</template>

<script>
export default {
  name: "CourseTable",
  data(){
    return{
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): {},
      tableData:[],


    }
  },
  created() {
    if(this.user.role === 'ROLE_STUDENT'){
      this.request.get("/stuCourse/courseTable/"+this.user.id).then(res => {
        this.tableData = res.data
      })
    }

    if(this.user.role === 'ROLE_TEACHER'){
      this.request.get("/course/courseTable/"+this.user.id).then(res => {
        this.tableData = res.data
      })
    }

  }
}
</script>

<style>
.headerBg{
  background: #eee!important;
  font-size: 16px;
}
</style>