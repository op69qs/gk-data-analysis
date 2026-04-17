/**
 * 图表数据处理模块
 */
layui.define(function (exports) {
    //处理存款结构翻译
    var ckjg={
        D01:'普通存款',
        D011:'单位活期存款',
        D012:'单位定期存款',
        D013:'活期储蓄存款',
        D014:'定期储蓄存款',
        D02:'定活两便存款',
        D03:'通知存款',
        D04:'协议存款',
        D05:'协定存款',
        D051:'结算户存款',
        D052:'协定户存款',
        D06:'保证金存款',
        D061:'银行承兑汇票保证金存款',
        D062:'信用证保证金存款',
        D063:'保函保证金存款',
        D064:'银行本票保证金存款',
        D065:'信用卡保证金存款',
        D066:'金融衍生产品交易保证金存款',
        D067:'黄金交易保证金存款',
        D068:'证券交易保证金',
        D069:'其他保证金存款',
        D07:'应解汇款及临时存款',
        D08:'结构性存款',
        D09:'信用卡存款',
        D091:'贷记卡存款',
        D092:'准贷记卡存款',
        D10:'财政性存款',
        D101:'国库存款',
        D1011:'财政库款',
        D1012:'财政过渡存款',
        D109:'其他财政存款',
        D1091:'划缴财政存款',
        D1092:'待结算财政款项',
        D1093:'财政专用基金存款',
        D1094:'财政预算外存款',
        D1095:'国库定期存款',
        D15:'委托资金存款(净)',
        D16:'大额存单',
        D99:'其他存款'
        }
    /**
     * 处理地图数据
     * @param {*} data 
     * @param {*} code 
     */
    function handleMapData(data, code) {
        if (data.map) {
            $.each(data.map, function (k, d) {
                var series = d.series;
                for (var key in series) {
                    if (series[key].length != 0) {
                        //
                        var features = echarts.getMap(code).geoJson.features;
                        var res = [];
                        for (var i = 0; i < features.length; i++) {
                            var c = features[i]['properties']['id'];
                            var name = features[i]['properties']['name'];
                            res[i] = {
                                name: name,
                                value: getValue(series[key], c),
                                code: c,
                                divId: k
                            }
                        }
                        data.map[k]['series'][key] = res;
                        //
                    }
                }
            })
        }
    }
    function getValue(arr, code) {
        var res = 0;
        for (var i = 0; i < arr.length; i++) {
            if (arr[i]['code'] == code) {
                res = arr[i]['value'];
                break;
            }
        }
        return res;
    }
    function handle_dbw_tree(data,name){
        if (data.length == 0) {
            return;
        }
        var total=0;
        var root={};
        $.each(data, function (k, o) {
            //计算全部存款金额
            var type = o['TREE_NAME'];
            var v = o['TREE_VALUE'] || 0;
            var par_name=o['PARENT_NAME'];
            var par_type=o['PARENT_TYPE'];
            if(!root[par_name]){
                root[par_name]=[];
            }
            root[par_name].push({type:type,value:v});
            total += v;
        })
        var root_=[];
        $.each(root,function(k,o){
            var leafs=o;
            var leafs_v=0;
            var ls=[];
            $.each(leafs,function(kk,oo){
                leafs_v+=oo.value;
                var node_={
                    name:oo.type,
                    percent:Math.round(oo.value / total * 10000)/100,
                    value:Math.round(oo.value/10000),
                    children: []
                }
                ls.push(node_);
            })
            var node = {
                name: k,
                percent:Math.round(leafs_v / total * 10000)/100,
                value:Math.round(leafs_v/10000),
                children: ls
            }
            root_.push(node);
        })

        var res = {
            name: name,
            children: root_
        }
        return res;
    }
    /**
     * 构建tree结构
     * @param {*} data 
     */
    function handle_ckxx_tree(data,name) {
        if (data.length == 0) {
            return;
        }
          //处理第一级
          var one_tree={}
          //处理第二级
          var two_tree={}
          //处理第三级
          var three_tree={}
        //计算总额
        var total = 0;
        $.each(data, function (k, o) {
            //计算全部存款金额
            var type = o['TREE_TYPE'];
            var v = o['TREE_VALUE'] || 0;
            total += v;
            var key1=type.substr(0,3);
            var key2=type.substr(0,4);
            var key3=type.substr(0,5);
            if(one_tree[key1]){
                one_tree[key1]=one_tree[key1]+v;
            }else{
                one_tree[key1]=v;
            }
            if(two_tree[key2]){
                two_tree[key2]=two_tree[key2]+v;
            }else{
                two_tree[key2]=v;
            }
            if(three_tree[key3]){
                three_tree[key3]=three_tree[key3]+v;
            }else{
                three_tree[key3]=v;
            }
        })
        var res = {
            name: name,
            children: []
        };
        $.each(one_tree,function(key,v){
            var node1 = {
                name: ckjg[key],
                percent:Math.round(v / total * 10000)/100,
                value:Math.round(v/10000),
                children: []
            }
            $.each(two_tree,function(key2,v2){
                if(key2.indexOf(key)>-1&&key2!=key){
                    var node2 = {
                        name: ckjg[key2],
                        percent:Math.round(v2 / v * 10000)/100,
                        value:Math.round(v2/10000),
                        children: []
                    }
                    $.each(three_tree,function(key3,v3){
                        if(key3.indexOf(key2)>-1&&key3!=key2){
                            var node3 = {
                                name: ckjg[key3],
                                percent:Math.round(v3 / v2 * 10000)/100,
                                value:Math.round(v3/10000),
                                children: []
                            }
                            node2.children.push(node3);
                        }
                    })
                    node1.children.push(node2);
                }
            })
            res.children.push(node1);
        })
        return res;
    }
    //保留2位小数
    function handleNum(data){
        if(data){
            return Math.round(data/10000);
        }else{
            return "-";
        }
    }
    exports("dataA_dataHandle", {
        handleMapData: handleMapData,
        handle_ckxx_tree: handle_ckxx_tree,
        handle_dbw_tree:handle_dbw_tree,
        handleNum:handleNum
    })
})