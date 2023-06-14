<template>
<div style="font-size: 12px;line-height: 60px;display: flex">
  <div style="flex: 1;font-size: 18px">
    <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
  </div>

  <div style="flex: 50;margin: 23px 0">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="'/'">主页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-dropdown style="width:100px;cursor: pointer">
    <div style="display: inline-block">
      <img :src="user.avatarUrl" alt=""
           style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
      <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item>
        <router-link to="/person" style="text-decoration: none;color: #666666">个人信息</router-link>
      </el-dropdown-item>
      <el-dropdown-item >
        <router-link to="/password" style="text-decoration: none;color: #666666">修改密码</router-link>
      </el-dropdown-item>
      <el-dropdown-item >
        <span style="text-decoration: none" @click="logout">退出</span>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    user: Object//从Mange.vue接收过来信息
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　//需要监听的数据
    }
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },
  data(){
    return {

    }
  },
  methods:{
    collapse() {
      // this.$parent.$parent.$parent.$parent.collapse()  // 通过4个 $parent 找到父组件，从而调用其折叠方法
      this.$emit("asideCollapse")
    },
    logout(){
      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style scoped>

</style>