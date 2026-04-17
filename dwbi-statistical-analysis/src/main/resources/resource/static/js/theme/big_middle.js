$(document).ready(function () {
   "user static";
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
	// $(".tabTitle ul li a").click(function(){
	//     if($(this).text()=='两权'){
    //         window.location.href="analysis/static/html/two_right.html";
    //     }
    // })
var pie1 = {
	title:{
		text:"按企业规模分析",
		left:"center",
        textStyle:{
            color: "#0E0E7B",
            fontFamily:"Courier New",
            fontSize: '14'
        }
//		top:'4%'
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
//  	show:true,

        left: '10%',
        right: '1%',
        // bottom: '20%',
        // width:"60%",
      
       
        containLabel: true
    },
    legend: {
        orient: 'vertical',
        x: 'right',
        y:"middle",
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:['大型','中型','小型','微型']
    },
    series: [
        {
//          name:'访问来源',
            type:'pie',
            selectedMode: 'single',
             roseType : 'radius',
            radius: ["10%", '50%'],

            label: {
                normal: {
               show: false
                }
            },
            labelLine: {
                normal: {
//                  show: true
                }
            },
            data:[             
                {value:0, name:'大型',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'中型',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'小型',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'微型',itemStyle:{normal:{color:"#2b821d"}}},
              
            ]
        }
        
    ]
};

var pie2 = {
	title:{
		text:"控股方式分析",
		left:"center",
        textStyle:{
            color: "#0E0E7B",
            fontFamily:"Courier New",
            fontSize: '14'
        }
//		top:'4%'
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
        left: '1%',
        right: '1%',
     bottom: '20%',
//      width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'vertical',
        x: 'right',
        y:"middle",
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:['国有控股企业','集体控股企业','私人控股企业','港澳台商控股企业','外商控股企业']
    },
    series: [
        {
//          name:'访问来源',
            type:'pie',
            selectedMode: 'single',
            roseType : 'radius',
            radius: ["10%", '50%'],

            label: {
                normal: {
               show: false
                }
            },
            labelLine: {
                normal: {
//                  show: true
                }
            },
            data:[             
                {value:0, name:'国有控股企业',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'集体控股企业',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'私人控股企业',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'港澳台商控股企业',itemStyle:{normal:{color:"#2b821d"}}},
                {value:0, name:'外商控股企业',itemStyle:{normal:{color:"#005eaa"}}},
              
            ]
        }
        
    ]
};

 var pie3 = {
	title:{
		text:"按贷款质量分析",
		left:"center",
        textStyle:{
            color: "#0E0E7B",
            fontFamily:"Courier New",
            fontSize: '14'
        }
//		top:'4%'
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	top:'20%',
        left: '1%',
        right: '1%',
//      bottom: '40%',
//      width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'vertical',
        x: 'right',
        y:"middle",
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:['正常类贷款','关注类贷款','次级类贷款','可疑类贷款','损失类贷款']
    },
    series: [
        {
//          name:'访问来源',
            type:'pie',
            selectedMode: 'single',
             roseType : 'radius',
            radius: ["10%", '50%'],

            label: {
                normal: {
                 show: false
                }
            },
            labelLine: {
                normal: {
//                  show: true
                }
            },
            data:[             
                {value:0, name:'正常类贷款',itemStyle:{normal:{color:"#c12e34"}}},
                {value:0, name:'关注类贷款',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'次级类贷款',itemStyle:{normal:{color:"#0098d9"}}},
                {value:0, name:'可疑类贷款',itemStyle:{normal:{color:"#2b821d"}}},
                {value:0, name:'损失类贷款',itemStyle:{normal:{color:"#005eaa"}}},
              
            ]
        }
        
    ]
};


var pie4 = {
	title:{
		text:"按抵质押方式分析",
		left:"center",
        textStyle:{
            color: "#0E0E7B",
            fontFamily:"Courier New",
            fontSize: '14'
        }
//		top:'4%'
	},
    tooltip: {
        trigger: 'item',
        formatter: "{b}: {c} ({d}%)"
    },
    grid: {
    	left: '1%',
        right: '1%',
     bottom: '20%',
//      width:"60%",
       padding:[0,0],
       
        containLabel: true
    },
    legend: {
        orient: 'vertical',
        x: 'right',
        y:"middle",
        itemWidth:15,
    	itemHeight:8,
    	itemGap:4,
    	textStyle:{
    	  fontSize:10	
    	},
        data:['信用贷款','保证贷款','抵（质）押贷款']
    },
    series: [
        {
//          name:'访问来源',
            type:'pie',
            selectedMode: 'single',
            roseType : 'radius',
            radius: ["10%", '50%'],
            label: {
                normal: {
                    show: false
                }
            },
            labelLine: {
                normal: {
//                  show: true
                }
            },
            data:[             
                {value:0, name:'保证贷款',itemStyle:{normal:{color:"#c12e34"}}},
                
                {value:0, name:'抵（质）押贷款',itemStyle:{normal:{color:"#e6b600"}}},
                {value:0, name:'信用贷款',itemStyle:{normal:{color:"#0098d9"}}},
             
              
            ]
        }
        
    ]
};


var rect = {
	    title : {
            top:0,
            left:"center",
	        text: '按国民经济行业分类',
            textStyle:{
                color: "#0E0E7B",
                fontFamily:"Courier New",
                fontSize: '14'
            }
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{b}: {c}"
	    },
	   
	    calculable : false,
	    series : [
	        {
	            name:'矩形图',
                type:'treemap',
                top:40,
	            itemStyle: {
	                normal: {
	                    label: {
	                        show: true,
	                        formatter: "{b}"
	                    },
	                    borderWidth: 1
	                },
	                emphasis: {
	                    label: {
	                        show: true
	                    }
	                }
	            },
	            data:[
	                {
	                    name: '农、林、牧、渔业',
	                    value: 6
	                },

	            ]
	        }
	    ]
	};

var part2 = {	
	title:{
		text:"时序分析",
		left:"center",
        textStyle:{
            color: "#0E0E7B",
            fontFamily:"Courier New",
            fontSize: '14'
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
        top:30,
        left: '1%',
        right: '1%',
        bottom: '22%',
        containLabel: true
    },
    legend: {
        bottom:'bottom',
        itemWidth:15,
        itemHeight:8,
        itemGap:4,
        textStyle:{
            fontSize:10
        },
        data:[{name:'大型企业余额'},
              {name:'中型企业余额'},
              {name:'小型企业余额'},
              {name:'微型企业余额'},
              {name:'小微型型企业余额'},
                {name:'大型企业增速'},
              {name:'中型企业增速'},
              {name:'小型企业增速'},
              {name:'微型企业增速'},
              {name:'小微型型企业增速'}]
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
            name:'大型企业余额',
            type:'bar',
            barMaxWidth:30,
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            data:[]
        },
        {
            name:'中型企业余额',
            type:'bar',
            barMaxWidth:30,
             itemStyle:{
            	normal:{color:"#e6b600"},
            	
            },
             data:[]
        },
         {
            name:'小型企业余额',
            type:'bar',
             barMaxWidth:30,
             itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
             data:[]
        },
         {
            name:'微型企业余额',
            type:'bar',
             barMaxWidth:30,
             itemStyle:{
            	normal:
                    {color:"#2b821d"},
            	
            },
             data:[]
        },
		{
            name:'小微型企业余额',
            type:'bar',
             barMaxWidth:30,
             barGap: 0,
             itemStyle:{
            	normal:
                    {color:"005eaa"},
            	
            },
             data:[]
        },

        {
            name:'大型企业增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#c12e34"},
            	
            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'中型企业增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#e6b600"},
            	
            },
            yAxisIndex: 1,
            data:[]
        },
        {
            name:'小型企业增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#0098d9"},
            	
            },
            yAxisIndex: 1,
           data:[]
        },
        {
            name:'微型企业增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#2b821d"},
            	
            },
            yAxisIndex: 1,
            data:[]
        },
		{
            name:'小微型企业增速',
            type:'line',
            itemStyle:{
            	normal:{color:"#005eaa"},
            	
            },
            yAxisIndex: 1,
            data:[]
        }
		

    ]
  };

    //请求数据
    function getServiceData(params) {
        //地图数据
        getservice('./analysis/dkih/dkihMap', params).done(function (res) {
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
        //按企业规模分析
        getservice('./analysis/dkih/getValueByScale',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){
                    pie1.series[0].data[i].value=res[i].value;
                    pie1.series[0].data[i].name=res[i].name;
                }
                echart_data(document.getElementById("pie1"),pie1);
            }

        });
        //按控股方式分析
        getservice('./analysis/dkih/getValueByControl',params).done(function (res) {

            if(res){
                for(var i=0;i<res.length;i++){
                    pie2.legend.data[i]=res[i].name;
                    pie2.series[0].data[i].value=res[i].value;
                    pie2.series[0].data[i].name=res[i].name;
                }

                echart_data(document.getElementById("pie2"),pie2);
            }

        });
        //按贷款质量分析
        getservice('./analysis/dkih/getValueByPatternOfLending',params).done(function (res) {
            if(res){
                for(var i=0;i<res.length;i++){
                    pie3.series[0].data[i].value=res[i].value;
                    pie3.series[0].data[i].name=res[i].name;
                }

                echart_data(document.getElementById("pie3"),pie3);
            }

        });
        //按抵押方式分析
        getservice('./analysis/dkih/getValueByMortgagePattern',params).done(function (res) {
            if(res){
                for(var i=0;i<res.length;i++){
                    pie4.series[0].data[i].value=res[i].value;
                    pie4.series[0].data[i].name=res[i].name;
                }
                echart_data(document.getElementById("pie4"),pie4);
            }

        });
        //企业分析
        getservice('./analysis/dkih/getValueByEnterprise',params).done(function (res) {
            if(res){

                for (var i = 0; i < res.length; i++) {
                    part2.series[i].data=res[i].value;
                    part2.series[i].name=res[i].name;
                    part2.xAxis[0].data=res[0].dataDate;
                }
                echart_data(document.getElementById("echart_part2"),part2);
            }

        });
        //大中小微企业贷款按行业分析
        getservice('./analysis/dkih/getValueByIndustry',params).done(function (res) {

            if(res){
                rect.series[0].data=res;
                echart_data(document.getElementById("rect"),rect);
            }

        });
    }


	window.addEventListener("resize",function(){
	   
	    chart.resize();     
	});
var Proe="";
var name="大型企业";
var chart = echarts.init(document.getElementById('echart_part1'));
var color=['#5D86A3','#77A3CA','#B7CADB','#89ADCD','#5D86A3'];
var col='#4889EB';
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
        	  {name:'独山县',value:98}
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
        '安龙县':[105.47,25.12],
    }

    $.getJSON("../../../assets/guizhou/52.json", function(data){
// 			//注册地图
        echarts.registerMap('贵州', data);
        setTimeout(function () {
            getServiceData(params)
        },500);

    });
        	
     $('.legend1 ul li a').click(function(e){

     $(this).parent().siblings('li').children("span").css("background",'#999999');
	 if(this.text=="大型企业"){	
	 	$(this).next().css("background",'#4889EB');
	 	params.typeId="CS01";
         col='#4889EB';
	 }
	 if(this.text=="中型企业"){	 	
	 	$(this).next().css("background",'#09F7F7');
         params.typeId="CS02";
	 	col='#09F7F7';
	 }
	 
	 if(this.text=="小型企业"){
	   $(this).next().css("background",'#EE6911');
         params.typeId="CS03";
	   col='#EE6911';
	 }
	 if(this.text=="微型企业"){
	   $(this).next().css("background",'#292929');
         params.typeId="CS04";
	   col='#292929';
	 }
	  	name=$(this).text();
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
                    x: "80%",
                    itemWidth: 10,
                     itemHeight: 100,
                    top: 'bottom',
                    text: ['高','低'],           // 文本，默认为数值文本
                    calculable: true,
                    inRange: {
                        color: ['#9CCCEB', '#8EC4E9', '#2B91D5', '#2887C7','#0E0E7B']
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
				            	//  color: c,
//				                areaColor: '#515D8C',
                                color: 'transparent',
				                borderColor: 'white'
				            },
				            emphasis: {
				                areaColor: 'darkorange'
				            }
				        },
			            data:data
			        },
			          {
                          name:"比上年同期增额",
			           type: 'scatter',
			           coordinateSystem: 'geo',
			           
			          data: convertData(counter),
			           symbolSize: 0,
                          // symbolSize: function (val) {
                          //     return val[2] / 20;
                          // },
//			           symbol: 'image://../images/111.jpg',
			           symbolRotate: 15,
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

                     {
                        name: '',
                        type: 'effectScatter',
                        coordinateSystem: 'geo',
                        data:convertData(data.sort(function (a, b) {
                            return b.value - a.value;
                        })),
                        symbolSize: function (val) {
                            return 17;
                        },
                        rippleEffect:{
                        	period:1,
                        	scale:4,
                        	brushType:'stroke'
                        },
                        showEffectOn: 'render',
                       
                        hoverAnimation: true,
                        label: {
                            normal: {
                                formatter: '{a}',
                                position: 'right',
        //			                    show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#ffff00',
                                shadowBlur: 20,
        //			                    shadowColor: '#F16441'
                            }
                        }
        
                    }
			        
			    ];

			
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
