$(document).ready(function () {
	function echart_data(id,param){
	 		
        var myChart=echarts.init(id);
        myChart.setOption(param, true);
        window.addEventListener("resize",function(){
	    myChart.resize();  
	    });
	}
    var params=JSON.parse(sessionStorage.obj);
//机构切换
    console.log(params)
    // $(".suspend_input select").change(function () {
    //     params.orgCode=$(this).val();
    //     console.log(params)
    //     setTimeout(function () {
    //         getServiceData(params)
    //     },2000);
    // })
    function getservice(url,param) {
        return $.ajax({
            type: 'POST',
            url: url,
            dataType: "json",
            headers: {Authorization: localStorage.token},
            data: param||{}
        });
    }
var pie0 = {
	title:{
		text:"土地经营权贷款按利率分析",
		left:"center",
		top:'4%',
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'0%',
        left: '1%',
        right: '1%',
//      bottom: '40%',
        width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'horizontal',
        // x: 'lef',
        bottom:'bottom',
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        // data:['低于基准利率','等于基准利率','高于基准利率（1,1.3）','高于基准利率（1.3,1.5）','高于基准利率（1.5,2）','高于基准利率2倍以上']
    },
    series: [
       
        {
//          name:'访问来源',
            type:'pie',
            radius: ['30%', '45%'],
//           roseType : 'radius',
//             selectedMode: 'single',
            label: {
                normal: {
                show: false
                }
            },
            data:[
               
                {value:0, name:'低于基准利率',itemStyle:{normal:{color:"#c12e34"}}},
                 {value:0, name:'等于基准利率',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'高于基准利率（1,1.3）',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'高于基准利率（1.3,1.5）',itemStyle:{normal:{color:"#2b821d"}}},
                 {value:0, name:'高于基准利率（1.5,2）',itemStyle:{normal:{color:"#005eaa"}}},
                  {value:0, name:'高于基准利率2倍以上',itemStyle:{normal:{color:"#997166"}}},
            ]
        }
    ]
};

 var pie01 = {
	title:{
		text:"住房财产权贷款按利率分析",
		left:"center",
		top:'4%',
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'0%',
        left: '1%',
        right: '1%',
//      bottom: '40%',
        width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'horizontal',
        // x: 'lef',
        bottom:'bottom',
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        // data:['低于基准利率','等于基准利率','高于基准利率（1,1.3）','高于基准利率（1.3,1.5）','高于基准利率（1.5,2）','高于基准利率2倍以上']
    },
    series: [
       
        {
//          name:'访问来源',
            type:'pie',
            radius: ['30%', '45%'],
//           roseType : 'radius',
//             selectedMode: 'single',
            label: {
                normal: {
                show: false
                }
            },
            data:[

                {value:0, name:'低于基准利率',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'等于基准利率',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'高于基准利率（1,1.3）',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'高于基准利率（1.3,1.5）',itemStyle:{normal:{color:"#2b821d"}}},
                {value:0, name:'高于基准利率（1.5,2）',itemStyle:{normal:{color:"#005eaa"}}},
                {value:0, name:'高于基准利率2倍以上',itemStyle:{normal:{color:"#997166"}}},
                 
                
            ]
        }
    ]
};

var pie1 = {
	title:{
		text:"按承贷主体和用途划分",
		left:"center",
		top:'4%',
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'0%',
        left: '1%',
        right: '1%',
//      bottom: '40%',
        width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
//  legend: {
//      orient: 'horizontal',
//      x: 'lef',
//      bottom:'bottom',
//      itemWidth:15,
//  	itemHeight:8,
//  	itemGap:4,
//  	textStyle:{
//  	  fontSize:10	
//  	},
//      data:['建档立卡贫困人口贷款','其他个人精准扶贫贷款','经营性贷款','消费贷款','农业牧渔业贷款','住房贷款','助学贷款','其他','其他消费贷款','农业牧渔业']
//  },
    series: [
        {
//          name:'访问来源',
            type:'pie',
            selectedMode: 'single',
            radius: [0, '10%'],

            label: {
                normal: {
//                  position: 'inner'
                }
            },
            labelLine: {
                normal: {
//                  show: true
                }
            },
            data:[
                {value:0, name:'个人贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'单位贷款',itemStyle:{normal:{color:"#0098d9"}}}
            ]
        },
        {
//          name:'访问来源',
            type:'pie',
            radius: ['17%', '32%'],
          
            label: {
                normal: {
                show: false
                }
            },
            data:[
                {value:0, name:'个人经营性贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'个人消费贷款',itemStyle:{normal:{color:"#DF3154"}}},
                {value:0, name:'个人其他贷款',itemStyle:{normal:{color:"#E08597"}}},
                {value:0, name:'农业牧渔业贷款',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'其他用途',itemStyle:{normal:{color:"#6BC1FA"}}},
            ]
        },
        {
//          name:'访问来源',
            type:'pie',
            radius: ['39%', '54%'],
            label: {
                normal: {
                show: false
                }
            },
            data:[
               
                {value:0, name:'其他',itemStyle:{normal:{color:"#c12e34"}}},
                 {value:0, name:'个人农林牧渔业贷款',itemStyle:{normal:{color:"#DF3154"}}},
                  {value:0, name:'家庭农场贷款',itemStyle:{normal:{color:"#E86C85"}}},
                   {value:0, name:'农业专业大户贷款',itemStyle:{normal:{color:"#EF98AA"}}},
                    {value:0, name:'承包方农户贷款',itemStyle:{normal:{color:"#FEDEE4"}}},
                {value:0, name:'承包方农户贷款',itemStyle:{normal:{color:"#FEEFF2"}}},
                {value:0, name:'其他',itemStyle:{normal:{color:"#729FE3"}}},
                 {value:0, name:'个人其他贷款',itemStyle:{normal:{color:"#6BC1FA"}}},
                {value:0, name:'家庭农场贷款',itemStyle:{normal:{color:"#66D3EE"}}},
                 {value:0, name:'农业产业化龙头企业贷款',itemStyle:{normal:{color:"#B0EBF9"}}},
                 {value:0, name:'农民专业合作社贷款',itemStyle:{normal:{color:"#CFF3FC"}}},
                 {value:0, name:'其他',itemStyle:{normal:{color:"#A0F8F8"}}},
                  {value:0, name:'其他用途',itemStyle:{normal:{color:"#A7F1D3"}}},
                 
                
            ]
        }
    ]
};

var pie2  = {
	title:{
		text:"个人贷款分析",
		left:"center",
		top:'4%',
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'0%',
        left: '1%',
        right: '1%',
//      bottom: '40%',
        width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
//  legend: {
//      orient: 'horizontal',
//      x: 'lef',
//      bottom:'bottom',
//      itemWidth:15,
//  	itemHeight:8,
//  	itemGap:4,
//  	textStyle:{
//  	  fontSize:10	
//  	},
//      data:['建档立卡贫困人口贷款','其他个人精准扶贫贷款','经营性贷款','消费贷款','农业牧渔业贷款','住房贷款','助学贷款','其他','其他消费贷款','农业牧渔业']
//  },
    series: [
        {
//          name:'访问来源',
            type:'pie',
            selectedMode: 'single',
            radius: [0, '20%'],

            label: {
                normal: {
//                  position: 'inner'
                }
            },
            labelLine: {
                normal: {
//                  show: true
                }
            },
            data:[
                {value:0, name:'个人经营性贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'个人消费贷款',itemStyle:{normal:{color:"#0098d9"}}}
              
            ]
        },
        {
//          name:'访问来源',
            type:'pie',
            radius: ['27%', '42%'],
          
            label: {
                normal: {
                show: false
                }
            },
            data:[
                {value:0, name:'个人农林牧渔业贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'农业专业大户贷款',itemStyle:{normal:{color:"#E86C85"}}},
                {value:0, name:'其他用途',itemStyle:{normal:{color:"#EF98AA"}}},
                 {value:0, name:'个人消费贷',itemStyle:{normal:{color:"#0098d9"}}}
            ]
        },
       
    ]
};

var pie3 = {
	title:{
		text:"土地经营权贷款按质量分析",
		left:"center",
		top:'4%',
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'0%',
        left: '1%',
        right: '1%',
//      bottom: '40%',
        width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'horizontal',
        x: 'lef',
        bottom:'bottom',
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:[]
    },
    series: [
       
        {
//          name:'访问来源',
            type:'pie',
            radius: ['30%', '45%'],
//           roseType : 'radius',
//             selectedMode: 'single',
            label: {
                normal: {
                show: false
                }
            },
            data:[
               
                {value:0, name:'正常类贷款',itemStyle:{normal:{color:"#c12e34"}}},
                 {value:0, name:'关注类贷款',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'次级 类贷款',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'可疑类贷款',itemStyle:{normal:{color:"#2b821d"}}},
                 {value:0, name:'损失类贷款',itemStyle:{normal:{color:"#005eaa"}}},
                                          
                
            ]
        }
    ]
};

 var pie4 = {
	title:{
		text:"住房财产权贷款按质量分析",
		left:"center",
		top:'4%',
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'0%',
        left: '1%',
        right: '1%',
     bottom: '10%',
        width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'horizontal',
        x: 'lef',
        bottom:'bottom',
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:['正常类贷款','关注类贷款','次级 类贷款','可疑类贷款','损失类贷款']
    },
    series: [
       
        {
//          name:'访问来源',
            type:'pie',
            radius: ['30%', '45%'],
//           roseType : 'radius',
//             selectedMode: 'single',
            label: {
                normal: {
                show: false
                }
            },
            data:[

                {value:0, name:'正常类贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'关注类贷款',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'次级 类贷款',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'可疑类贷款',itemStyle:{normal:{color:"#2b821d"}}},
                {value:0, name:'损失类贷款',itemStyle:{normal:{color:"#005eaa"}}},
                                          
                
            ]
        }
    ]
};

var part2 = {	
	title:{
		text:"两权贷款分析",
		left:"center",
        textStyle:{
            color:"#008acd",
            fontFamily:"Courier New"
        }
	},
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            crossStyle: {
                color: '#999'
            }
        }
    },
// toolbox: {
//   	x: '85%',
//	    top: '0',
//      feature: {
//          restore: {},
//          saveAsImage: {}
//      }
//  },
    grid: {
        left: '1%',
        right: '1%',
        bottom: '10%',
        containLabel: true
    },
    legend: {
    	bottom:"bottom",
        orient:'horizontal',
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
//  	width:10,
        data:[{name:'土地经营权'},
              {name:'住房财产权'},
              {name:'土地经营权增速'},
              {name:'住房财产权增速'}]
    },
    xAxis: [
        {
            type: 'category',
            data: ['2017年2月','2017年3月','2017年4月','2017年5月','2017年6月','2017年7月','2017年8月','2017年9月','2017年10月','2017年11月','2017年12月'],
            axisPointer: {
                type: 'shadow'
            },
            nameRotate:20,
        }
    ],
    yAxis: [
        {
            type: 'value',
            name: '亿元',
            splitLine:{
	        	show:true,
	        	lineStyle:{       		
	        		width:0,
	        		type:'dotted'
	        	}
	        },
            axisLabel: {
                formatter: '{value} '
            }
        },
        {
            type: 'value',
            name: '百分比',
            min: 0,
            max: 100,
            interval: 20,
            splitLine:{
	        	show:true,
	        	lineStyle:{       		
	        		width:0,
	        		type:'dotted'
	        	}
	        },
            axisLabel: {
                formatter: '{value} %'
            }
        }
    ],
    series: [
        {
            name:'土地经营权',
            type:'bar',
            barMaxWidth:30,
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            data:[]
        },
        {
            name:'住房财产权',
            type:'bar',
            barMaxWidth:30,
            barGap:0,
             itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
             data:[]
        },
        {
            name:'土地经营权增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'住房财产权增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
            yAxisIndex: 1,
            data:[]
        }
    ]
  };

    function getServiceData(params) {
        //地图的数据

        getservice('./analysis/twoRight/getTwoRightMap', params).done(function (res) {
            var arr=[];
            console.log(res)
            if(res){
                for(var i=0;i<res.length;i++){
                    arr.push(res[i].value)
                }
                if(arr.length!=0){
                    option.visualMap.show=true;
                    option.visualMap.min=Math.min.apply(null,arr);
                    option.visualMap.max=Math.max.apply(null,arr);
                }else{
                    option.visualMap.show=false;
                }
                renderMap(params.areaName,res,res,name,col);
            }
        });
        //专项-两权-土地经营权贷款按利率分析
        getservice('./analysis/twoRight/getLandManagement',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){

                        pie0.series[0].data[i].value=res[i].value;
                        pie0.series[0].data[i].name=res[i].name;

                }
                echart_data(document.getElementById("pie0"),pie0);
            }

        });
        //专项-两权-土地经营权贷款按利率分析

        getservice('./analysis/twoRight/getHousPropertyByRate',params).done(function (res) {
            console.log(res)
            if(res){
                for(var i=0;i<res.length;i++){

                    pie01.series[0].data[i].value=res[i].value;
                    pie01.series[0].data[i].name=res[i].name;

                }
                echart_data(document.getElementById("pie01"),pie01);
            }

        });
       // 两权-按承贷主体和用途划分
        getservice('./analysis/twoRight/getProjectOwnerAndUse',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){
                    for(var j=0;j<res[i].length;j++){
                        pie1.series[i].data[j].value=res[i][j].value;
                        pie1.series[i].data[j].name=res[i][j].name;
                    }

                }
                echart_data(document.getElementById("pie1"),pie1);
            }

        });
        getservice('./analysis/twoRight/getHousPropertyByOwnerAndUse',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){
                    for(var j=0;j<res[i].length;j++){
                        pie2.series[i].data[j].value=res[i][j].value;
                        pie2.series[i].data[j].name=res[i][j].name;
                    }

                }
                echart_data(document.getElementById("pie2"),pie2);
            }

        });
        // 两权贷款分析
        getservice('./analysis/twoRight/getTwoRightLoan',params).done(function (res) {

            if(res){

                for (var i = 0; i < res.length; i++) {
                    part2.series[i].data=res[i].value;
                    part2.series[i].name=res[i].name;
                    part2.xAxis[0].data=res[0].dataDate;
                }
                echart_data(document.getElementById("echart_part2"),part2);
            }

        });
        //土地经营
        getservice('./analysis/twoRight/getHousPropertyByQuality',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){
                    pie3.series[0].data[i].value = res[i].value;
                    pie3.series[0].data[i].name = res[i].name;
                    pie3.legend.data.push(res[i].name)
                }
                echart_data(document.getElementById("pie3"),pie3);
            }

        });
        //住房财产贷款分析
        getservice('./analysis/twoRight/getLandManagementQuality',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){
                    pie4.series[0].data[i].value = res[i].value;
                    pie4.series[0].data[i].name = res[i].name;
                    pie4.legend.data.push(res[i].name)
                }
                echart_data(document.getElementById("pie4"),pie4);
            }

        });


    }
	window.addEventListener("resize",function(){
	   
	    chart.resize();     
	});
var Proe="";	
var chart = echarts.init(document.getElementById('echart_part1'));
var color=['#5D86A3','#77A3CA','#B7CADB','#89ADCD','#5D86A3'];
var col='#4889EB';
var guiyang=[{name:'贵阳',value:[106.7992,26.8682]}];
var name="土地经营权贷款余额";
    var cityProper={
        '贵州':'../../../assets/guizhou/52.json',
        '贵阳市':'../../../assets/guizhou/520100.json',
        '六盘水市':'../../../assets/guizhou/520200.json',
        '遵义市':'../../../assets/guizhou/520300.json',
        '安顺市':'../../../assets/guizhou/520400.json',
        '毕节市':'../../../assets/guizhou/520500.json',
        '铜仁市':'../../../assets/guizhou/520600.json',
        '黔西南布依族苗族自治州':'../../../assets/guizhou/522300.json',
        '黔东南苗族侗族自治州':'../../../assets/guizhou/522600.json',
        '黔南布依族苗族自治州':'../../../assets/guizhou/522700.json'
    };
var county=[
        	  {name:'赤水市',value:88},
        	  {name:'习水县',value:98},
        	  {name:'开阳县',value:8},
        	  {name:'修文县',value:28},
        	  {name:'独山县',value:98},
        	];
    var geoCoordMap={
        '遵义市':[106.928,28.2744],
        '贵阳市':[106.7992,26.8682],
        '毕节市':[105.1631,27.2648],
        '六盘水市':[104.7546,26.2925],
        '安顺市':[105.9382,25.9182],
        '黔西南布依族苗族自治州':[105.5847,25.4949],
        '黔南布依族苗族自治州':[107.2885,25.5398],
        '黔东南苗族侗族自治州':[108.4841,26.6166],
        '铜仁市':[108.6718,28.1096],
        '赤水市':[105.89,28.51],
        '习水县':[106.22,28.32],
        '红花岗区':[106.92,27.65],
        '汇川区':[106.94,27.81],
        '播州区':[106.83,27.48],
        '桐梓县':[106.82,28.23],
        '绥阳县':[107.18,28.05],
        '正安县':[107.43,28.55],
        '道真仡佬族苗族自治县':[107.6,28.88],
        '务川仡佬族苗族自治县':[107.88,28.53],
        '凤冈县':[107.75,27.97],
        '湄潭县':[107.48,27.77],
        '余庆县':[107.68,27.42],
        '仁怀市':[106.42,27.82],
        '开阳县':[106.97,27.07],
        '修文县':[106.58,26.89],
        '南明区':[106.72,26.55],
        '云岩区':[106.72,26.62],
        '花溪区':[106.67,26.39],
        '乌当区':[106.85,26.73],
        "白云区":[106.68,26.73],
        "小河区":[106.7,26.50],
        "息烽县":[106.63,27.15],
        "清镇市":[106.37,26.68],
        '独山县':[107.53,25.63],
        '惠水县':[106.69,26.03],
        '贵定县':[107.23,26.58],
        '都匀市':[107.49,26.21],
        '福泉市':[107.5,26.73],
        '荔波县':[107.88,25.39],
        '瓮安县':[107.47,27.12],
        '平塘县':[107.22,25.83],
        '罗甸县':[106.75,25.43],
        '长顺县':[106.45,26.03],
        '龙里县':[106.97,26.45],
        '三都水族自治县':[107.87,25.98],
        '钟山区':[104.83,26.6],
        '六枝特区':[105.38,26.22],
        '盘县':[104.57,25.72],
        '水城县':[104.95,26.35],
        '西秀区':[106.12,26.21],
        '平坝区':[106.25,26.42],
        '普定县':[105.75,26.32],
        '镇宁布依族苗族自治县':[105.87,25.94],
        '关岭布依族苗族自治县':[105.52,25.91],
        '紫云苗族布依族自治县':[106.08,25.75],
        '碧江区':[109.18,27.72],
        '江口县':[108.78,27.7],
        '玉屏侗族自治县':[108.92,27.29],
        '石阡县':[108.23,27.52],
        '松桃苗族自治县':[109.05,28.05],
        '思南县':[108.15,27.93],
        '印江土家族苗族自治县':[108.52,28],
        '德江县':[108.12,28.27],
        '沿河土家族自治县':[108.41,28.51],
        '万山区':[109.26,27.52],
        '金沙县':[106.22,27.47],
        '织金县':[105.77,26.67],
        '七星关区':[105.28,27.3],
        '大方县':[105.6,27.15],
        '黔西县':[106.03,27.03],
        '纳雍县':[105.38,26.78],
        '威宁彝族回族苗族自治县':[104.18,26.98],
        '赫章县':[104.72,27.13],
        '剑河县':[108.6,26.64],
        '台江县':[108.25,26.67],
        '黎平县':[109.13,26.23],
        '凯里市':[107.97,26.58],
        '黄平县':[107.9,26.9],
        '施秉县':[108.12,27.03],
        '三穗县':[108.68,26.94],
        '镇远县':[108.42,27.05],
        '岑巩县':[108.72,27.28],
        '天柱县':[109.2,26.92],
        '锦屏县':[109.15,26.61],
        '榕江县':[108.42,25.97],
        '从江县':[108.75,25.78],
        '雷山县':[108.07,26.38],
        '麻江县':[107.58,26.5],
        '丹寨县':[107.88,26.25],
        '兴仁县':[105.18,25.43],
        '普安县':[104.95,25.78],
        '晴隆县':[105.22,25.83],
        '贞丰县':[105.65,25.38],
        '望谟县':[106.1,25.17],
        '兴义市':[104.9,25.08],
        '册亨县':[105.82,24.98],
        '安龙县':[105.47,25.12]
    }
    var series_name={
        34001:"土地经营权贷款余额",
        34002:"土地经营权贷款笔数",
        34003:"土地经营权贷款户数",
        34000:"土地经营权贷款发生额",
        34061:"住房财产权贷款余额",
        34062:"住房财产权贷款笔数",
        34063:"住房财产权贷款户数",
        34060:"住房财产权贷款发生额"

    }
    $.getJSON("../../../assets/guizhou/52.json", function(data){
// 			//注册地图
        echarts.registerMap('贵州', data);
        getServiceData(params);

    });
        	
     $('.legend ul li a').click(function(e){

         e.preventDefault();
//
         $(this).parent().siblings('li').children("span").css("background",'#999999');

         if(['34000','34001','34002','34003'].indexOf($(this).children('select').val())!=-1){
             console.log(222)
             $(this).next().css("background",'#4889EB');
             col='#4889EB';
         }
         if(['34060','34061','34062','34063'].indexOf($(this).children('select').val())!=-1){
             $(this).next().css("background",'#09F7F7');
             col='#09F7F7';
         }


         name=series_name[$(this).children('select').val()];

         params.typeId=$(this).children('select').val();
         $(this).parent().addClass('active').siblings('.active').removeClass('active');

         getServiceData(params);

	 
     });
        	
           	//初始化绘制全国地图配置
			var option = {
//			    tooltip: {
//			        trigger: 'item',
////			        formatter: '{b}'
//                  formatter: function(param){
//                  	console.log(param)
//                  	return param;
//                  }
//			    },
			    legend: {
			    	show:false,
                   orient: 'vertical',
                   left: 'left',
                   data:['土地经营权贷款余额']
               },
                visualMap: {
                    seriesIndex: 0,
                    min: 0,
                    max: 2500,
                    x:"80%",
                    top: 'bottom',
                    text: ['高','低'],           // 文本，默认为数值文本
                    calculable: true,
                    inRange: {
                        color: ['#E3F0F9','#B8DAF1','#9CCCEB','#8EC4E9','#72B6E3','#2B91D5','#2887C7']
                    }
                },
               geo:{
               	show:false,
               	map:'贵州',
               	aspectScale:1,
			    zoom:1.25,
//			    label: {
//				            normal: {
//				            	show:true,
//				            	color:"red",
//				                
//				            },
////				           
//				        },
//			    zlevel:0
               },
              
               itemStyle: {
				            normal: {
				            	color:"red",
				                areaColor: 'white',
				                borderColor: 'white'
				            },
//				            emphasis: {
//				                areaColor: 'darkorange'
//				            }
				        },
//			    toolbox: {
//			        show: true,
//			        orient: 'vertical',
//			        x: '90%',
//			        top: '0',
//			        feature: {
////			            dataView: {readOnly: false},
//			            restore: {},
//			            saveAsImage: {}
//			        },
//			        iconStyle:{
//			        	normal:{
////			        		color:'#fff'
//			        	}
//			        }
//			    },
			    animationDuration:500,
			    animationEasing:'cubicOut',
		        animationDurationUpdate:800
			};
function convertData(param) {
   var res = [];
   for (var i = 0; i < param.length; i++) {
       var geoCoord = geoCoordMap[param[i].name];
       if (geoCoord) {
           res.push({
               name: param[i].name,
               value: geoCoord.concat(param[i].value)
           });
       }
   }
// console.log(res);
   return res;
};
	function renderMap(map,data,counter,name,c){
				option.tooltip={
			        trigger: 'item',
//			       
			   };
			    option.series = [ 
					{
			            name: name,
			            type: 'map',
			            mapType: map,
			            roam: false,
			            aspectScale:1,
			            zoom:1.25,
			            nameMap:{
						    'china':'中国'
						},
						
						
			            label: {
				            normal:{
								show:false,
								 
								textStyle:{
									color:c,
									fontSize:13
								}  
				            },
				           
				            emphasis: {
				                show: true,
				                textStyle:{
									color:'#fff',
									fontSize:13
								}
				            }
				        },
				        itemStyle: {
				            normal: {
				            	 color: c,
//				                areaColor: '#515D8C',
				                borderColor: '#B5B5F4'
				            },
				            emphasis: {
				                areaColor: 'darkorange'
				            }
				        },
			            data:data
			        },
			          {
			           type: 'scatter',
			           coordinateSystem: 'geo',
			           
			          data: convertData(counter),
			           symbolSize: 0,
//			           symbol: 'image://../images/111.jpg',
			           symbolRotate: 0,
			           label: {
			               normal: {
			                   formatter: '{b}',
			//                 position: 'right',
			                    color: '#E66B1A',
			                   show: false
			               },
			               emphasis: {
//			                   show: true
			               }
			           },
			           itemStyle: {
			               normal: {
			                    color: '#226DDD'
			               }
			           }
			         }, 

//			        
			        
			    ];
			
			    chart.setOption(option);
			}
           		
        chart.on('click',function (para){
            Proe = para.name;
            if(Proe in cityProper){
                params.lvl=4;
                params.areaName=Proe;
                option.geo.map=Proe;

            }else{
                params.lvl=3;
                params.areaName='贵州';
                option.geo.map='贵州';
                Proe="贵州";

            }

            $.getJSON(cityProper[Proe],function(data){
                //注册地图
                echarts.registerMap(Proe, data);
                getServiceData(params);
            });
        });

  
   
})
