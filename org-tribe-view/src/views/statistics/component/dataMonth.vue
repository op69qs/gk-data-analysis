<template>
<div class="head">
        <!-- <li>
            <el-button type="primary" size="small" plain @click="monthTime">月</el-button>
            <el-button type="primary" size="small" plain @click="doubleMouth">双月</el-button>
            <el-button type="primary" size="small" plain @click="quarterTime">季度</el-button>
            <el-button type="primary" size="small" plain @click="halfYear">半年</el-button>
            <el-button type="primary" size="small" plain @click="fullYear">年</el-button>
        </li> -->
                    <!-- <li class="nav_left">适用时间:</li> -->
                    <span class="span" style="position:relative;" @click="showDoubleMonth" ref="li"><a-icon style="position:absolute;right:10px;top:12px;z-index:100;color: rgba(0, 0, 0, 0.25);font-size:14px;" slot="suffixIcon" type="calendar" /><el-input v-model="choseQuarter"  placeholder="请选择季度"></el-input></span> ~ <span class="span"  style="position:relative;" @click="showDoubleMonth1" ref="lis"><a-icon style="position:absolute;right:10px;top:12px;z-index:100;color: rgba(0, 0, 0, 0.25);font-size:14px;" slot="suffixIcon" type="calendar" /><el-input v-model="choseQuarter1"  placeholder="请选择季度"></el-input></span>
                    <li class="show1" v-show="showTime1a">
                        <p ref="p1"> <button
                                type="button"
                                aria-label="前一年"
                                class="el-picker-panel__icon-btn el-date-picker__prev-btn el-icon-d-arrow-left"
                                @click="prev"
                        ></button>
                            <span role="button" class="span-year">{{year}}年</span>
                            <button
                                    type="button"
                                    aria-label="后一年"
                                    @click="next"
                                    class="el-picker-panel__icon-btn el-date-picker__next-btn el-icon-d-arrow-right"
                            ></button></p>
                        <p v-for="(item,index) in fullMonth">
                            <span class="selectMonth" @click="selectQuarter(index)">{{item}}</span>
                        </p>
                    </li>
                    <li class="show1" v-show="showTime1a1">
                        <p ref="p2"> <button
                                type="button"
                                aria-label="前一年"
                                class="el-picker-panel__icon-btn el-date-picker__prev-btn el-icon-d-arrow-left"
                                @click="prev"
                        ></button>
                            <span role="button" class="span-year">{{year}}年</span>
                            <button
                                    type="button"
                                    aria-label="后一年"
                                    @click="next"
                                    class="el-picker-panel__icon-btn el-date-picker__next-btn el-icon-d-arrow-right"
                            ></button></p>
                        <p v-for="(item,index) in fullMonth">
                            <span class="selectMonth" @click="selectQuarter1(index)">{{item}}</span>
                        </p>
                    </li>
 </div>
</template>

<script>
const clickoutside = {
    // 初始化指令
    bind(el, binding, vnode) {
        function documentHandler(e) {
            // 这里判断点击的元素是否是本身，是本身，则返回
            if (el.contains(e.target)) {
                return false;
            }
            // 判断指令中是否绑定了函数
            if (binding.expression) {
                // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
                binding.value(e);
            }
        }
        // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
        el.__vueClickOutside__ = documentHandler;
        document.addEventListener('click', documentHandler);
    },
    unbind(el, binding) {
        // 解除事件监听
        document.removeEventListener('click', el.__vueClickOutside__);
        delete el.__vueClickOutside__;
    },
};
  export default {
    props:['choseQuarter','choseQuarter1'],
    data() {
      return {
            showTime1a:false,
            showTime1a1:false,
            year: new Date().getFullYear(),
            fullMonth:[3,6,9,12],
            doubleMouthShow:false,
            choseQuarter:'',
            choseQuarter1:'',
            indexType:'',
      }
    },
    directives: {clickoutside},
    mounted(){
        document.addEventListener('click', this.handleDocumentClick)
    },
    beforeDestroy(){
        document.removeEventListener('click', this.handleDocumentClick)
    },
    computed: {
     
   },
    methods: {
        handleDocumentClick(e){
            const {li, lis, p1, p2} = this.$refs
            if (li && p1 && !li.contains(e.target) && !p1.contains(e.target)) {
                this.showTime1a = false
            }
            if (lis && p2 && !lis.contains(e.target) && !p2.contains(e.target)) {
                this.showTime1a1 = false
            }
        },
        showDoubleMonth(){
            this.showTime1a=true;
        },
        showDoubleMonth1(){
            this.showTime1a1=true;
        },
        prev() {
            this.year = this.year * 1 - 1;
        },
        next() {
            this.year = this.year * 1 + 1
        },
        selectQuarter(index){
            console.log(parseInt(index))
            if(parseInt(index)==0 ){
                this.choseQuarter=this.year+'-'+'Q1';
                this.showTime1a=false
            }else if(parseInt(index)==1  ){
                this.choseQuarter=this.year+'-'+'Q2';
                this.showTime1a=false
            }else if(parseInt(index)==2){
                this.choseQuarter=this.year+'-'+'Q3';
                this.showTime1a=false
            }else{
                this.choseQuarter=this.year+'-'+'Q4';
                this.showTime1a=false
            }
            this.$emit('startquarter',this.choseQuarter)
            //this.$emit('startquarter',this.choseQuarter)
        },
        selectQuarter1(index){
            console.log(parseInt(index))
            if(parseInt(index)==0 ){
                this.choseQuarter1=this.year+'-'+'Q1';
                this.showTime1a1=false
            }else if(parseInt(index)==1  ){
                this.choseQuarter1=this.year+'-'+'Q2';
                this.showTime1a1=false
            }else if(parseInt(index)==2){
                this.choseQuarter1=this.year+'-'+'Q3';
                this.showTime1a1=false
            }else{
                this.choseQuarter1=this.year+'-'+'Q4';
                this.showTime1a1=false
            }
            this.$emit('endquarter',this.choseQuarter1)
            //this.$emit('startquarter',this.choseQuarter)
        },
        /* handleClose(e) {
            debugger
            if(this.showTime1a === true){
            this.showTime1a = false;
            }
        }, */
    }
  }
</script>
<style>
  *{
    margin: 0;
    padding: 0;
    list-style: none;

  }
 .head{
     display:inline-block;
     width:100%;
 }
 .head .span{
    display: inline-block;
    width: 46%;
 }
 .navBar input{
    width: 180px;
    height: 32px;
    display: inline-block;
}
.el-input__inner{
    height:32px;
    line-height:32px;
}
.show1{
    width: 320px;
    margin-top: 5px;
    position: absolute;
    z-index: 2;

    height: 100px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    background: #fff;
    padding: 5px;
}
.show1 p:nth-child(1){
    width: 100%;
    height: 40px;
    border-bottom: 1px solid #f5f5f5;
}
.show1 p:nth-child(2){
    box-sizing: border-box;
}
.selectMonth{
    display: inline-block;
    float: left;
    width: 76px;
    height: 30px;
    line-height: 30px;
    text-align: center;
}
.selectMonth:hover{
    background: rgba(19,131,255,0.052);
}
.span-year{
    width: 90%;
    margin: 0 auto;
    display: inline-block;
    text-align: center;
    line-height: 40px;

}
.head .el-input{
    width: 100%;
    height: 32px;
    display: inline-block;
}
.head .el-input input{
   font-weight:bold;
}
</style>
