<template>
  <a-modal
    title="报告详情"
    :maskClosable="false"
    v-model="visibleModal"
    width="55%"
    v-if="show3"
    @cancel="handleCancle"
  >
    <template v-if="model.REPORT_TYPE_ID === '3'">
      <template v-if="model.HTML_REPORT">
        <div id="HTML_REPORT2" v-html="model.HTML_REPORT" :contenteditable="isEdit" :key="id"></div>
      </template>
      <div
        v-else
        :contenteditable="isEdit"
        id="HTML_REPORT"
        :key="id"
        style="font-family:SimSun;"
      >
        <h1 style="text-align: center;font-family:SimSun;margin:0px;">{{model.YEAR}}年重庆市国库资金运行分析报告</h1>
        <h2 style="padding:8px 0;">一、重庆市国库资金运行概况</h2>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          <span style="font-weight:700">收入方面:</span>
          {{model.YEAR}}年，重庆市全辖国库收入
          <!--<sup>1</sup>-->
          （不含转移性收入）{{model['000444'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000445'] | dataTreating}}%。其中，公共预算收入{{model['000401'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占比{{model['000419'] | dataTreating}}%，同比{{model['000402'] | dataTreating}}%；基金预算收入{{model['000403'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占比{{model['000418'] | dataTreating}}%，同比{{model['000404'] | dataTreating}}%；地方债收入{{model['000206'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占比{{model['000416'] | dataTreating}}%，同比{{model['000207'] | dataTreating}}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          <span style="font-weight:700">支出方面:</span>
          国库支出{{model['000031'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000032'] | dataTreating}}%。其中，公共预算支出{{model['000037'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000038'] | dataTreating}}%；基金预算支出{{model['000043'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000044'] | dataTreating}}%；债务还本支出{{model['000260'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000261'] | dataTreating}}%（见表1）。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          <span style="font-weight:700">库款方面:</span>
          {{model.MONTH}}月末，重庆市全辖国库库存（不含省级国库现金管理） {{model['000172'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000176'] | dataTreating}}%。分级次看，市级{{model['000177'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000178'] | dataTreating}}%；区县级（含乡镇级） {{model['000180'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000181'] | dataTreating}}%。地方国库现金管理余额为X{{model.AMT_UNIT_NAME}}。
        </div>
        <!-- 注释 -->
        <!-- <block name="remarks">1.由于社保征缴方式处于过渡期，只有城乡居民社会保险纳入预算内，国库收入不含社保收入</block> -->
        <!--<b style="padding-top:50px;line-height:38px;font-size:15px;font-bold:400;" name="remarks">-->
          <!--<div style="width:225px;height:0px;border-top:solid 1px #000 "></div>-->
          <!--<div-->
            <!--style="font-size:15px;"-->
          <!--&gt;1.由于社保征缴方式处于过渡期，只有城乡居民社会保险纳入预算内，国库收入不含社保收入</div>-->
        <!--</b>-->
        <div
          style="text-align: center;line-height:38px;"
        >表1：{{model.YEAR}}年{{model.MONTH}}月重庆市国库收支情况表</div>
        <div
          style="text-align: right;padding-right: 50px;line-height:38px"
        >单位：{{model.AMT_UNIT_NAME}}/%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
          >
            <tr>
              <th rowspan="2" width="28%">项目</th>
              <th colspan="2" width="24%">中央、地方小计</th>
              <th colspan="2" width="24%">中央</th>
              <th colspan="2" width="24%">地方</th>
            </tr>
            <tr>
              <th width="12%">金额</th>
              <th width="12%">同比增减</th>
              <th width="12%">金额</th>
              <th width="12%">同比增减</th>
              <th width="12%">金额</th>
              <th width="12%">同比增减</th>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">公共预算收入</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000401']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000402']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000397']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000398']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000012']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000013']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">基金预算收入</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000403']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000404']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000399']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000400']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000018']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000019']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">一般债务预算收入</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000262']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000263']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000262']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000263']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">地方政府专项债务收入</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000264']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000265']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000264']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000265']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">国有资本经营预算收入</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000024']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000025']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000024']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000025']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">社会保险基金收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000488']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model['000489']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{model['000490']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model['000491']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center; font-weight:700">国库收入合计</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000407']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000408']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000405']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000406']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000005']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000006']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">公共预算支出</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000037']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000038']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000037']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000038']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">基金预算支出</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000043']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000044']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000043']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000044']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">一般债务预算支出</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000266']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000267']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000266']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000267']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">地方政府专项债务还本支出</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000268']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000269']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000268']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000269']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">国有资本经营预算支出</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000049']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000050']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000049']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000050']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">社会保险基金支出</td>
              <td style="font-size:14px;text-align:center;">{{model['000492']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model['000493']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{model['000494']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model['000495']| dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-weight:700;">国库支出合计</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000031']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000032']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">----</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000031']| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab1['000032']| dataTreating}}</td>
            </tr>
          </table>
        </div>
        <h2 style="font-weight:700;padding:8px 0;">二、重庆市国库资金运行主要特点</h2>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（一）公共预算收入情况。</div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          {{model.YEAR}}年，重庆市全辖公共预算收入{{model['000401'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000417'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000402'] | dataTreating}}%，占国库收入的比重为{{model['000419'] | dataTreating}}%。<!--占比较去年同期{{model['000420'] | dataTreating}}个百分点。-->
          <span
            style="font-weight:700"
          >分级次看，</span>中央级公共预算收入{{model['000397'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000421'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000398'] | dataTreating}}%，<!--{{model['000422'] | dataTreating}}，-->占全辖公共预算收入的比重为{{model['000271'] | dataTreating}}%<!--，较去年同期{{model['000272'] | dataTreating}}个百分点-->。地方级公共预算收入{{model['000012'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000392'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000013'] | dataTreating}}%{{str}}。
          <span
            style="font-weight:500"
          >序时看，</span>当季度一般公共预算收入同比{{model['000410'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000424'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000423'] | dataTreating}}%。{{model.YEAR}}年，公共预算收入累计降幅{{model['000402'] | dataTreating}}%{{str1}}<!--{{model['000497'] | dataTreating}}%-->。
          
        </div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（二）税收收入、非税收入情况。</div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          {{model.YEAR}}年，全市税收收入(全级次){{model['000341'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000342'] | dataTreating}}%{{str2}}，税收占公共预算收入的比重为{{model['000345'] | dataTreating}}%；<!--，占比较去年同期{{model['000275'] | dataTreating}}个百分点。-->非税收入{{model['000346'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000347'] | dataTreating}}%。
          <span
            style="font-weight:500"
          >税收分级次看，</span>中央级税收收入{{model['000216'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000217'] | dataTreating}}%；地方级税收收入{{model['000218'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000219'] | dataTreating}}%。
                    
        </div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（三）分税种看，国内流转税、所得税情况。</div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          {{model.YEAR}}年，国内流转税收入{{model['000287'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000288'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000289'] | dataTreating}}%，占税收收入的比重为{{model['000291'] | dataTreating}}%。<!--较去年同期{{model['000499'] | dataTreating}}个百分点。-->其中，国内增值税(含改征增值税){{model['000292'] | dataTreating}}{{model.AMT_UNIT_NAME}},同比{{model['000293'] | dataTreating}}%；国内消费税{{model['000295'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000297'] | dataTreating}}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
         所得税收入合计{{model['000301'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000302'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000303'] | dataTreating}}%；占税收收入比重为{{model['000305'] | dataTreating}}%。<!--较去年同期{{model['000306'] | dataTreating}}个百分点。-->其中，企业所得税{{model['000350'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000351'] | dataTreating}}%，个人所得税{{model['000353'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000354'] | dataTreating}}%。
        </div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（四）分地区看，主城都市区、渝东北三峡库区城镇群、渝东南武陵山区城镇群情况。</div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
         从全级次一般公共预算收入看，全市共有{{model.sum000402big}}个区县实现正增长，其中增幅最大的是{{model.max000402Area}}，同比{{model.max000402}}%。主城都市区、渝东北三峡库区城镇群、渝东南武陵山区城镇群分别实现一般公共预算收入{{model['000518'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000519'] | dataTreating}}，占全市比重分别为{{model['000520'] | dataTreating}}。从税收收入看，全市共有{{model.sum000342}}个区县实现正增长，其中增幅最大的是{{model.max000342Area}}，同比{{model.max000342}}%。主城都市区、渝东北三峡库区城镇群、渝东南武陵山区城镇群分别实现税收收入{{model['000521'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000522'] | dataTreating}}。
        </div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（五）地方政府财力情况。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市地方政府财力为{{model['000230'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000283'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000231'] | dataTreating}}%。<!--增幅较往年同期{{model['000427'] | dataTreating}}个百分点，较前3季度低{{model['000500'] | dataTreating}}个百分点。-->其中，地方自有财力为{{model['000005'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000441'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{ model['000006'] ? model['000006'].includes('-') ? '下降':'增长':'/'}}{{model['000006'] | dataTreating}}%，占政府财力的比重为{{model['000007'] | dataTreating}}%<!--，占比较{{model.YEAR-1}}年同期{{model['000284'] | dataTreating}}个百分点-->；地方债收入{{model['000208'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000278'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000209'] | dataTreating}}%，占政府财力的比重为{{model['000259'] | dataTreating}}%；<!--占比较去年同期{{model['000279'] | dataTreating}}个百分点；-->中央对重庆转移支付收入（含税返）为{{model['000204'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000280'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000205'] | dataTreating}}%，占政府财力比重为{{model['000249'] | dataTreating}}%。<!--占比与{{model.YEAR-1}}年同期相比{{model['000282'] | dataTreating}}个百分点。--></div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（六）土地市场相关情况。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市全辖土地出让收入为{{model['000365'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000393'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000119'] | dataTreating}}%，{{model['000319'] | dataTreating}}，占地方自有财力的比重为{{model['000366'] | dataTreating}}%<!--，较去年同期{{model['000367'] | dataTreating}}个百分点，{{model['000320'] | dataTreating}}-->。从税收数据看，土地相关的税收{{model['000394'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000395'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000396'] | dataTreating}}%。<!--较前3季度{{model['000515'] | dataTreating}}个百分点。-->其中，与房地产市场密切相关的契税金额为{{model['000356'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000323'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000357'] | dataTreating}}%<!--，{{model['000326'] | dataTreating}}-->。分级次看，市级土地出让收入{{model['000501'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000502'] | dataTreating}}%，占全辖比重{{model['000503'] | dataTreating}}%；区县（含乡镇）级土地出让收入{{model['000504'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000505'] | dataTreating}}%。</div>
<div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（七）进出口情况。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，进出口三税（进口货物增值税、进口消费品消费税和关税）合计收入{{model['000506'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000507'] | dataTreating}}%，{{model['000508'] | dataTreating}}。其中，进口货物增值税{{model['000411'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000412'] | dataTreating}}%；进口消费品消费税{{model['000509'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000510'] | dataTreating}}%；关税{{model['000413'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000414'] | dataTreating}}%。出口方面，出口货物退增值税{{model['000516'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000517'] | dataTreating}}%；出口消费品退消费税{{model['000511'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000512'] | dataTreating}}%。</div>
<div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（八）国库支出情况。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市全辖国库支出{{model['000031'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000331'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000032'] | dataTreating}}%，其中，公共预算支出{{model['000037'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000332'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000038'] | dataTreating}}%。分项目看：一般性公共服务支出{{model['000368'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占国库支出比重为{{model['000370'] | dataTreating}}%；社会保障和就业、卫生健康、农林水、教育、城乡社区、住房保障等与民生直接相关的领域共计支出
{{model['000372'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000374'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000373'] | dataTreating}}%，占国库支出比重为{{model['000376'] | dataTreating}}%。<!--较去年同期{{model['000377'] | dataTreating}}个百分点。-->分金额看，支出金额排名前3位的为{{model.subject_1}}，金额分别{{model.value_4}}，占比分别为{{model.ratio_1}}。分地区看，主城都市区、渝东北三峡库区城镇群、渝东南武陵山区城镇群公共预算支出增速分别为{{model['000513'] | dataTreating}}；增速排名前三区县的分别为{{model['000514'] | dataTreating}}。</div>
 <!--        <div style="text-align: center;line-height:38px;">图1:公共预算收入走势图</div>
        <div id="echars0" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">图2：中央与地方公共预算收入占比情况</div>
        <div id="echars1" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">图3:税收与非税收入走势图</div>
        <div id="echars2" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">图4：重庆市政府财力占比图</div>
        <div id="echars3" style="height:300px;width:100%;text-align:center;"></div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（二）地方政府财力小幅提升，债务收入大幅增长，中央转移支付收入增幅减缓，自有财力占比同比降低。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市地方政府财力（地方级公共预算收入+基金预算收入+国有资金经营预算收入+地方债收入+中央转移性收入(省级一般转移性收入+税返收入)）为{{model['000230'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000283'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000231'] | dataTreating}}%，增幅较往年同期{{model['000427'] | dataTreating}}个百分点。其中，地方债收入{{model['000208'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000278'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000209'] | dataTreating}}%，占政府财力的比重为{{model['000259'] | dataTreating}}，占比较{{model.YEAR-1}}年{{model['000279'] | dataTreating}}个百分点；中央对重庆转移支付收入（含税返）为{{model['00027901'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000280'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['00028001'] | dataTreating}}%，增速较去年同期{{model['000281'] | dataTreating}}个百分点，占政府财力比重为{{model['00028101'] | dataTreating}}%，占比与{{model.YEAR-1}}年持平；地方自有财力（地方级公共预算收入+基金预算收入+国有资金经营预算收入）为{{model['000005'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000441'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占政府财力的比重为{{model['000007'] | dataTreating}}%，占比较去年{{model['000284'] | dataTreating}}个百分点，{{model['000285'] | dataTreating}}</div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（三）地方级公共预算收入未完成年度预算进度，公共预算支出超年度预算，各区县完成进度不一。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，按财政口径计算，地方级公共预算收入{{model['000012'] | dataTreating}}{{model.AMT_UNIT_NAME}}，完成年度预算进度X，低于序时进度X，主要原因是减税降费政策效应持续显现。公共预算支出{{model['000037'] | dataTreating}}{{model.AMT_UNIT_NAME}}，完成年度预算进度X，在年中调高预算X{{model.AMT_UNIT_NAME}}的情况下，仍超年度预算X个百分点。分区县看，完成进度不一。如奉节县公共预算支出进度为X，超预算进度X个百分点，城口县公共预算支出完成进度为X，低于预算进度X个百分点；酉阳县公共预算收入完成进度为X，高于预算进度X个百分点，开州区公共预算收入完成进度为X，低于预算进度X个百分点。</div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（四）国内增值税近4年首次负增长，国内消费税持续快速增长；企业所得税降幅进一步扩大，个人所得税连续14个月负增长，带动税收收入负增长。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市全辖各级次税收收入{{model['000341'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000343'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000342'] | dataTreating}}%，降幅较前3季度{{model['000286'] | dataTreating}}个百分点，较去年同期{{model['000429'] | dataTreating}}个百分点。国内流转税（国内增值税+国内消费税）收入{{model['000287'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000288'] | dataTreating}}{{model.AMT_UNIT_NAME}}、{{model['000289'] | dataTreating}}%，增速较去年同期{{model['000290'] | dataTreating}}个百分点，占税收收入的比重为{{model['000291'] | dataTreating}}%（见图5）。其中，国务院出台的增值税降税率、统标准税收优惠政策效应显现，国内增值税(含原国内增值税、改征增值税){{model['000292'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000293'] | dataTreating}}%，年度累计数近X年来首次负增长，增速较去年同期{{model['000294'] | dataTreating}}个百分点。受烟草制造业X年X季度税收入库时间延迟至X年X季度影响，国内消费税{{model['000295'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000296'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000297'] | dataTreating}}%，{{model['000298'] | dataTreating}}个百分点，占比为{{model['000299'] | dataTreating}}，{{model['000300'] | dataTreating}}个百分点。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >所得税收入（企业所得税+个人所得税）合计{{model['000301'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000302'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000303'] | dataTreating}}%，{{model['000304'] | dataTreating}}个百分点；占比为{{model['000305'] | dataTreating}}%，{{model['000306'] | dataTreating}}个百分点。受工业企业利润增长放缓，小型微利企业可享受税收优惠的门槛降低影响，企业所得税{{model['000350'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000351'] | dataTreating}}%，{{model['000310'] | dataTreating}}个百分点，近X年来首次负增长；受个人所得税起征点调高、个税专项扣除正式实施影响，个人所得税{{model['000353'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000314'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000354'] | dataTreating}}%，较去年{{model['000316'] | dataTreating}}个百分点，连续X个月负增长，带动税收收入负增长（图6）。</div>
        <div style="text-align:center;line-height:38px;">图5：流转税增速及占比走势图</div>
        <div id="echars4" style="height:300px;width:700px;"></div>
        <div style="text-align:center;line-height:38px;">图6：所得税增速及占比走势图</div>
        <div id="echars5" style="height:300px;width:700px;"></div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（五）土地供应量加大，土地出让收入及土地相关税收降幅有所收窄，房地产市场遇冷，房地产业税收收入增速持续减缓。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >X月，重庆市商品房销售面积及销售金额齐降，降幅分别为X，降幅较上半年分别X、X个百分点；在年末两个月加大土地供应的刺激下，{{model.MONTH}}月重庆土地成交面积X万平方米，同比X%，降幅较前X季度X个百分点，成交金额共计X{{model.AMT_UNIT_NAME}}，同比X%，增速较前X季度X个百分点。上述因素影响下，重庆市全辖土地出让收入（科目1030148+1030146+1030147+1030718）{{model['000365'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000393'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000119'] | dataTreating}}%，降幅较前3季度{{model['000319'] | dataTreating}}个百分点（详见图7）；占地方自有财力的比重为{{model['000366'] | dataTreating}}%，较去年同期和前{{model.lastQtr}}季度分别{{model['000367'] | dataTreating}}个百分点、{{model['000320'] | dataTreating}}个百分点。土地相关的税收（房产税、城镇土地使用税、土地增值税、耕地占用税、契税）{{model['000394'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000395'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000396'] | dataTreating}}%，{{ model['000396'] ? model['000396'].includes('下降') ? '减收':'增收':'/'}}规模较前{{model.lastQtr}}季度{{model['000321'] | dataTreating}}{{model.AMT_UNIT_NAME}}。其中，与房地产市场密切相关的契税金额为{{model['000356'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000323'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000357'] | dataTreating}}%，较前{{model.lastQtr}}季度{{model['000326'] | dataTreating}}个百分点。在房地产市场交易趋冷带动下，房地产行业税收增速持续减缓，1-X月房地产业税收收入X{{model.AMT_UNIT_NAME}}，同比X%，增速较去年同期及前{{model.lastQtr}}季度分别X、X个百分点，增速持续减缓，占全行业税收比重为X%，占比较去年同期X个百分点。</div>
        <div style="text-align:center;line-height:38px;">图7：土地出让收入趋势图</div>
        <div id="echars6" style="height:300px;width:700px;"></div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（六）汽车制造业转型升级取得新进展，汽车制造业税收降幅持续收窄。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >汽车产业转型升级、产品更新换代取得新进展，重庆永川区长城整车生产基地投产，长安汽车推出新车型，带动汽车行业产销提升。如长安汽车X月累计产销量同比X%、X%，降幅较前X季度分别X、X个百分点。国内汽车销售萎靡大环境下，北汽银翔、力帆汽车等低端车型为主的车企，面临发展瓶颈，亏损严重，汽车制造业税收收入持续负增长，但降幅持续收窄。X月汽车制造业税收共计X{{model.AMT_UNIT_NAME}}，同比X{{model.AMT_UNIT_NAME}}，下降X%，降幅较前X季度及上半年分别收窄X、X个百分点。</div>
        <div class="indent" style="font-weight:700;line-height:38px;">（七）进出口总增速持续下滑，出口增速减缓，进口增速小幅反弹，关税增速由负转正。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >1-X月，重庆市进出口X{{model.AMT_UNIT_NAME}}，同比X%，较全国X个百分点，但全球经济放缓、中美贸易摩擦负面影响逐渐显现，进出口增速较前X季度下降X个百分点，增速连续X个月放缓。受企业“抢出口”，对“一带一路”沿线国家贸易快速增长带动，出口X%，较上年同期X个百分点，连续X个月放缓，受X月X日起增值税税率下降影响，出口货物退增值税X{{model.AMT_UNIT_NAME}}，同比X%（详见图8）；从进口看，全市进口额同比X%，较前X季度和上年同期分别X个和X个百分点，在前期X个月连续放缓后，X月出现小幅反弹。进口货物增值税{{model['000411'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000412'] | dataTreating}}%；关税{{model['000413'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000414'] | dataTreating}}%（详见图9）。</div>
        <div style="text-align:center;line-height:38px;">图8 出口环节税收累计增速</div>
        <div id="echars7" style="height:300px;width:700px;"></div>
        <div style="text-align: center;line-height:38px;">图9 进口环节税收累计增速</div>
        <div id="echars8" style="height:300px;width:700px;"></div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（八）非税收入降幅收窄，行政事业性收费收入降幅居前，政府住房基金收入暴增，四季度国有资源（资产）有偿使用收入大幅增收。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市全辖非税收入{{model['000346'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000348'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000347'] | dataTreating}}%，较前{{model.lastQtr}}季度{{model['000327'] | dataTreating}}个百分点，连续6个季度负增长，占一般公共预算收入的比重为{{model['000349'] | dataTreating}}%，占比较去年同期{{model['000328'] | dataTreating}}个百分点。受重庆市财政局调整城市建设配套费列报方式，从行政事业性收费收入调整至基金预算收入科目以及积极实施减税降费政策等因素影响，行政事业性收费收入{{model['000359'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000329'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000360'] | dataTreating}}%。受重庆市政府将公租房项目中配建的商业设施出售给重庆发展投资有限公司一次性因素影响，政府住房基金收入{{model['000362'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000330'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000363'] | dataTreating}}%。受四季度各级行政事业单位处置资产一次性因素影响，国有资源（资产）有偿使用收入当季{{model['000431'] | dataTreating}}{{model.AMT_UNIT_NAME}}，导致全年非税收入降幅较前X季度有所收窄。</div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（九）国库支出持续增长，民生领域支出为重点保障领域。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.YEAR}}年，重庆市全辖国库支出{{model['00033001'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000331'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['00033002'] | dataTreating}}%，其中，公共预算支出{{model['000037'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000332'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000432'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000038'] | dataTreating}}%。围绕市委、市政府决策部署，压缩一般性项目支出，整合资源，全力保障民生和重点领域支出。分项目看：一般性公共服务支出{{model['000368'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占比为{{model['000370'] | dataTreating}}%，与去年{{model['000371'] | dataTreating}}持平；社会保障和就业、卫生健康、农林水、教育、城乡社区、住房保障等与民生直接相关的领域共计支出{{model['000372'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000374'] | dataTreating}}{{model.AMT_UNIT_NAME}}，增量占支出总增量的比重为{{model['000375'] | dataTreating}}%，同比{{model['000373'] | dataTreating}}%，占国库支出比重为{{model['000376'] | dataTreating}}%，较去年同期{{model['000377'] | dataTreating}}个百分点。分金额看，支出金额排名前3位的为城乡社区支出、社会保障和就业支出、教育支出，金额分别2938.90{{model.AMT_UNIT_NAME}}、884.69{{model.AMT_UNIT_NAME}}、706.28{{model.AMT_UNIT_NAME}}，占比分别为38.88%、11.7%、9.34%。（详见表2）</div> -->
        <div style="text-align:center;line-height:38px;">表2 {{model.YEAR}}年重庆市国库支出主要科目情况</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="order-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
          >
            <tr>
              <th style="font-weight:700" width="25%">科目名称</th>
              <th style="font-weight:700" width="25%">支出金额（{{model.AMT_UNIT_NAME}}）</th>
              <th style="font-weight:700" width="25%">同比增速（％）</th>
              <th style="font-weight:700" width="25%">
                在国库支出中的
                占比（％）
              </th>
            </tr>
            <tr>
              <td style="text-align:center;">城乡社区支出</td>
              <td style="text-align:center;">{{tab2['000389'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000390'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000391'] | dataTreating}}</td>
            </tr>
            <tr>
              <td style="text-align:center;">社会保障和就业支出</td>
              <td style="text-align:center;">{{tab2['000378'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000379'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000380'] | dataTreating}}</td>
            </tr>
            <tr>
              <td style="text-align:center;">教育支出</td>
              <td style="text-align:center;">{{tab2['000386'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000387'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000388'] | dataTreating}}</td>
            </tr>
            <tr>
              <td style="text-align:center;">农林水支出</td>
              <td style="text-align:center;">{{tab2['000384'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000385'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000333'] | dataTreating}}</td>
            </tr>
            <tr>
              <td style="text-align:center;">一般公共服务支出</td>
              <td style="text-align:center;">{{tab2['000368'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000369'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000370'] | dataTreating}}</td>
            </tr>
            <tr>
              <td style="text-align:center;">卫生健康支出</td>
              <td style="text-align:center;">{{tab2['000381'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000382'] | dataTreating}}</td>
              <td style="text-align:center;">{{tab2['000383'] | dataTreating}}</td>
            </tr>
          </table>
        </div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（九）库存情况。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.MONTH}}月末，重庆市全辖国库库存余额{{model['000172'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000175'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000176'] | dataTreating}}%。分级次看，市级库存{{model['000177'] | dataTreating}}{{model.AMT_UNIT_NAME}}、同比{{model['000334'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000178'] | dataTreating}}%，占全市库存比重{{model['000179'] | dataTreating}}%、较去年同期{{model['000336'] | dataTreating}}个百分点；区县级（含乡镇）库存{{model['000180'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000337'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000181'] | dataTreating}}%，占全市库存比重{{model['000181'] | dataTreating}}%，较去年同期{{model['000339'] | dataTreating}}个百分点。
        分区县看，库存居前三位的为{{model.stock_area_1}}，金额分别为{{model.value_7}}；而{{model.stock_area_2}}库存较低，排在末三位，金额分别为{{model.value_8}}（见表3）。 </div>
<!--         <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（十）小微企业减免退税优惠政策，带动减免退库大幅增长。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >2019年，重庆市全辖共计退库X{{model.AMT_UNIT_NAME}}，同比X%，降幅将上半年X个百分点。按退库级次看，中央级退库X{{model.AMT_UNIT_NAME}}，同比X%，占比为X%，地方级退库X{{model.AMT_UNIT_NAME}}，同比X%。按退库种类看，出口产品退库为X{{model.AMT_UNIT_NAME}}，同比X%，占比为X%，受重庆市分库高效办理小微企业退库，有效保证小微企业普惠性税收减免政策落地生效，减免退库X{{model.AMT_UNIT_NAME}}，同比大幅X%，汇算清缴退库X{{model.AMT_UNIT_NAME}}，同比大幅X%。</div>
        <div
          class="indent"
          style="font-weight:700;text-indent:2em;line-height:38px;"
        >（十一）日均库存余额走低，年末市级、区县级库款占比均回落，财政支出年底集中拨付导致库存波动。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.MONTH}}月末，重庆市全辖国库库存余额{{model['000172'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000175'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000176'] | dataTreating}}%。分级次看，市级库存{{model['000177'] | dataTreating}}{{model.AMT_UNIT_NAME}}、同比{{model['000334'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000178'] | dataTreating}}%，环比{{model['000335'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占全市库存比重{{model['000179'] | dataTreating}}%、较去年同期{{model['000336'] | dataTreating}}个百分点；区县级（含乡镇）库存{{model['000180'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000337'] | dataTreating}}{{model.AMT_UNIT_NAME}}，{{model['000181'] | dataTreating}}%，环比{{model['000338'] | dataTreating}}{{model.AMT_UNIT_NAME}}，占全市库存比重{{model['000182'] | dataTreating}}%，较去年同期{{model['000339'] | dataTreating}}个百分点（见图10）。</div>
        <div
          class="indent"
          style="text-indent:2em;line-height:38px;"
        >{{model.MONTH}}月全辖日均库存余额{{model['000168'] | dataTreating}}{{model.AMT_UNIT_NAME}}，同比{{model['000169'] | dataTreating}}%。年底最后两周（12月18日-31日）库存波动幅度较大，X{{model.AMT_UNIT_NAME}}、X%，期间最高一日库存下降幅度达X% ，主要是因为年末财政支出集中拨付，支出项目较多，特别是土地出让支出、征地和拆迁补偿支出、城市建设支出等资金拨付较多，导致库存快速下降，如{{model.MONTH}}月份巴南区土地出让支出{{model.max_area_value}}，导致巴南支库库款大幅下降。分区县看，库存居前三位的为{{model.stock_area_1}}，金额分别为{{model.value_7}}{{model.AMT_UNIT_NAME}}；而{{model.stock_area_2}}库存较低，排在末三位，金额分别为{{model.value_8}}（见表3）。</div>
        <div style="text-align: center;line-height:38px;">图10: 2016.01- 2019.12库存走势图</div>
        <div id="echars9" style="height:300px;width:700px;"></div> -->
        <div
          style="text-align:center;line-height:38px;"
        >表3 {{model.YEAR}}年{{model.MONTH}}月{{model.LAST_DAY}}日重庆市全辖库存情况</div>
        <div
          style="text-align:right;padding-right: 50px;line-height:38px"
        >单位：{{model.AMT_UNIT_NAME}}/%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
          >
            <tr>
              <th width="10%">地区名称</th>
              <th width="12%">金额</th>
              <th width="14%">比去年同期增加额</th>
              <th width="14%">比上月末增加额</th>
              <th width="10%">地区名称</th>
              <th width="12%">金额</th>
              <th width="14%">比去年同期增加额</th>
              <th width="14%">比上月末增加额</th>
            </tr>
            <!-- <tr>
              <td style="font-size:14px;text-align:center;">重庆市</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table1 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table2 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table4 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">九龙坡区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table116 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table117 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table119 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">永川区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table46 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table47 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table49 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">铜梁区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table136 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table137 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table139 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;"> 合川区</td>
              <td style="font-size:14px;text-align:center;"> {{tab3.table31 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"> {{tab3.table32 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"> {{tab3.table34 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"> 綦江区</td>
              <td style="font-size:14px;text-align:center;"> {{tab3.table156 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"> {{tab3.table157 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"> {{tab3.table159 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">江津区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table51 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table52 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table54 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">巫山县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table106 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table107 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table109 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">巫溪县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table61 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table62 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table64 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">奉节县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table121 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table122 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table124 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">荣昌区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table41 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table42 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table44 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">云阳县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table101 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table102 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table104 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">大渡口区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table56 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table57 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table54 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">酉阳县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table131 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table132 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table134 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">城口县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table36 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table37 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table39 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">梁平区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table181 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table182 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table184 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">涪陵区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table6 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table7| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table9 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">忠县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table151 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table152 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table154 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">江北区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table126 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table127 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table129 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">黔江区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table176 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table177 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table179 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">北碚区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table86 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table87 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table89 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">潼南区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table161 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table162 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table164 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">垫江县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table76 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table77 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table79 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">高新区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table146 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table147 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table149 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">长寿区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table96 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table97 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table99 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">丰都县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table206 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table207 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table209 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">两江新区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table26 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table27 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table29 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">秀山县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table171 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table172 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table174 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">璧山区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table21 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table22 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table24 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">大足区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table201 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table202 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table204 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">南川区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table91 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table92 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table94 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">巴南区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table16 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table17 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table19 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">南岸区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table71 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table72 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table74 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">万州区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table81 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table82 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table84 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">开州区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table66 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table67 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table69 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">万盛开发区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table196 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table197 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table199 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">渝中区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table111 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table112 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table114 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">石柱县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table166 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table167 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table169 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">沙坪坝区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table141 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table142 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table144 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">渝北区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table11 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table12 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table14 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">武隆区</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table186 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table187 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table189 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">彭水县</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table191 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table192 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{tab3.table194 | dataTreating}}</td>
            </tr> -->
            <!-- 按照从大到小排列 -->
            <tr v-for="(item,index) in tabLeft4" :key="index">
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.AREA_DSCR}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE_2}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE_3}}</td>
              <!-- 右边列 -->
              <td
                style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;"
                v-if="tabRight4[index]"
              >{{tabRight4[index].AREA_DSCR}}</td>
              <td
                style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;"
                v-if="tabRight4[index]"
              >{{tabRight4[index].INDEX_VALUE}}</td>
              <td
                style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;"
                v-if="tabRight4[index]"
              >{{tabRight4[index].INDEX_VALUE_2}}</td>
              <td
                style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;"
                v-if="tabRight4[index]"
              >{{tabRight4[index].INDEX_VALUE_3}}</td>
            </tr>
          </table>
        </div>
      </div>
    </template>
    <canvas id="canvas" style="border: 1px solid #ccc;display:none"></canvas>
    <template slot="footer">
      <template v-if="isEdit === false">
        <a-button type="primary" @click="e => {isEdit = true;}">编辑</a-button>
        <a-button type="primary" @click="handleExport()">导出</a-button>
      </template>
      <template v-else>
        <a-button @click="handleCancle">取消</a-button>
        <a-button type="primary" @click="handleOk()">保存</a-button>
      </template>
    </template>
  </a-modal>
</template>

<script>
import echarts from 'echarts'
import { getQuarterReport, editEntityReport } from '@/api/intelligenceReport'
export default {
  name: 'quarterlyReport',
  data() {
    return {
      visibleModal: false,
      isEdit: false,
      model: {},
      id: +new Date(), //重新渲染dom控制
      tab1: {},
      tab2: {},
      tab3: {},
      show3:false,
      state:'1',
      tabLeft4:[],
      tabRight4:[],
      str:'',
      str1:'',
      str2:''
    }
  },
  filters: {
    //处理空数据显示
    dataTreating(value) {
      // console.log(value)
      if (!value) return '/'
      if (value.length <= 3) {
        return value
      } else {
        return value.replace(/\|/g, ',')
      }
    },
  },
  /* computed: {
      str () {
        console.log(this.model)
        if(this.model.qtr === '1'){
          return ''
        }else{
          debugger
          if(this.model['000013'].includes('增长')&&parseInt(this.model['000496']>0)){
            return '增幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000496']+'个百分点'
          }else if(this.model['000013'].includes('增长')&&parseInt(this.model['000496']<0)){
            return '增幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000496']+'个百分点'
          }else if(this.model['000013'].includes('下降')&&parseInt(this.model['000496']>0)){
            return '降幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000496']+'个百分点'
          }else if(this.model['000013'].includes('下降')&&parseInt(this.model['000496']<0)){
            return '降幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000496']+'个百分点'
          }
           
        }
      }
    }, */
  methods: {
    edit(record) {
      this.visibleModal = true
      this.show3 =  true
      this.model = {};
      Object.assign(this.model, record)
      // console.log(this.model, 'mmmmmm')
      if (!this.model.HTML_REPORT) {
        getQuarterReport(record).then((res) => {
          if (res.result === 'success') {
            let data = res.rows
            console.log(data)
            // 根据数据情况赋值
            if (data.textList) {
              this.model = Object.assign({}, this.model, data.textList, data.AMT_UNIT_NAME)
              console.log(this.model)
              if(this.model.qtr === '1'){
          this.str = ''
        }else{
          console.log(this.model['000013'])
          if(this.model['000013'].includes('增长')&&parseInt(this.model['000496'])>0){
            this.str =  '，增幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000496']+'个百分点'
          }else if(this.model['000013'].includes('增长')&&parseInt(this.model['000496'])<0){
            this.str =  '，增幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000496']+'个百分点'
          }else if(this.model['000013'].includes('下降')&&parseInt(this.model['000496'])>0){
            this.str =  '，降幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000496']+'个百分点'
          }else if(this.model['000013'].includes('下降')&&parseInt(this.model['000496'])<0){
            this.str =  '，降幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000496']+'个百分点'
          }
           
        }
        if(data.textList.qtr === '1'){
          this.str1 = ''
        }else{
          console.log(this.model['000402'])
          if(this.model['000402'].includes('增长')&&parseInt(this.model['000497'])>0){
            this.str1 =  '，增幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000497']+'个百分点'
          }else if(this.model['000402'].includes('增长')&&parseInt(this.model['000497'])<0){
            this.str1 =  '，增幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000497']+'个百分点'
          }else if(this.model['000402'].includes('下降')&&parseInt(this.model['000497'])>0){
            this.str1 =  '，降幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000497']+'个百分点'
          }else if(this.model['000402'].includes('下降')&&parseInt(this.model['000497'])<0){
            this.str1 =  '，降幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000497']+'个百分点'
          }
           
        }
        if(data.textList.qtr === '1'){
          this.str2 = ''
        }else{
          console.log(this.model['000342'])
          if(this.model['000342'].includes('增长')&&parseInt(this.model['000498'])>0){
            this.str2 =  '，增幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000498']+'个百分点'
          }else if(this.model['000342'].includes('增长')&&parseInt(this.model['000498'])<0){
            this.str2 =  '，增幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000498']+'个百分点'
          }else if(this.model['000342'].includes('下降')&&parseInt(this.model['000498'])>0){
            this.str2 =  '，降幅较第'+(this.model.qtr-1)+'季度扩大'+this.model['000498']+'个百分点'
          }else if(this.model['000342'].includes('下降')&&parseInt(this.model['000498'])<0){
            this.str2 =  '，降幅较第'+(this.model.qtr-1)+'季度收窄'+this.model['000498']+'个百分点'
          }
           
        }
            }
            if (data.tableParams) {
              this.tab1 = Object.assign({}, data.tableParams)
              console.log(this.tab1)
            }
            if (data.tableParams2) {
              this.tab2 = Object.assign({}, data.tableParams2)
            }
            if (data.tableParams3) {
              this.tab3 = Object.assign({}, data.tableParams3)
              console.log(this.tab3, 'tttttttttttt333333333')
            }
            this.tabLeft4 = data.tableParams3.slice(0, Math.ceil(data.tableParams3.length / 2))
              // console.log(this.tabLeft4, 'tttttlllll4')
            this.tabRight4 = data.tableParams3.slice(Math.ceil(data.tableParams3.length / 2))
            //echarts图 处理
           /*  if (data.echartsData && data.echartsData.length > 0) {
              console.log(data.echartsData, '111')
              // debugger
              this.$nextTick((event) => {
                data.echartsData.map((item, index) => {
                  let options = {}
                  if (this.model.REPORT_TYPE_ID === '3') {
                    switch (index) {
                      // 图1 柱状折线图
                      case 0:
                        options = {
                          legend: {
                            data: ['当季公共预算收入', '公共预算收入累计增速'],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL1,
                              max: item.MAX_VAL1,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                            {
                              type: 'value',
                              min: item.MIN_VAL2,
                              max: item.MAX_VAL2,
                              // interval: cc1 / 5,
                              axisLabel: {
                                show: true,
                                interval: 'auto',
                                formatter: function (value) {
                                  return value.toFixed(2)
                                },
                              },
                            },
                          ],
                          series: [
                            {
                              name: '当季公共预算收入',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barWidth: 10, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '公共预算收入累计增速',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              symbol: 'rectangle', // 正方形
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图2 柱状折线图
                      case 1:
                        options = {
                          legend: {
                            data: ['中央当季收入', '地方当季收入', '中央累计增速', '地方累计增速'],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL1,
                              max: item.MAX_VAL1,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2)
                                },
                              },
                            },
                            {
                              type: 'value',
                              min: item.MIN_VAL2,
                              max: item.MAX_VAL2,
                              // interval: cc1 / 5,
                              axisLabel: {
                                show: true,
                                interval: 'auto',
                                formatter: function (value) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                          ],
                          series: [
                            {
                              name: '中央当季收入',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barWidth: 10, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '地方当季收入',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barWidth: 10, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '中央累计增速',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              symbol: 'rectangle', // 正方形
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '地方累计增速',
                              data: item.series4.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              symbol: 'triangle', //三角形
                              itemStyle: {
                                normal: {
                                  color: '#63FFFF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#63FFFF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图3 折线图
                      case 2:
                        options = {
                          legend: {
                            data: ['非税收入累计增速', '税收收入累计增速', '税收收入占公共预算收入比重(右)'],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL1,
                              max: item.MAX_VAL1,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                            {
                              type: 'value',
                              min: item.MIN_VAL2,
                              max: item.MAX_VAL2,
                              // interval: cc1 / 5,
                              axisLabel: {
                                show: true,
                                interval: 'auto',
                                formatter: function (value) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                          ],
                          series: [
                            {
                              name: '非税收入累计增速',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '税收收入累计增速',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '税收收入占公共预算收入比重(右)',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图4 折线图
                      case 3:
                        options = {
                          legend: {
                            data: ['地方值占比', '自有财力占比', '转移性收入占比'],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: {
                            type: 'value',
                            min: item.MIN_VAL1,
                            max: item.MAX_VAL1,
                            // interval: bb1 / 5
                            axisLabel: {
                              formatter: function (value, index) {
                                return value.toFixed(2) + '%'
                              },
                            },
                          },
                          series: [
                            {
                              name: '地方值占比',
                              data: item.series1?item.series1.replace(/\[|]/g, '').split(','):[],
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '自有财力占比',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '转移性收入占比',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图5 折线图
                      case 4:
                        options = {
                          legend: {
                            data: ['国内增值税累计增速', '国内消费税累计增速', '国内流转税占税收收入比重(右)'],
                          },
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          animation: false,
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL1,
                              max: item.MAX_VAL1,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                            {
                              type: 'value',
                              min: item.MIN_VAL2,
                              max: item.MAX_VAL2,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                          ],
                          series: [
                            {
                              name: '国内增值税累计增速',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '国内消费税累计增速',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '国内流转税占税收收入比重(右)',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图6 折线图
                      case 5:
                        options = {
                          legend: {
                            data: ['企业所得税累计增速', '个人所得税累计增速', '所得税占税收收入比重(右)'],
                          },
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          animation: false,
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL1,
                              max: item.MAX_VAL1,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                            {
                              type: 'value',
                              min: item.MIN_VAL2,
                              max: item.MAX_VAL2,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                          ],
                          series: [
                            {
                              name: '企业所得税累计增速',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '个人所得税累计增速',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '所得税占税收收入比重(右)',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图7 柱状折线图
                      case 6:
                        options = {
                          legend: {
                            data: [
                              '土地出让收入累计金额(右)',
                              '土地出让收入累计增幅',
                              '土地出让收入占地方自有财力比重',
                            ],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL1,
                              max: item.MAX_VAL1,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                            },
                            {
                              type: 'value',
                              min: item.MIN_VAL2,
                              max: item.MAX_VAL2,
                              // interval: cc1 / 5,
                              axisLabel: {
                                show: true,
                                interval: 'auto',
                                formatter: function (value) {
                                  return value.toFixed(2)
                                },
                              },
                            },
                          ],
                          series: [
                            {
                              name: '土地出让收入累计金额(右)',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barWidth: 10, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '土地出让收入累计增幅',
                              data: item.series2?item.series2.replace(/\[|]/g, '').split(','):[],
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '土地出让收入占地方自有财力比重',
                              data: item.series3?item.series3.replace(/\[|]/g, '').split(','):[],
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF00FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF00FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图8 折线图
                      case 7:
                        options = {
                          legend: {
                            data: ['出口总值累计增速', '出口货物退增值税累计增速'],
                          },
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          animation: false,
                          yAxis: {
                            type: 'value',
                            min: item.MIN_VAL1,
                            max: item.MAX_VAL1,
                            axisLabel: {
                              formatter: function (value, index) {
                                return value.toFixed(2) + '%'
                              },
                            },
                          },
                          series: [
                            {
                              name: '出口总值累计增速',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '出口货物退增值税累计增速',
                              data: item.series2?item.series2.replace(/\[|]/g, '').split(','):[],
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图9 折线图
                      case 8:
                        options = {
                          legend: {
                            data: ['进口总值累计增速', '进口货物增值税累计增速', '关税累计增速'],
                          },
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: {
                            type: 'value',
                            min: item.MIN_VAL1,
                            max: item.MAX_VAL1,
                            axisLabel: {
                              formatter: function (value, index) {
                                return value.toFixed(2) + '%'
                              },
                            },
                          },
                          animation: false,
                          series: [
                            {
                              name: '进口总值累计增速',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '进口货物增值税累计增速',
                              data: item.series2?item.series2.replace(/\[|]/g, '').split(','):[],
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '关税累计增速',
                              data: item.series3?item.series3.replace(/\[|]/g, '').split(','):[],
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFFFCC', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFFFCC', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 图10 折线图
                      case 9:
                        options = {
                          legend: {
                            data: ['2016年', '2017年', '2018年', '2019年'],
                          },
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: {
                            type: 'value',
                            min: item.MIN_VAL1,
                            max: item.MAX_VAL1,
                            axisLabel: {
                              formatter: function (value, index) {
                                return value.toFixed(2)
                              },
                            },
                          },
                          animation: false,
                          series: [
                            {
                              name: '2016年',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#9999FF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#9999FF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '2017年',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF6600', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF6600', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '2018年',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#969696', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#969696', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '2019年',
                              data: item.series4.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FDCE13', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FDCE13', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                    }
                    let myChart1 = echarts.init(document.getElementById('echars' + index))
                    //console.log(myChart1)
                    myChart1.setOption(options)
                    // console.log(myChart1)
                    let src = myChart1.getDataURL()
                    // console.log(src, '1111111111')
                    //myChart1.dispose() // echart的销毁
                    // console.log(myChart1)
                    let canvas = document.getElementById('canvas')
                    // // // console.log(canvas)
                    let context = canvas.getContext('2d')
                    // console.log(context) // 创建新图片
                    let img = new Image()
                    img.src = src
                    // console.log(img.src, '222222')
                    let imgbase64 = ''
                    img.addEventListener('load', function () {
                      canvas.width = img.width
                      canvas.height = img.height
                      // console.log(canvas)
                      context.fillStyle = '#fff'
                      context.fillRect(0, 0, canvas.width, canvas.height)
                      context.drawImage(img, 0, 0, canvas.width, canvas.height)
                      imgbase64 = canvas.toDataURL('image/png')
                      // console.log(imgbase64)
                      document.getElementById('echars' + index).innerHTML = `<img src="${imgbase64}"/>`
                    })
                  }
                })
              })
            } */
          }
        })
      }
    },
    //取消
    handleCancle() {
      this.isEdit = false
      this.show3 = false
      this.id = +new Date()
    },
    //保存
    async handleOk() {
      // console.log(this.model.HTML_REPORT ? this.model.HTML_REPORT : document.getElementById('HTML_REPORT').innerHTML)
      console.log(this.model.REPORT_ID)
      // debugger
      let params = {
        HTML_REPORT: this.model.HTML_REPORT ? document.getElementById('HTML_REPORT2').innerHTML : document.getElementById('HTML_REPORT').innerHTML,
        REPORT_ID: this.model.REPORT_ID,
      }
      const res = await editEntityReport(params)
      if (res.result === 'success') {
        console.log(res)
        this.handleClose()
        this.$emit('ok')
      }
      this.$message[res.result === 'success' ? 'success' : 'warning']
    },
    handleClose() {
      this.isEdit = false
      this.visibleModal = false
      this.show3 = false
    },
    //导出
    async handleExport() {
      var sUserAgent = navigator.userAgent;
      var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
      if(isWin){
        var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
        if(isWin7){
          this.state = '0'
        }else{
          this.state = '1'
        }
      }
      console.log(
        `${window._CONFIG['domianURL']}/fixedReport/qurReport/downLoadCheckList?guoku_id=${
          this.$sessionStorage.ls.get('Login_Userinfo').guokuId
        }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
          this.$sessionStorage.ls.get('Login_Userinfo').id
        }&REPORT_ID=${this.model.REPORT_ID}&state=${this.state}`
      )
      // 判断是否修改了文档   如果没有这个字段 说明未修改 则先保存这个html文件 然后在执行下载操作
      if (!this.model.HTML_REPORT) {
        // 调用保存文档的方法
        let params = {
          HTML_REPORT: document.getElementById('HTML_REPORT').innerHTML,
          REPORT_ID: this.model.REPORT_ID,
        }
        console.log(params)
        // debugger
        await editEntityReport(params)
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/qurReport/downLoadCheckList?guoku_id=${
            this.$sessionStorage.ls.get('Login_Userinfo').guokuId
          }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
            this.$sessionStorage.ls.get('Login_Userinfo').id
          }&REPORT_ID=${this.model.REPORT_ID}&isCover=0&state=${this.state}`
        )
      } else {
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/qurReport/downLoadCheckList?guoku_id=${
            this.$sessionStorage.ls.get('Login_Userinfo').guokuId
          }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
            this.$sessionStorage.ls.get('Login_Userinfo').id
          }&REPORT_ID=${this.model.REPORT_ID}&isCover=0&state=${this.state}`
        )
      }
    },
  },
}
</script>

<style  scoped>
h1,
h2,
h3,
h4,
h5,
h6 {
  font-weight: bold;
  padding: 8px 0;
  margin: 0;
}

span,
div {
  font-weight: normal;
}

.indent {
  font-size: 16px;
  /* text-indent: 2em !important; */
  word-break: break-all;
  text-align: justify;
  line-height: 30px;
}

table tr td {
  font-weight: normal;
}

td {
  padding: 5px;
}
</style>
