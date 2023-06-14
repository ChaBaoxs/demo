<template>
  <el-card style="width: 500px;">
    <el-form label-width="80px" size="small" :model="form" :rules="rules" ref="userForm">
      <el-upload
          class="avatar-uploader"
          :action="'http://localhost:9090/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称"  prop="name">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="身份证号码"  prop="idNumber">
        <el-input v-model="form.idNumber" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱"  prop="email">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话"  prop="phone">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别"  prop="sex">
<!--        <el-input v-model="form.sex" autocomplete="off"></el-input>-->

          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>

      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>

export default {
  name: "Person",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},//将浏览器中的字符串信息解析为json

      //校验规则
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        idNumber: [
          {required: true, message: '请输入身份证号码', trigger: 'blur'},
          {pattern:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '输入正确身份证号码', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: '请输入电话号码', trigger: 'blur'},
          {pattern:/^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/, message: '输入正确电话号码', trigger: 'blur'}
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: "blur" },
          { pattern:/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: '输入正确邮箱类型', trigger: "blur"}
        ]
      },

      radio: '男',
    }
  },
  created() {
    this.getUser().then(res => {
      console.log(res)
      this.form = res
    })
  },
  methods: {
    async getUser() {
      return (await this.request.get("/employee/id/" + this.user.id)).data
    },
    save() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.put("/employee", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")

              // 触发父级更新User的方法,解决头像不实时更新
              this.$emit("refreshUser")

              // // 更新浏览器存储的用户信息
              // this.getUser().then(res => {
              //   res.token = JSON.parse(localStorage.getItem("user")).token
              //   localStorage.setItem("user", JSON.stringify(res))
              // })
            } else {
              this.$message.error("保存失败")
            }
          })
        }
      });
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
