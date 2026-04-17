<template>
    <div>
        <div id="depart-canvas" style="height: 600px;min-width:800px;margin-top:20px;"></div>
    </div>
</template>

<script>
import echarts from "echarts";

export default {
    props: {
        echartsData: {
            type: Object,
            default() {
                return {}
            }
        }
    },
    data() {
        return {
            onlineChart: null,
            options: {}
        };
    },
    methods: {
        setOpt() {
            this.onlineChart.setOption(this.options, true);
        },
        transData() {
            this.options = Object.assign({}, this.echartsData);
            this.setOpt();
        }
    },
    mounted() {
       this.$nextTick(function () {
            const dom = document.getElementById('depart-canvas');
            dom.style.width = '100%';
            this.onlineChart = echarts.init(dom);
            this.transData();
        })
    },
    watch: {
        echartsData: {
            handler: function (val, oldVal) {
                this.transData();
            },
            deep: true
        }
    }
}
</script>