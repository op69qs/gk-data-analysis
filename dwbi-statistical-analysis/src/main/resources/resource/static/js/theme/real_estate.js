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
    function getservice(url,param) {
        return $.ajax({
            type: 'POST',
            url: url,
            dataType: "json",
            headers: {Authorization: localStorage.token},
            data: param||{}
        });
    }
var pie1 = {
	title:{
		text:"房地产开发贷款分析",
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
//      data:[]
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
                {value:0, name:'地产开发贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'房产开发贷款',itemStyle:{normal:{color:"#0098d9"}}}
            ]
        },
        {
//          name:'访问来源',
            type:'pie',
            radius: ['32%', '50%'],
            label: {
                normal: {
                   show: false
                }
            },
            data:[
                {value:0, name:'住房开发贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'商业用房开发贷款',itemStyle:{normal:{color:"#E68094"}}},
                {value:0, name:'其它房产开发贷款',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'房产开发贷款',itemStyle:{normal:{color:"#97B4E0"}}},
                {value:0, name:'房产开发贷款',itemStyle:{normal:{color:"#A3D4F5"}}}
            ]
        }
        
    ]
};

var pie2 = {
	title:{
		text:"房地产贷款分类",
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
        left: '-5%',
        right: '0%',
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
//      data:[]
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
//             
                {value:0, name:'企业购房贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'机关团体购房贷款',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'个人购房贷款',itemStyle:{normal:{color:"#0098d9"}}},
              
            ]
        },
        {
//          name:'访问来源',
            type:'pie',
            radius: ['32%', '50%'],
            label: {
                normal: {
                   show: false
                }
            },
            data:[
                {value:0, name:'商业用房贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'住房贷款',itemStyle:{normal:{color:"#FB7C7C"}}},
                {value:0, name:'商业用房贷款',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'住房贷款',itemStyle:{normal:{color:"#F3F394"}}},
                {value:0, name:'个人商业用房贷款',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'个人住房贷款',itemStyle:{normal:{color:"#5ab1ef"}}}
            ]
        },
        
    ]
};


var part2 = {	
	title:{
		text:"房地产贷款时序图",
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
//  	width:10,
        data:['上月余额','本月新增额','房地产贷款增速']
    },
    xAxis: [
        {
            type: 'category',
            data: [],
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
            name:'上月余额',
            type:'bar',
            barMaxWidth:30,
            stack:'新增额', 
            itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
            data:[]
        },
        {
            name:'本月新增额',
            type:'bar',
            barGap:0,
            barMaxWidth:30,
            stack:'新增额',
             itemStyle:{
            	normal:{color:"#e6b600"},
            	
            },
             data:[]
        },
        {
            name:'房地产贷款增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            yAxisIndex: 1,
            data:[]
        },
       
    ]
  };

  var line_bar1 = {
	title:{
		text:"房产开发贷款分析",
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
        bottom: '30%',
        containLabel:true,
        
    },
    legend: {
        bottom:'bottom',
        itemWidth:8,
        itemHeight:6,
        itemGap:0,
        textStyle:{
            fontSize:8
        },
        data:[]
    },
    xAxis: [
        {
            type: 'category',
            data: [],
            axisPointer: {
            	show:true,
                type: 'shadow',
                lineStyle: {
			        // 使用深浅的间隔色
			        width:10,
			      } 
            }
           
        }
    ],
    yAxis: [
        {
            type: 'value',
            name: '亿元',
            splitNumber:3,
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
            splitNumber:3,
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
            name:'保障性住房开发贷款余额',
            
            type:'bar',
            barMaxWidth:30,
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            data:[]
        },
        {
            name:'普通商品房开发贷款余额',
            type:'bar',
            barMaxWidth:30,
            barGap:0,
             itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
             data:[]
        },
       
        {
            name:'保障性住房开放贷款增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'普通商品房开发贷款增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
            yAxisIndex: 1,
            data:[]
        }
    ]
  };

 var line_bar2 = {	
 	title:{
		text:"房地产贷款流量分析",
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
        bottom: '20%',
        containLabel: true
    },
    legend: {
 	    show:true,
        bottom:'bottom',
        itemWidth:8,
        itemHeight:6,
        itemGap:0,
        textStyle:{
            fontSize:6
        },
        data:[]
    },
    xAxis: [
        {
            type: 'category',
            data: [],
            axisPointer: {
                type: 'shadow'
            }
        }
    ],
    yAxis: [
        {
            type: 'value',
            name: '亿元',
            splitNumber:3,
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
            splitNumber:3,
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
            name:'当月发放新建房贷款',
            type:'bar',
            barMaxWidth:20,
            stack:'贷款', 
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            data:[]
        },
        {
            name:'当月发放再交易房贷款',
            type:'bar',
            barMaxWidth:20,
             stack:'贷款',  
            itemStyle:{
            	normal:{color:"#e6b600"},
            	
            },
            data:[]
        },
        {
            name:'当月购入个人住房贷款',
            type:'bar',
            barMaxWidth:20,
             stack:'贷款',  
            itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
            data:[]
        },
        {
            name:'正常还款',
            type:'bar',
            barMaxWidth:20,
            stack:'还款', 
            itemStyle:{
            	normal:{color:"#2b821d"},
            	
            },
            data:[]
        },
        {
            name:'部分提前还款',
            type:'bar',
            barMaxWidth:30,
            // barGap:0,
             stack:'还款',  
            itemStyle:{
            	normal:{color:"#005eaa"},
            	
            },
            data:[]
        },
        {
            name:'全部提前还款',
            type:'bar',
            barMaxWidth:30,
            barGap:0,
            stack:'还款', 
            itemStyle:{
            	normal:{color:"#b6a2de"},
            	
            },
            data:[]
        },
        {
            name:'当月发放新建房贷款',
            type:'line',
            itemStyle:{
                normal:{color:"#c12e34"},

            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'当月发放新建房贷款',
            type:'line',
            itemStyle:{
                normal:{color:"#e6b600"},

            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'当月购入个人住房贷款',
            type:'line',
            itemStyle:{
                normal:{color:"#0098d9"},

            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'正常还款',
            type:'line',
            itemStyle:{
                normal:{color:"#2b821d"},

            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'部分提前还款',
            type:'line',
            itemStyle:{
                normal:{color:"#005eaa"},

            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'全部提前还款',
            type:'line',
            itemStyle:{
                normal:{color:"#b6a2de"},

            },
            yAxisIndex: 1,
            data:[]
        }
       
    ]
  };

 var line_bar3 = {
	title:{
		text:"个人住房贷款分析",
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
        bottom: '15%',
        containLabel: true
    },
    legend: {
    	bottom:"bottom",
//  	orient:'horizontal',
    	itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:[]
    },
    xAxis: [
        {
            type: 'category',
            data: [],
            axisPointer: {
                type: 'shadow'
            }
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
            name:'',
            
            type:'bar',
            barMaxWidth:30,
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            data:[]
        },
        {
            name:'',
            type:'bar',
            barMaxWidth:30,
            barGap:0,
             itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
             data:[]
        },
        {
            name:'',
            type:'line',
             itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            yAxisIndex: 1,
             data:[]
        },
        {
            name:'',
            type:'line',
             itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
            yAxisIndex: 1,
             data:[]
        },
    ]
  };

//请求数据
    function getServiceData(params) {
        getservice('./analysis/realEstate/getMapBalance', params).done(function (res) {
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
        //房地产开发贷款分析

        getservice('./analysis/realEstate/getRealEstateDevelopment',params).done(function (res) {
            if(res){

                for(var i=0;i<res.length;i++){
                    for(var j=0;j<res[i].length;j++){
                        // pie1.series[i].data.push({value:res[i][j].value,name:res[i][j].name});
                        pie1.series[i].data[j].value=res[i][j].value;
                        pie1.series[i].data[j].name=res[i][j].name;
                    }

                }
                echart_data(document.getElementById("pie1"),pie1);
            }

        });
        //房地产贷款分类
        getservice('./analysis/realEstate/getRealEstateType',params).done(function (res) {

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
        //房产开发贷款分析
        getservice('./analysis/realEstate/getHouseProperty',params).done(function (res) {

            if(res){
                line_bar1.legend.data=[];
                for(var i=0;i<res.length;i++){
                    line_bar1.series[i].data=res[i].value;
                    line_bar1.series[i].data.name=res[i].name;
                    line_bar1.series[i].name=res[i].name
                    line_bar1.legend.data.push(res[i].name);
                }
                line_bar1.xAxis[0].data=res[0].dataDate;

                echart_data(document.getElementById("line_bar1"),line_bar1);
            }
        });
        //房地产贷款流量分析
        getservice('./analysis/realEstate/getRealEstateFlow',params).done(function (res) {

            if(res){
                line_bar2.legend.data=[];
                for(var i=0;i<res.length;i++){
                    line_bar2.series[i].data=res[i].value;
                    line_bar2.series[i].data.name=res[i].name;
                    line_bar2.series[i].name=res[i].name;
                    line_bar2.xAxis[0].data=res[0].dataDate;
                    line_bar2.legend.data.push({name:res[i].name});
                }

                echart_data(document.getElementById("line_bar2"),line_bar2);
            }

        });
        //房地产贷款时序图
        getservice('./analysis/realEstate/getLoanSequential',params).done(function (res) {

            if(res){
                part2.series[0].data=res[2];
                part2.series[1].data=res[3]
                part2.series[2].data=res[4]
                part2.xAxis[0].data=res[1];
                part2.title.text=res[0]+"时序图";
                echart_data(document.getElementById("echart_part2"),part2);
            }

        });
        //个人住房贷款分析
        getservice('./analysis/realEstate/getPersonalLoan',params).done(function (res) {

            if(res){
                line_bar3.legend.data=[];
                for(var i=0;i<res.length;i++){
                    line_bar3.series[i].data=res[i].value;
                    line_bar3.series[i].name=res[i].name;
                    line_bar3.xAxis[0].data=res[0].dataDate;
                    line_bar3.legend.data.push({name:res[i].name});
                }
                  echart_data(document.getElementById("line_bar3"), line_bar3);
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
var name="房地产贷款";
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
        	  {name:'修文县',value:28}

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

    $.getJSON("../../../assets/guizhou/52.json", function(data){
// 			//注册地图
        echarts.registerMap('贵州', data);
        getServiceData(params);

    });
        	
     $('.legend ul li a').click(function(e){

        		  e.preventDefault();

     $(this).parent().siblings('li').children("span").css("background",'#999999');
	 if(this.text=="房地产贷款"){	
	 	$(this).next().css("background",'#4889EB');
	 	col='#4889EB';
	 	params.typeId="REST";
	 }
	 if(this.text=="房地产开发贷款"){	 	
	 	$(this).next().css("background",'#09F7F7');
         params.typeId="REDE";
	 	col='#09F7F7';
	 }
	 
	 if(this.text=="购房"){
	   $(this).next().css("background",'#EE6911');
         params.typeId="PURC";
	   col='#EE6911';
	 }
	  	
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
                   data:['贷款余额']
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
				                borderColor: 'white',
                                show:false,
				            },
//				            emphasis: {
//				                areaColor: 'darkorange'
//				            }
				        },
//
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

			        
			    ];
			    //渲染地图
//			    console.log(option.series);
			
			    chart.setOption(option);
			}
           		
        chart.on('click',function (para){
            //选择省的单机事件
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
