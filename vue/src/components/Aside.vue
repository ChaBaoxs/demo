<template>
  <el-menu :default-openeds="opens" style="min-height: 100%;overflow-x: hidden"
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffb700"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px;line-height: 60px;text-align: center;color: #fff">
      <img src="../assets/haishi.png" alt="" id="logo">
      <b v-show="logoTextShow" style="margin-left: 5px">后台管理系统</b>
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path" style="width: 200px">
          <i :class="item.icon" ></i>
          <template slot="title">{{ item.name }}</template>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path" style="width: 200px">
              <i :class="subItem.icon" ></i>
              <template slot="title">{{ subItem.name }}</template>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,
  },
  data(){
    return{
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  },
  created() {
  }
}
</script>

<style scoped>
#logo{
  width: 70px;
  height: 30px;
  margin-bottom: -10px;
}
</style>