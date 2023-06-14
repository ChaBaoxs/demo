<template>
<div>
  <el-row :gutter="10" style="margin-bottom: 60px ">
    <el-col :span="6">
      <el-card style="color: #409EFF">
        <div><i class="el-icon-user-solid" /> 学生总数</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold" :data="userTotal">
          {{userTotal}}人
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #F56C6C">
        <div><i class="el-icon-money" /> 课程总量</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold" :data="courseTotal">
          {{courseTotal}}
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #67C23A">
        <div><i class="el-icon-bank-card" /> 收益总额</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold" :data="priceTotal">
          ￥{{priceTotal}}
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #E6A23C">
        <div><i class="el-icon-s-shop" /> 成交订单</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold" :data="orderTotal">
          {{orderTotal}}单
        </div>
      </el-card>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="12">
      <div id="main" style="width: 500px;height: 450px"></div>
    </el-col>

    <el-col  :span="12">
      <div id="pie" style="width: 600px;height: 450px"></div>
    </el-col>
  </el-row>

</div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data(){
    return{
      userTotal:0,
      courseTotal:0,
      orderTotal:0,
      priceTotal:0,
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
          this.request.get("/employee/total").then(res => {
            this.userTotal = res.data
          }),
          this.request.get("/course/total").then(res => {
            this.courseTotal = res.data
          }),
          this.request.get("/order/total").then(res => {
            this.orderTotal = res.data
          }),
          this.request.get("/order/allPrice").then(res => {
             this.priceTotal = res.data.sum
          })
    },
  },
  mounted() {//页面元素渲染之后再触发

    var option = {
      title: {
        text: '各季度会员数据统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度","第二季度","第三季度","第四季度",]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: "会员数",
          data: [],
          type: 'line'
        },
        {
          name: "会员数",
          data: [],
          type: 'bar'
        },
        // {
        //   name: "会员数2",
        //   data: [],
        //   type: 'line'
        // },
        // {
        //   name: "会员数2",
        //   data: [],
        //   type: 'bar'
        // }

      ]
    };


    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    this.request.get("/echarts/members").then(res =>{
      //填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data

      // option.series[2].data = [5,8,11,16]
      // option.series[3].data = [5,8,11,16]
      //数据准备完毕之后再set
      myChart.setOption(option);
    })

    //饼图
    var pieOption = {
      title: {
        text: '各季度成交订单数据统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger  : 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: '订单数',
          type: 'pie',
          radius: '55%',
          label: {
            normal: {
              show: true,
              formatter: '{b}: {c}({d}%)'
            }
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/orders").then(res =>{
      //填空
      pieOption.series[0].data = [
        {name:"第一季度",value:res.data[0]},
        {name:"第二季度",value:res.data[1]},
        {name:"第三季度",value:res.data[2]},
        {name:"第四季度",value:res.data[3]}
      ]
      //数据准备完毕之后再set
      pieChart.setOption(pieOption);
    })
  }
}
</script>

<style scoped>

</style>