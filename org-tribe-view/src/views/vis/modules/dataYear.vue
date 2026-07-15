<template>
<div class="yearDate">
   <el-date-picker
        v-model="startYear"
        value-format="yyyy"
        type="year"
        @change="startYearChange"
        placeholder="请选择年份">
    </el-date-picker>
    ~
    <el-date-picker
        v-model="endYear"
        value-format="yyyy"
        type="year"
        @change="endYearChange"
        placeholder="请选择年份">
    </el-date-picker>
</div>
</template>

<script>
  export default {
    name: 'dataYear',
    props:['startYearData','endYearData'],
    data() {
      return {
        startYear: '', // 开始年份
        endYear: '', // 结束年份
      }
    },
    mounted(){
        this.startYear = this.startYearData
        this.endYear = this.endYearData
    },
    watch: {
        startYearData(value) {
            this.startYear = value;
        },
        endYearData(value) {
            this.endYear = value;
        },
    },
    methods: {
        startYearChange(value){
            if(parseInt(this.endYear) < value){
                this.$message.warning('您的开始时间大于结束时间请重新选择！');
            }
            this.$emit('startYearValue',this.startYear)
        },
        endYearChange(value){
            if(parseInt(this.startYear) > value){
                this.$message.warning('您的结束时间小于开始时间请重新选择！')
            }
            this.$emit('endYearValue',this.endYear)
        }
        /* handleClose(e) {
            debugger
            if(this.showTime1a === true){
            this.showTime1a = false;
            }
        }, */
    }
  }
</script>
<style scoped>

</style>