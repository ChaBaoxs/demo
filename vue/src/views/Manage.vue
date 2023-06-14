<template>
  <el-container style="min-height: 100vh; ">
    <el-aside width="sideWith +'px'" style="background-color: rgb(238, 241, 246);box-shadow: #c5cfd3 2px 0px 6px ">
      <Aside :is-collapse="isCollapse" :logo-text-show="logoTextShow"/>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user" />
      </el-header>

      <el-main>
<!--    表示当前页面的子路由会在 <router-view /> 里面展示-->
        <router-view @refreshUser="getUser"/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass:'el-icon-s-grid',
      isCollapse:false,
      sideWith:200,
      logoTextShow:true,
      user: {}
    }
  },
  created() {
    //从后台获取最新的user数据
    this.getUser()
  },
  components: {
    Aside,
    Header
  },

  methods:{
    collapse(){//点击收缩按钮
      this.isCollapse = !this.isCollapse
      if (this.isCollapse){//收缩
        this.sideWith=64
        this.collapseBtnClass = 'el-icon-menu'
        this.logoTextShow = false
      }else{ //展开
        this.collapseBtnClass = 'el-icon-s-grid'
        this.logoTextShow = true
      }
    },
    getUser() {
      let id = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).id :""
      //从后台获取User数据
      this.request.get("/employee/id/" + id).then(res =>{
        console.log("Mange获取user信息")
        //重新赋值后台最新的User数据
        this.user = res.data
      })
    }

  }
}
</script>
