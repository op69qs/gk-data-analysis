<template>
  <a-modal
    title="报告详情"
    :maskClosable="false"
    v-model="visibleModal"
    v-if="show4"
    width="65%"
    @cancel="handleCancle"
  >
    <template v-if="model.REPORT_TYPE_ID === '4'">
      <template v-if="model.HTML_REPORT">
        <div
          v-html="model.HTML_REPORT"
          :contenteditable="isEdit"
          :key="id"
          
        ></div>
      </template>
      <div v-else :contenteditable="isEdit" id="HTML_REPORT" :key="id" style="font-family:SimSun;">
      
        <h1 style="text-align: center;font-family:SimSun;margin:0px;">{{model['YEAR']}}年{{model['qtr'] | dataTreating2}}重庆市国库运行简况</h1>
        <div class="indent" style="text-indent:2em;line-height:38px">
          {{model['YEAR']}}年{{model['qtr'] | dataTreating2}}，重庆市
          <sup>1</sup>
          地方级国库收入{{model['000005'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000441'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000006'] | dataTreating }}%；支出{{model['000031'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000453'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000032'] | dataTreating }}%。X月末，全辖国库库存余额为{{model['000172'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000175'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000176'] | dataTreating }}%。{{model['qtr']}}季度国库运行主要特点如下：
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">一是税收收入与非税收入齐降。</span>
          {{model['qtr'] | dataTreating2}}，公共预算收入{{model['000012'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000392'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000013'] | dataTreating }}%，连续{{model['lib_000423']}}个季度{{ model['000423'] ? model['000423'].includes('增长') ? '正':'负':'/'}}增长，{{ model['000423'] ? model['000423'].includes('增长') ? '增':'降':'/'}}幅创新高。其中税收收入{{model['000218'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000458'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000219'] | dataTreating }}%；非税收入{{model['000459'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000460'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000461'] | dataTreating }}%，税收占公共预算收入的比重为{{model['000345'] | dataTreating }}%，居近{{model['lib_000345']}}个季度最{{ model['000345'] ? parseInt(model['000345'])>0? '高':'低':'/'}}水平。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">二是土地相关收入均呈负增长。</span>
          受疫情叠加房地产市场遇冷影响，土地出让收入及土地相关税收均大幅减收，{{model['qtr'] | dataTreating2}}，土地出让收入{{model['000365'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000393'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['00031802'] | dataTreating }}%，连续{{model['lib_00031802']}}个季度{{ model['00031802'] ? model['00031802'].includes('增长') ? '正':'负':'/'}}增长。土地相关的税收（房产税、城镇土地使用税、土地增值税、耕地占用税、契税）{{model['00032001'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000395'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000396'] | dataTreating }}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">三是金融业税收贡献度提升。</span>
          {{model['qtr'] | dataTreating2}}，金融业全口径税收
          <sup>2</sup>
          合计X亿元，同比增长（下降）X%，占总税收收入比重为X%，较去年同期高（低）X个百分点。其中货币金融服务税收X亿元，同比增长（下降）X%；资本市场服务税收X亿元，同比增长（下降）X%；保险业税收X亿元，同比增长（下降）X%；其他金融业税收X亿元，同比增长（下降）X%。
        </div>
        <b style="padding-top:50px;line-height:38px;font-size:15px;font-bold:400;" name="remarks">
          <div style="width:225px;height:0px;border-top:solid 1px #000 "></div>
          <div
            style="font-size:15px;"
          >1.地方级国库收入包括一般公共预算收入、基金预算收入和国有资本经营预算收入，即地方政府自有财力，不包含地方政府债务收入和转移性收入。</div>
        </b>
        <b style="line-height:38px;font-size:15px;font-bold:400;" name="remarks">
          <div style="font-size:15px;">2.快报中金融业税收为全口径数据，即中央级与地方级合计，其余数据口径为地方级。</div>
          <br />
        </b>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">四是国库支出小幅下降。</span>
          疫情防控及“三保”资金为重点保障领域，{{model['qtr'] | dataTreating2}}，重庆辖内各级国库共办理疫情防治紧急拨款X笔，金额X亿元；社会保障和就业、卫生健康、农林水、教育、城乡社区、住房保障等重点保障领域支出{{model['000372'] | dataTreating }}{{model.AMT_UNIT_NAME}}，占国库支出比重为{{model['000376'] | dataTreating }}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">五是库存余额大幅下降。</span>
          X月末，重庆市全辖国库库存余额为{{model['000172'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000176'] | dataTreating }}%。分级次看，市级库存{{model['000177'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000192'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000178'] | dataTreating }}%，为{{model['lib_000178']}}年以来历史最{{ model['000178'] ? model['000178'].includes('增长') ? '高':'低':'/'}}位；区县（含乡镇）级库存{{model['000180'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000337'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000181'] | dataTreating }}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">六是首期地方债成功发行。</span>
          X月，重庆X年首批政府债券X亿元成功发行并入库，其中一般债务{{model['000262'] | dataTreating }}{{model.AMT_UNIT_NAME}}，专项债务{{model['000264'] | dataTreating }}{{model.AMT_UNIT_NAME}}，主要用于疫情防控、基础设施建设等方面，涉及X个项目。X季度地方政府债务收入同比{{model['000209'] | dataTreating }}%，地方债还本付息{{model['000260'] | dataTreating }}{{model.AMT_UNIT_NAME}}，债券余额为X亿元。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">七是到期现金管理存款延期收回。</span>
          受新冠肺炎疫情影响，为维护重庆金融市场平稳有序运行，更好地发挥财政金融对疫情防控工作的支持，X年第X期、第X期现金管理存款延至X月份收回，X年初地方国库现金管理余额X亿元，季末余额X亿元。
        </div>
        <span><div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">八是储蓄国债暂停发行。</span>
          按财政部和人总行要求，为保护广大投资者生命安全与身体健康，X年X季度储蓄国债暂停发行。
        </div>
        </span>
        <!-- <span><br clear=all style='page-break-before:always;'></span> -->
        <br clear=all style='page-break-before:always;mso-break-type:section-break'>
        <div
          style="text-align: center;padding-top:20px;line-height:38px"
        >表1：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市国库收支存概况表</div>
        <div
          style="text-align: right;padding-right: 50px;line-height:38px"
        >单位：{{model.AMT_UNIT_NAME}}/%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;font-family:SimSun;"
          >
            <tr>
              <th rowspan="2" style="backgroundcolor:#C0C0C0">项目名称</th>
              <th colspan="3" style="backgroundcolor:#C0C0C0">本期执行数</th>
              <th colspan="3" style="backgroundcolor:#C0C0C0">本年累计数(期末时点数)</th>
            </tr>
            <tr>
              <th style="backgroundcolor:#C0C0C0">金额</th>
              <th style="backgroundcolor:#C0C0C0">同比增速</th>
              <th style="backgroundcolor:#C0C0C0">占比</th>
              <th style="backgroundcolor:#C0C0C0">金额</th>
              <th style="backgroundcolor:#C0C0C0">同比增速</th>
              <th style="backgroundcolor:#C0C0C0">占比</th>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">一、收入合计</td>
              <td style="font-size:14px;text-align:center;">{{model['000252'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000253'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model['000254'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000255'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">--</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">（一）地方政府财力</td>
              <td style="font-size:14px;text-align:center;">{{model['000228'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000229'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
              <td style="font-size:14px;text-align:center;">{{model['000230'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000231'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">1.地方政府自有财力</td>
              <td style="font-size:14px;text-align:center;">{{model['000001'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000002'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000232'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000005'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000006'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000233'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（1）一般公共预算收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000008'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000009'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000234'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000012'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000013'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000235'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">其中：地方级税收收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000052'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000054'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000236'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000218'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000219'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000237'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（2）基金预算收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000015'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000016'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000238'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000018'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000019'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000239'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">其中：国有土地使用权出让收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000117'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000120'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000240'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000118'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000119'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000241'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（3）国有资本经营预算收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000021'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000022'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000242'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000024'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000025'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000243'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">2.转移性收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000194'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000195'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000244'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000196'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000197'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000245'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（1）税收返还收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000198'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000199'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000246'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000200'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000201'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000247'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（2）一般性转移支付收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000202'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000203'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000248'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000204'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000205'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000249'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">3.地方政府债券收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000206'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000207'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000250'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000208'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000209'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000251'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">（二）中央收入（扣除税返）</td>
              <td style="font-size:14px;text-align:center;">{{model['000210'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000211'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model['000212'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000213'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">--</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">其中:中央级税收收入</td>
              <td style="font-size:14px;text-align:center;">{{model['000214'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000215'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model['000216'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000217'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">--</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">二、支出合计（国库口径）</td>
              <td style="font-size:14px;text-align:center;">{{model['000027'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000028'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
              <td style="font-size:14px;text-align:center;">{{model['000031'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000032'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">按科目：（一）一般公共预算支出</td>
              <td style="font-size:14px;text-align:center;">{{model['000034'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000035'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000036'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000037'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000038'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000039'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（二）基金预算支出</td>
              <td style="font-size:14px;text-align:center;">{{model['000040'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000041'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000042'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000043'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000044'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000045'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（三）国有资本预算支出</td>
              <td style="font-size:14px;text-align:center;">{{model['000046'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000047'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000048'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000049'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000050'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000051'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">按级次：（一）市级</td>
              <td style="font-size:14px;text-align:center;">{{model['000131'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000132'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000220'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000221'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000222'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000223'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（二）区县级(含乡镇)</td>
              <td style="font-size:14px;text-align:center;">{{model['000133'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000134'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000224'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000225'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000226'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000227'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-weight:700;font-size:14px;text-align:center;">三、库款合计（期末时点数）</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model['000172'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000176'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（一）市级</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model['000177'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000178'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000179'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;">（二）区县级（含乡镇）</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model['000180'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000181'] | dataTreating1 }}</td>
              <td style="font-size:14px;text-align:center;">{{model['000182'] | dataTreating }}</td>
            </tr>
          </table>
          <div style="text-align:left;">
            <div
              style="text-indent:0px;font-size:15px;font-family:SimSun;"
            >备注：1.X年初地方国库现金管理余额X{{model.AMT_UNIT_NAME}}，X季度未进行现金管理操作，季末余额X{{model.AMT_UNIT_NAME}}。</div>
            <div style="text-indent:3em;font-size:15px;font-family:SimSun;">2.表中“中央收入”为扣除税收返还收入后的数据。</div>
            <br />
          </div>
        </div>
        <br clear=all style='page-break-before:always;mso-break-type:section-break'>
        <div
          style="page-break-before:always;text-align: center;padding-top:20px;line-height:38px"
        >表2：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市金融业税收统计表</div>
        <div
          style="text-align: right;padding-right: 50px;line-height:38px"
        >单位：{{model.AMT_UNIT_NAME}}/%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
          >
            <tr style="backgroundColor :#C0C0C0">
              <th rowspan="2" width="10%"></th>
              <th colspan="2" width="16%">{{model.YEAR-1}}年{{model.qtr | dataTreating2}}</th>
              <th colspan="3" width="24%">{{model.YEAR}}年{{model.qtr | dataTreating2}}</th>
              <th rowspan="2" width="10%"></th>
              <th colspan="2" width="16%">{{model.YEAR-1}}年{{model.qtr | dataTreating2}}</th>
              <th colspan="3" width="24%">{{model.YEAR}}年{{model.qtr | dataTreating2}}</th>
            </tr>
            <tr style="backgroundColor :#C0C0C0">
              <th width="8%">金额</th>
              <th width="8%">占比</th>
              <th width="8%">金额</th>
              <th width="8%">占比</th>
              <th width="8%">同比增速</th>
              <th width="8%">金额</th>
              <th width="8%">占比</th>
              <th width="8%">金额</th>
              <th width="8%">占比</th>
              <th width="8%">同比增速</th>
            </tr>
            <tr v-for="(item,index) in tabLeft4" :key="index">
              <td style="font-size:14px;text-align:center;">{{item.SERVICE_TYPE}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center">{{item.UP_YEAR}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center">{{item.UP_YEAR_PP}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center">{{item.DOWN_YEAR}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center">{{item.DOWN_YEAR_PP}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center;">{{item.DOWN_YEAR_Y}}</td>
              <td style="font-size:14px;text-align:center;" v-if="tabRight4[index]">{{item.SERVICE_TYPE}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center" v-if="tabRight4[index]">{{tabRight4[index].UP_YEAR}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center" v-if="tabRight4[index]">{{tabRight4[index].UP_YEAR_PP}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center" v-if="tabRight4[index]">{{tabRight4[index].DOWN_YEAR}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center" v-if="tabRight4[index]">{{tabRight4[index].DOWN_YEAR_PP}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center" v-if="tabRight4[index]">{{tabRight4[index].DOWN_YEAR_Y}}</td>
            </tr>
          </table>


         <!--  <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all"
          >
            <tr>
              <th rowspan="2" style="backgroundcolor:#C0C0C0"></th>
              <th colspan="2" style="backgroundcolor:#C0C0C0">X年X季度</th>
              <th colspan="3" style="backgroundcolor:#C0C0C0">X年X季度</th>
              <th rowspan="2" style="backgroundcolor:#C0C0C0"></th>
              <th colspan="2" style="backgroundcolor:#C0C0C0">X年X季度</th>
              <th colspan="3" style="backgroundcolor:#C0C0C0">X年X季度</th>
            </tr>
            <tr>
              <th style="backgroundcolor:#C0C0C0">金额</th>
              <th style="backgroundcolor:#C0C0C0">占比</th>
              <th style="backgroundcolor:#C0C0C0">金额</th>
              <th style="backgroundcolor:#C0C0C0">占比</th>
              <th style="backgroundcolor:#C0C0C0">同比增速</th>
              <th style="backgroundcolor:#C0C0C0">金额</th>
              <th style="backgroundcolor:#C0C0C0">占比</th>
              <th style="backgroundcolor:#C0C0C0">金额</th>
              <th style="backgroundcolor:#C0C0C0">占比</th>
              <th style="backgroundcolor:#C0C0C0">同比增速</th>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-weight:700;backgroundcolor:#CCFFFF">1.货币金融服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;font-weight:700;backgroundcolor:#FF9900">3.保险业</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">①中央银行服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">①人身保险</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">②货币银行服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">②财产保险</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">③非货币银行服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">③再保险</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">融资租赁服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">④商业养老金</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">财务公司</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">⑤保险中介服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">典当</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">⑥保险资产管理</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">汽车金融公司服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">⑦保险监管服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">小额贷款公司服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900">⑧其他</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF9900"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">消费金融公司服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;font-weight:700;backgroundcolor:#99CC00">4.其他金融业</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">网络借贷服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00">①金融信托与管理服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">其他</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00">②控股公司服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">④银行理财服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00">③非金融机构支付服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF">⑤银行监管服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#CCFFFF"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00">④金融信息服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-weight:700;backgroundcolor:#FFFF99">2.资本市场服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00">⑤金融资产管理公司</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">①证券市场服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00">⑥其他</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#99CC00"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">证券市场管理服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;font-weight:700;backgroundcolor:#FF99CC">金融业合计</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF99CC"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF99CC">100.00</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF99CC"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF99CC">100.00</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FF99CC"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">证券经纪交易服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">②公开募集证券投资基金</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">③非公开募集证券投资基金</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">其中：创业投资基金</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">天使投资</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">④期货市场服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">⑤证券期货监管服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">⑥资本投资服务</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99">⑦其他</td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;backgroundcolor:#FFFF99"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
          </table> -->
          <div style="text-align:left;">
            <div style="text-indent:0px;font-size:15px;font-family:SimSun;">备注：本表数据来源于重庆市税务局，为中央和地方级税收合计。</div>
            <br />
          </div>
        </div>
        <div
          style="page-break-before:always;text-align: center;padding-top:20px;line-height:38px"
        >表3：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市分地区一般公共预算及税收收入情况表</div>
        <div
          style="text-align: right;padding-right: 50px;line-height:38px"
        >单位：{{model.AMT_UNIT_NAME}}/%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
          >
            <tr style="backgroundColor :#C0C0C0">
              <th rowspan="2" width="6%">序号</th>
              <th rowspan="2" width="12%">地区</th>
              <th colspan="2" width="16%">一般公共预算收入</th>
              <th colspan="2" width="16%">税收收入</th>
              <th rowspan="2" width="6%">序号</th>
              <th rowspan="2" width="12%">地区</th>
              <th colspan="2" width="16%">一般公共预算收入</th>
              <th colspan="2" width="16%">税收收入</th>
            </tr>
            <tr style="backgroundColor :#C0C0C0">
              <th width="8%">累计金额</th>
              <th width="8%">同比增速</th>
              <th width="8%">累计金额</th>
              <th width="8%">同比增速</th>
              <th width="8%">累计金额</th>
              <th width="8%">同比增速</th>
              <th width="8%">累计金额</th>
              <th width="8%">同比增速</th>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">42</td>
              <td style="font-size:14px;text-align:center;font-weight:700;backgroundColor :#FF99CC">全市合计</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table421 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table422 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table423 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table424 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">22</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">荣昌区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table221 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table222 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table223 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table224 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">43</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">区县合计</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table431 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table432 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table433 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table434 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">23</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">大足区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table231 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table232 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table233 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table234 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">44</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">重庆市本级</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table441 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table442 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table443 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF99CC">{{tab2.table444 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">24</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">璧山区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table241 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table242 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table243 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table244 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">45</td>
              <td style="font-size:14px;text-align:center;font-weight:bold;backgroundColor :#CCFFFF">主城都市区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table451 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table452 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table453 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table454 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900"></td>
              <td style="font-size:14px;text-align:center;font-weight:bold;backgroundColor :#FF9900">渝东北三峡库区城镇群</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table461 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table462 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table463 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table464 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">1</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">渝中区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table1 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table2 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table3 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table4 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">25</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">万州区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table251 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table252 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table253 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table254 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">2</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">沙坪坝区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table21 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table22 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table23 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table24 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">26</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">梁平区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table261 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table262 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table263 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table264 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">3</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">江北区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table31 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table32 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table33 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table34 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">27</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">城口县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table271 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table272 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table273 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table274 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">4</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">南岸区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table41 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table42 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table43 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table44 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">28</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">丰都县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table281 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table282 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table283 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table284 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">5</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">北碚区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table51 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table52 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table53 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table54 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">29</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">垫江县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table291 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table292 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table293 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table294 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">6</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">九龙坡区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table61 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table62 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table63 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table64 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">30</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">忠县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table301 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table302 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table303 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table304 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">7</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">大渡口区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table71 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table72 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table73 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table74 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">31</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">开州区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table311 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table312 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table313 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table314 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">8</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">巴南区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table81 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table82 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table83 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table84 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">32</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">云阳县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table321 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table322 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table323 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table324 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">9</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">渝北区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table91 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table92 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table93 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table94| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">33</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">奉节县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table331 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table332 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table333 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table334 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">10</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">两江新区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table101 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table102| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table103 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table104 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">34</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">巫山县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table341 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table342 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table343 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table344 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">11</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">高新区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table111 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table112 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table113 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table114 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">35</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">巫溪县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table351 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table352 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table353| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FF9900">{{tab2.table354 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">12</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">长寿区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table121 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table122 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table123 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table124 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">47</td>
              <td style="font-size:14px;text-align:center;font-weight:bold;backgroundColor :#99CC00">渝东南武陵山区城镇群</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table471 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table472 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table473 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table474 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">13</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">涪陵区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table131 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table132 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table133 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table134 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">36</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">黔江区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table361 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table362 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table363 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table364 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">14</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">合川区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table141 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table142 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table143 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table144 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">37</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">武隆区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table371 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table372 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table373 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table374 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">15</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">江津区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table151 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table152 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table153 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table154 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">38</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">石柱县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table381 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table382 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table383 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table384 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">16</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">永川区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table161 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table162 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table163 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table164 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">39</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">彭水县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table391 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table392 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table393 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table394 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">17</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">南川区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table171 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table172 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table173 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table174 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">40</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">酉阳县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table401 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table402 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table403 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table404 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">18</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">綦江区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table181 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table182 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table183 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table184 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">41</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">秀山县</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table411 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table412 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table413 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#99CC00">{{tab2.table414 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">19</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">万盛经济技术开发区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table191 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table192 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table193 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table194 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">20</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">潼南区</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table201 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table202 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table203 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{tab2.table204 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">21</td>
              <td style="font-size:14px;text-align:center;backgroundColor  :#CCFFFF">铜梁区</td>
              <td style="font-size:14px;text-align:center;backgroundColor  :#CCFFFF">{{tab2.table211 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor  :#CCFFFF">{{tab2.table212 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor  :#CCFFFF">{{tab2.table213 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor  :#CCFFFF">{{tab2.table214 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
          </table>
        </div>
        <div
          style="page-break-before:always;text-align: center;padding-top:20px;line-height:38px"
        >表4：{{model.YEAR}}年{{model.qtr | dataTreating2}}地方国库收入全国比较表</div>
        <div style="text-align: right;padding-right: 50px;line-height:38px">单位:%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="order-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
          >
            <tr style="backgroundColor :#C0C0C0">
              <th rowspan="2" width="10%">项目</th>
              <th colspan="2" width="20%">一般公共预算收入</th>
              <th colspan="2" width="20%">税收收入</th>
              <th rowspan="2" width="10%">项目</th>
              <th colspan="2" width="20%">一般公共预算收入</th>
              <th colspan="2" width="20%">税收收入</th>
            </tr>
            <tr style="backgroundColor :#C0C0C0">
              <th width="10%">同比增速</th>
              <th width="10%">排位</th>
              <th width="10%">同比增速</th>
              <th width="10%">排位</th>
              <th width="10%">同比增速</th>
              <th width="10%">排位</th>
              <th width="10%">同比增速</th>
              <th width="10%">排位</th>
            </tr>
            <tr v-for="(item,index) in tab3" :key="index">
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{item.bPROJECT}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{item.bT010101_RATE}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{item.bT010101_RANK}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FFFF00">{{item['b101_TATE']}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FFFF00">{{item['b101_RANK']}}</td>
              <td style="font-size:14px;text-align:center;">{{item.aPROJECT_1}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{item.aT010101_RATE_1}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#CCFFFF">{{item.aT010101_RANK_1}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FFFF00">{{item['a101_TATE_1']}}</td>
              <td style="font-size:14px;text-align:center;backgroundColor :#FFFF00">{{item['a101_RANK_1']}}</td>
            </tr>
          </table>
        </div>
        <div style="margin-top:30px;text-align: center;line-height:38px;">
          图1：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市税收与非税收入增长图
        </div>
        <div style="text-align: right;padding-right: 50px;">单位：{{model.AMT_UNIT_NAME}}</div>
        <div id="echars0" style="height:400px;width:100%;margin-top:30px;"></div>
        <div style="margin-top:30px;text-align: center;line-height:38px;">
          图2：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市地方财力结构图
        </div>
        <div id="echars1" style="height:400px;width:100%;margin-top:30px;"></div>
        <!-- <table  border='0' cellpadding='0' cellspacing='0' style="width:100%;margin-top:30px;">
          <tr>
            <td style="width:50%;border:none;">

              <div id="echars0" style="height:400px;width:100%;"></div>
            </td>
            <td style="width:50%;border:none;">
             
              <div id="echars1" style="height:400px;width:100%;"></div>
            </td>
          </tr>
        </table> -->
        <br />
        <!-- 卡片区域 -->
        <!-- <div class="site-card-wrapper">
          <a-row gutter="{16}">
            <a-col span="{8}">
              <a-card title="重庆市市级预算收入总额" bordered="{false}">330.09</a-card>
            </a-col>
            <a-col span="{8}">
              <a-card title="两江新区预算收入总额" bordered="{false}">50.81</a-card>
            </a-col>
            <a-col span="{8}">
              <a-card title="高新区预算收入总额" bordered="{false}">4.01</a-card>
            </a-col>
          </a-row>
          <span>单位:亿元</span>
        </div>-->
        <div
          style="text-align:center;margin:30px 0px;"
        >图3：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市地方级国库收入分地区统计图</div>
        <!-- 卡片区域 -->
        <div class="site-card-wrapper">
          <!--  <div style="display:flex"> -->
          <!-- <table style="width:100%;border-collapse: collapse;border:none;">
            <tr>
              <td>
                <div style="width:98%%; margin-right: 2%;">
                  <div style="border:1px solid #eee;border-radius:1em;box-shadow:5px 5px 5px gray;">
                    <div
                      style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                    >重庆市市级预算收入总额</div>
                    <div
                      style="font-size:25px;color:#EF6704;font-weight:600;padding-left:19px;"
                    >{{model['500000']}}</div>
                  </div>
                </div>
              </td>
              <td>
                <div style="width:98%;margin-right: 2%;">
                  <div style="border:1px solid #eee;border-radius:1em;box-shadow:3px 3px 3px gray;">
                    <div
                      style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                    >两江新区预算收入总额</div>
                    <div
                      style="font-size:25px;color:#EF6704;font-weight:600;padding-left:24px;"
                    >{{model['500999']}}</div>
                  </div>
                </div>
              </td>
              <td>
                <div style="width:98%;margin-right: 2%;">
                  <div style="border:1px solid #eee;border-radius:1em;box-shadow:5px 5px 5px gray;">
                    <div
                      style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                    >高新区预算收入总额</div>
                    <div
                      style="font-size:25px;color:#EF6704;font-weight:600;padding-left:32px;"
                    >{{model['500996']}}</div>
                  </div>
                </div>
              </td>
              <td>
                <div style="width:100%;">
                  <div
                    style="font-size:12px;font-weight:600;padding-top:5px;padding-right:5px;"
                  >单位:{{model.AMT_UNIT_NAME}}</div>
                </div>
              </td>
            </tr>
          </table> -->
          <!-- </div> -->
        </div>
        <div style="position:relative;">

              <!-- 大地图-->
              <div style="width:100%;height:820px;position:relative;left:0px;top:-20px;">
                <div
                  id="echars2"
                  style="width:100%;height:820px;position:absolute;left:-10px;top:35px;"
                ></div>
              </div>

        </div>
        <div
          style="text-align:center;margin:30px 0px;"
        >图4：{{model.YEAR}}年{{model.qtr | dataTreating2}}重庆市地方级国库收入增速分地区统计图</div>
        <!-- 卡片区域 -->
        <!-- <div class="site-card-wrapper">
          <table style="width:100%;border-collapse: collapse;border:none;">
            <tr>
              <td style="width:25%;">
                <div style="width:98%%; margin-right: 2%;">
                  <div style="border:1px solid #eee;border-radius:1em;box-shadow:5px 5px 5px gray;">
                    <div
                      style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                    >重庆市市级预算收入同比增速</div>
                    <div
                      style="font-size:25px;color:#EF6704;font-weight:600;padding-left:19px;"
                    >{{model['500000']}}</div>
                  </div>
                </div>
              </td>
              <td style="width:25%;">
                <div style="width:98%;margin-right: 2%;">
                  <div style="border:1px solid #eee;border-radius:1em;box-shadow:3px 3px 3px gray;">
                    <div
                      style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                    >两江新区预算收入同比增速</div>
                    <div
                      style="font-size:25px;color:#EF6704;font-weight:600;padding-left:24px;"
                    >{{model['500999']}}</div>
                  </div>
                </div>
              </td>
              <td style="width:25%;">
                <div style="width:98%;margin-right: 2%;">
                  <div
                    style="font-size:12px;font-weight:600;padding-top:5px;padding-right:5px;"
                  >单位:{{model.AMT_UNIT_NAME}}</div>
                </div>
              </td>
            </tr>
          </table>
        </div> -->
        <div style="position:relative;">
          <!-- 大地图-->
          <div style="width:100%;height:820px;position:relative;left:0px;top:-20px;">
            <div
              id="echars4"
              style="width:100%;height:820px;position:absolute;left:-10px;top:35px;"
            ></div>
          </div>

        </div>
      </div>
    </template>
    <canvas id="canvas" style="border: 1px solid #ccc;display:none"></canvas>
    <canvas id="canvas1" style="border: 1px solid #ccc;display:none"></canvas>
    <canvas id="canvas2" style="border: 1px solid #ccc;display:none"></canvas>
    <canvas id="canvas3" style="border: 1px solid #ccc;display:none"></canvas>
    <canvas id="canvas4" style="border: 1px solid #ccc;display:none"></canvas>
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
import CQJSON from '@/CQmapJson/CQ.json'
import CQPartJSON from '@/CQmapJson/CQpart.json'
import echarts from 'echarts'
import { getMonthlyReport, editEntityReport, getQuarterQuickReport } from '@/api/intelligenceReport'
export default {
  name: 'quarterlyQuickReport',
  data() {
    return {
      visibleModal: false,
      show4: false,
      isEdit: false,
      tab2: {},
      tab3: [],
      tab4:[],
      tabBig4:[],
      tabLeft4:[],
      tabRight4:[],
      model: {},
      html: '',
      id: +new Date(), //重新渲染dom控制,
      state:'1',
      chart1:'',
      chart2:'',
      chart3:'',
      chart4:''
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
    dataTreating1(value){
      if (!value) return '/'
      if (value.length <= 3) {
        if(value.includes('增长')){
          return value.substr(2)
        }else if(value.includes('下降')){
          return '-'+value.substr(2) 
        }else {
          return value
        }
        //return value
      } else {
        if(value.includes('增长')){
          return value.substr(2).replace(/\|/g, ',')
        }else if(value.includes('下降')){
          return '-'+value.substr(2).replace(/\|/g, ',')
        }else{
          return value.replace(/\|/g, ',')
        }
        //return value.replace(/\|/g, ',')
      }
      /* if(value.includes('增长')){
        return value.substr(2)
      }else if(value.includes('下降')){
        return '-'+value.substr(2)
      } */
    },
    dataTreating2(value){
      if (!value) return '/'
      if(value === '1'){
        return '1季度'
      }else if(value === '2'){
        return '上半年'
      }else if (value === '3'){
        return '1-3季度'
      }else if(value === '4'){
        return '全年'
      }
    }
  },
  methods: {
    edit(record) {
      this.visibleModal = true
      this.show4 = true
      this.model = {}
      Object.assign(this.model, record)
      console.log(this.model, 'mmmmmm')
      if (!this.model.HTML_REPORT) {
        // 改接口
        getQuarterQuickReport(record).then((res) => {
          if (res.result === 'success') {
            let data = res.rows
            console.log(data)
            // debugger
            // 根据数据情况赋值
            if (data.textList) {
              this.model = Object.assign({}, this.model, data.textList, data.tableParams)
            }
            if (data.tableParams) {
              this.tabBig4 = Object.assign([{}], data.tableParams)
              // 表4的数据
              // console.log(this.tabBig4, 'tabBig4')
              this.tabLeft4 = this.tabBig4.slice(0, Math.ceil(this.tabBig4.length / 2))
              // console.log(this.tabLeft4, 'tttttlllll4')
              this.tabRight4 = this.tabBig4.slice(Math.ceil(this.tabBig4.length / 2))
            }
            if (data.tableParams2) {
              this.tab2 = Object.assign({}, data.tableParams2)
            }
            if (data.tableParams3) {
              this.tab3 = Object.assign({}, data.tableParams3)
              console.log(this.tab3, 'tttttttttttt333333333')
            }
            if (data.numAll) {
              this.model = Object.assign({}, this.model, data.numAll)
            }
            if (data.areaAll) {
              this.model = Object.assign({}, this.model, data.areaAll)
            }
            //echarts图 处理
            if (data.echartsData && data.echartsData.length > 0) {
              // console.log('123')
              this.$nextTick((event) => {
                data.echartsData.map((item, index) => {
                  // console.log(item, index)
                  let options = {}
                  //月报模板
                  if (this.model.REPORT_TYPE_ID === '4') {
                    // debugger
                    switch (index) {
                      // 柱状折线图
                      case 0:
                        options = {
                          /* title: {
                            text: '图1：' + this.model.YEAR + '年' + this.model.qtr + '季度重庆市税收与非税收入增长图',
                            textStyle: {
                              fontWeight: 'normal', //可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                              fontFamily: 'san-serif', //主题文字字体，默认微软雅黑
                              fontSize: 14, //主题文字字体大小，默认为18px,
                            },
                            top: 'top',
                            left: 'center',
                            right: 'auto',
                            bottom: 'auto',
                            subtext: '单位：' + this.model.AMT_UNIT_NAME,
                            subtextStyle: {
                              color: '#333',
                              fontWeight: 'normal', //可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                              fontFamily: 'san-serif', //主题文字字体，默认微软雅黑
                              fontSize: 14, //主题文字字体大小，默认为18px,
                              align: 'right',
                            },
                          }, */
                          color:['#5B9BD5','#A2A2A2','#F8883E','#FFDB4B'],
                          legend: {
                            data: ['税收收入', '非税收入', '税收同比增速', '非税同比增速'],
                            left: 'center',
                            top: '12%', //距上边距
                            bottom: '1%', //距离下边距
                            textStyle: {
                              //图例文字的样式
                              color: '#333',
                              fontSize: 12,
                            },
                          },
                          grid: {
                            top: '20%',
                          },
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                            axisLabel: {
                              interval: 0, // 横轴信息全部显示
                              rotate:
                                item.data_dateAll.replace(/\[|]/g, '').split(',') &&
                                item.data_dateAll.replace(/\[|]/g, '').split(',').length > 10
                                  ? 90
                                  : 0, // -45度角倾斜显示
                            },
                          },
                          yAxis: [
                            {
                              type: 'value',
                              // name:'亿元',
                              min: (parseInt(item.MIN_VAL1)>parseInt(item.MIN_VAL2))?item.MIN_VAL2:item.MIN_VAL1,
                              max: (parseInt(item.MAX_VAL1)>parseInt(item.MAX_VAL2))?item.MAX_VAL1:item.MAX_VAL2,
                              // interval: bb1 / 5
                              axisLabel: {
                                formatter: function (value, index) {
                                  return value.toFixed(2)
                                },
                              },
                            },
                            {
                              type: 'value',
                              // name:"%",
                              min: (parseInt(item.MIN_VAL3)>parseInt(item.MIN_VAL4))?item.MIN_VAL4:item.MIN_VAL3,
                              max: (parseInt(item.MAX_VAL3)>parseInt(item.MAX_VAL4))?item.MAX_VAL3:item.MAX_VAL4,
                              // interval: cc1 / 5,
                              axisLabel: {
                                show: true,
                                interval: 'auto',
                                formatter: function (value) {
                                  return value.toFixed(2) + '%'
                                },
                              },
                              splitLine:{
                                show:false,
                              }
                            },
                          ],
                          series: [
                            {
                              name: '税收收入',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barMaxWidth: 30, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#5B9BD5', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#5B9BD5', //改变折线颜色
                                  },
                                },
                              },
                              animation: false,
                            },
                            {
                              name: '非税收入',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barMaxWidth: 30, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#A2A2A2', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#A2A2A2', //改变折线颜色
                                  },
                                },
                              },
                              animation: false,
                            },
                            {
                              name: '税收同比增速',
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              yAxisIndex:1,
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#F8883E', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#F8883E', //改变折线颜色
                                  },
                                },
                              },
                              animation: false,
                            },
                            {
                              name: '非税同比增速',
                              data: item.series4.replace(/\[|]/g, '').split(','),
                              yAxisIndex:1,
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFDB4B', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFDB4B', //改变折线颜色
                                  },
                                },
                              },
                              animation: false,
                            },
                          ],
                        }
                        let myChart = echarts.init(document.getElementById('echars0'))
                        myChart.resize()
                        myChart.setOption(options)
                        let src = myChart.getDataURL()
                        console.log(src, '1111111111')
                        //myChart2.dispose() // echart的销毁
                        console.log(myChart)
                        let canvas = document.getElementById('canvas')
                        // // // console.log(canvas)
                        let context = canvas.getContext('2d')
                        // console.log(context) // 创建新图片
                        let img = new Image()
                        img.src = src
                        // console.log(img.src, '222222')
                        let imgbase64 = ''
                        var oThis = this;
                        img.addEventListener('load', function () {
                          canvas.width = img.width
                          canvas.height = img.height
                          context.fillStyle = '#fff'
                          context.fillRect(0, 0, canvas.width, canvas.height)
                          context.drawImage(img, 0, 0, canvas.width, canvas.height)
                          imgbase64 = canvas.toDataURL('image/png')
                          oThis.chart1 = imgbase64
                          document.getElementById('echars0').innerHTML = `<img src="${imgbase64}"/>`
                        })
                        break
                      // 饼图
                      case 1:
                        options = {
                          /* title: {
                            text: '图2：' + this.model.YEAR + '年' + this.model.qtr + '季度重庆市地方财力结构图',
                            textStyle: {
                              fontWeight: 'normal', //可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                              fontFamily: 'san-serif', //主题文字字体，默认微软雅黑
                              fontSize: 14, //主题文字字体大小，默认为18px,
                            },
                            top: 'top',
                            left: 'center',
                            right: 'auto',
                            bottom: 'auto',
                          }, */
                          //提示框组件,鼠标移动上去显示的提示内容
                          tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b}: {c} ({d}%)', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。
                          },
                          color: [
                          "#5B9BD5",
                          "#ED7D31",
                          "#A5A5A5",
                          "#FFC000",
                          "#4472C4",
                          "#70AD47",
                          "#255E91"
                           ],
                          legend: {
                            orient: 'vertical',
                            y: 'center', //延Y轴居中
                            x: 'right', //居右显示
                            //top: 0,
                            //left: 'center',
                            data: item.series,
                          },
                          series: [
                            {
                              type: 'pie',
                              radius: '60%',
                              data: item.series,
                              animation: false,
                              selectedMode:true,
                              itemStyle:{
                                borderWidth:10,
                                borderColor:'#fff',
                              },
                              //标签
                              label: {
                                normal: {
                                  show: true,
                                  position: 'outside',
                                  formatter: '{b} : {d}%', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比
                                  textStyle: {
                                    align: 'center',
                                    baseline: 'middle',
                                    fontFamily: '微软雅黑',
                                    fontSize: 15,
                                    fontWeight: 'bolder',
                                  },
                                },
                              },
                            },
                          ],
                        }
                        let myChart1 = echarts.init(document.getElementById('echars1'))
                        myChart1.resize()
                        myChart1.setOption(options)
                        let src1 = myChart1.getDataURL()
                        console.log(src, '1111111111')
                        //myChart2.dispose() // echart的销毁
                        console.log(myChart1)
                        let canvas1 = document.getElementById('canvas')
                        // // // console.log(canvas)
                        let context1 = canvas1.getContext('2d')
                        // console.log(context) // 创建新图片
                        let img1 = new Image()
                        img1.src = src1
                        // console.log(img.src, '222222')
                        let imgbase64_1 = ''
                        var oThis = this;
                        img1.addEventListener('load', function () {
                          canvas1.width = img1.width
                          canvas1.height = img1.height
                          // console.log(canvas)
                          context1.fillStyle = '#fff'
                          context1.fillRect(0, 0, canvas1.width, canvas1.height)
                          context1.drawImage(img1, 0, 0, canvas1.width, canvas1.height)
                          imgbase64_1 = canvas1.toDataURL('image/png')
                          oThis.chart2 = imgbase64_1
                          // console.log(imgbase64)
                          document.getElementById('echars1').innerHTML = `<img src="${imgbase64_1}"/>`
                        })
                        break
                      // 图三的大地图
                      case 2:
                        console.log(data.echartsData[index+1].series)
                        echarts.registerMap('CQ1', CQJSON)
                        echarts.registerMap('CQPart1', CQPartJSON)
                        options = {
                          tooltip: {
                            show: true,
                            formatter: function (params) {
                              return params.name + '：' + params.data['value'] + '%'
                            },
                          },
                          title: {
                            left: '88%',
                            top: '7%',
                            text: '单位：'+this.model.AMT_UNIT_NAME,
                            textStyle: { color: '#000', fontWeight: '100', fontSize: 14 },
                          },
                          visualMap: {
                            type: 'piecewise',
                            itemWidth: 23,
                            itemHeight: 13,
                            showLabel: true,
                            pieces: [
                              { gte: parseFloat(this.model['map11']), lte: parseFloat(this.model['map12']), label: this.model['map11']+'~'+this.model['map12']+'亿元', color: '#B7E1FF' },
                              { gt: parseFloat(this.model['map12']), lte: parseFloat(this.model['map13']), label: this.model['map12']+'~'+this.model['map13']+'亿元', color: '#93DE8F' },
                              { gt: parseFloat(this.model['map13']), lte: parseFloat(this.model['map14']), label: this.model['map13']+'~'+this.model['map14']+'亿元', color: '#FEE97D' },
                              { gt: parseFloat(this.model['map14']), lte: parseFloat(this.model['map15']), label: this.model['map14']+'~'+this.model['map15']+'亿元', color: '#F7B970' },
                            ],
                            textGap: 20,
                            itemGap: 13,
                            top: '20%',
                            left: '5%',
                          },
                          graphic: [
                            {
                              type: 'group',
                              left: '5%',
                              top: '4%',
                              children: [
                                {
                                  type: 'rect',
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 220,
                                    height: 80,
                                  },
                                  style: {
                                    fill: '#fff',
                                    stroke: '#E4E4E4',
                                    lineWidth: 0.8,
                                    shadowBlur: 10,
                                    shadowOffsetX: 5,
                                    shadowOffsetY: 5,
                                    shadowColor: '#E4E4E4',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'center',
                                  top: '-18',
                                  style: {
                                    fill: '#000',
                                    text: '重庆市市级预算收入总额',
                                    font: '14px Microsoft YaHei',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'right',
                                  top: '8',
                                  style: {
                                    fill: '#ff6600',
                                    text: this.model['500000'],
                                    font: '17px Microsoft YaHei',
                                  },
                                },
                              ],
                            },
                            {
                              type: 'group',
                              left: '33%',
                              top: '4%',
                              children: [
                                {
                                  type: 'rect',
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 220,
                                    height: 80,
                                  },
                                  style: {
                                    fill: '#fff',
                                    stroke: '#E4E4E4',
                                    lineWidth: 0.8,
                                    shadowBlur: 10,
                                    shadowOffsetX: 5,
                                    shadowOffsetY: 5,
                                    shadowColor: '#E4E4E4',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'center',
                                  top: '-18',
                                  style: {
                                    fill: '#000',
                                    text: '两江新区预算收入总额',
                                    font: '14px Microsoft YaHei',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'right',
                                  top: '8',
                                  style: {
                                    fill: '#ff6600',
                                    text: this.model['500999'],
                                    font: '17px Microsoft YaHei',
                                  },
                                },
                              ],
                            },
                            {
                              type: 'group',
                              left: '61%',
                              top: '4%',
                              children: [
                                {
                                  type: 'rect',
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 220,
                                    height: 80,
                                  },
                                  style: {
                                    fill: '#fff',
                                    stroke: '#E4E4E4',
                                    lineWidth: 0.8,
                                    shadowBlur: 10,
                                    shadowOffsetX: 5,
                                    shadowOffsetY: 5,
                                    shadowColor: '#E4E4E4',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'center',
                                  top: '-18',
                                  style: {
                                    fill: '#000',
                                    text: '高新区预算收入总额',
                                    font: '14px Microsoft YaHei',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'right',
                                  top: '8',
                                  style: {
                                    fill: '#ff6600',
                                    text: this.model['500996'],
                                    font: '17px Microsoft YaHei',
                                  },
                                },
                              ],
                            },
                            {
                              type: 'group',
                              left: '72%',
                              top: '68%',
                              children: [
                                {
                                  type: 'rect',
                                  z: 1,
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 240,
                                    height: 190,
                                  },
                                  style: {
                                    fill: '#fff',
                                    //stroke: '#E4E4E4',
                                    //lineWidth: 0.8,
                                    //shadowBlur: 10,
                                    //shadowOffsetX: 5,
                                    //shadowOffsetY: 5,
                                    //shadowColor: '#E4E4E4',
                                  },
                                },
                              ],
                            },
                          ],
                          xAxis: {
                            show: false,
                          },
                          series: [
                            {
                              type: 'map',
                              zoom: 1.1,
                              left: '3%',
                              top: '22%',
                              aspectScale: 1.0,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQ1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                // formatter: '{b} \n{c}',
                                formatter: function (params) {
                                  if (
                                    params.name == '沙坪坝区' ||
                                    params.name == '大渡口区' ||
                                    params.name == '南岸区' ||
                                    params.name == '渝中区' ||
                                    params.name == '九龙坡区' ||
                                    params.name == '江北区'
                                  )
                                    return params.name
                                  else if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 13,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 13,
                                fontWeight: 500,
                                // position:['50%', '50%'],
                                // align:'center',
                                // verticalAlign:'middle'
                              },

                              data: item.series,/* [
                                { name: '梁平区', value: -31.4 },
                                { name: '沙坪坝区', value: -30.33 },
                                { name: '垫江县', value: -29.14 },
                                { name: '万州区', value: -28.81 },
                                { name: '江北区', value: -27.4 },
                                { name: '九龙坡区', value: -27.0 },
                                { name: '渝北区', value: -21.74 },
                                { name: '大渡口区', value: -19.57 },
                                { name: '涪陵区', value: -14.79 },
                                { name: '江津区', value: -14.73 },
                                { name: '渝中区', value: -11.34 },
                                { name: '永川区', value: -10.93 },
                                { name: '合川区', value: -9.91 },
                                { name: '巫溪县', value: -8.79 },
                                { name: '石柱县', value: -8.75 },
                                { name: '北碚区', value: -7.91 },
                                { name: '荣昌区', value: -6.9 },
                                { name: '黔江区', value: -6.06 },
                                { name: '铜梁区', value: -5.45 },
                                { name: '潼南区', value: -5.0 },
                                { name: '綦江区', value: -4.72 },
                                { name: '忠县', value: -3.39 },
                                { name: '大足区', value: -1.34 },
                                { name: '巫山县', value: -0.93 },
                                { name: '彭水县', value: 0.13 },
                                { name: '巴南区', value: 1.64 },
                                { name: '武隆区', value: 3.58 },
                                { name: '南岸区', value: 3.86 },
                                { name: '长寿区', value: 6.83 },
                                { name: '丰都县', value: 9.77 },
                                { name: '奉节县', value: 12.84 },
                                { name: '璧山区', value: 13.94 },
                                { name: '云阳县', value: 14.89 },
                                { name: '城口县', value: 23.92 },
                                { name: '万盛区', value: 27.5 },
                                { name: '南川区', value: 30.08 },
                                { name: '开州区', value: 36.57 },
                                { name: '酉阳县', value: 36.81 },
                                { name: '秀山县', value: 40.14 },
                              ], */
                              // 自定义名称映射
                              nameMap: {
                                石柱土家族自治县: '石柱县',
                                彭水苗族土家族自治县: '彭水县',
                                酉阳土家族苗族自治县: '酉阳县',
                                秀山土家族苗族自治县: '秀山县',
                                开县: '开州区',
                                武隆县: '武隆区',
                                垫江区: '垫江区',
                                潼南县: '潼南区',
                                璧山县: '璧山区',
                                铜梁县: '铜梁区',
                                大足县: '大足区',
                                荣昌县: '荣昌区',
                                綦江县: '綦江区',
                                梁平县: '梁平区',
                              },
                            },
                            {
                              type: 'map',
                              z: 2,
                              zoom: 0.31,
                              left: '47%',
                              top: '48%',
                              aspectScale: 1,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQPart1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                formatter: function (params) {
                                  if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 12,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 12,
                                fontWeight: 500,
                              },

                              data: data.echartsData[index+1].series/* [
                                { name: '江北区', value: -27.4 },
                                { name: '南岸区', value: 3.86 },
                                { name: '渝中区', value: -11.34 },
                                { name: '大渡口区', value: -19.57 },
                                { name: '沙坪坝区', value: -30.33 },
                                { name: '九龙坡区', value: -27.0 },
                              ], */
                            },
                          ],
                        }
                        /* options = {
                          visualMap: {
                            type: 'piecewise', // 定义为分段型 visualMap
                            pieces: [
                              { gt: parseInt(this.model['map11']), lte: parseInt(this.model['map12']), label: this.model['map11']+'~'+this.model['map12']+'亿元', color: '#B7E1FF' },
                              { gt: parseInt(this.model['map12']), lte: parseInt(this.model['map13']), label: this.model['map12']+'~'+this.model['map13']+'亿元', color: '#93DE8F' },
                              { gt: parseInt(this.model['map13']), lte: parseInt(this.model['map14']), label: this.model['map13']+'~'+this.model['map14']+'亿元', color: '#FEE97D' },
                              { gt: parseInt(this.model['map14']), lte: parseInt(this.model['map15']), label: this.model['map14']+'~'+this.model['map15']+'亿元', color: '#F7B970' },
                            ],
                            orient: 'horizontal',
                            top: '3%',
                          },
                          series: [
                            {
                              type: 'map',
                              zoom: 1.2,
                              aspectScale: 1,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQ1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                // formatter: '{b} \n{c}',
                                formatter: function (params) {
                                  if (
                                    params.name == '沙坪坝区' ||
                                    params.name == '大渡口区' ||
                                    params.name == '南岸区' ||
                                    params.name == '渝中区' ||
                                    params.name == '九龙坡区' ||
                                    params.name == '江北区'
                                  )
                                    return params.name
                                  else if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 11,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 11,
                                fontWeight: 500,
                                // position:['50%', '50%'],
                                // align:'center',
                                // verticalAlign:'middle'
                              },
                              data: item.series,
                              // 自定义名称映射
                              nameMap: {
                                石柱土家族自治县: '石柱县',
                                彭水苗族土家族自治县: '彭水县',
                                酉阳土家族苗族自治县: '酉阳县',
                                秀山土家族苗族自治县: '秀山县',
                                开县: '开州区',
                                武隆县: '武隆区',
                                垫江区: '垫江区',
                                潼南县: '潼南区',
                                璧山县: '璧山区',
                                铜梁县: '铜梁区',
                                大足县: '大足区',
                                荣昌县: '荣昌区',
                                綦江县: '綦江区',
                                梁平县: '梁平区',
                              },
                            },
                          ],
                        }
 */

                        let myChart2 = echarts.init(document.getElementById('echars2'))
                        myChart2.resize()
                        myChart2.setOption(options)
                        let src2 = myChart2.getDataURL()
                        console.log(src2, '1111111111')
                        myChart2.dispose() // echart的销毁
                        console.log(myChart2)
                        let canvas2 = document.getElementById('canvas1')
                        // // // console.log(canvas)
                        let context2 = canvas2.getContext('2d')
                        // console.log(context) // 创建新图片
                        let img2 = new Image()
                        img2.src = src2
                        // console.log(img.src, '222222')
                        let imgbase64_2 = ''
                        var oThis = this
                        img2.addEventListener('load', function () {
                          canvas2.width = img2.width
                          canvas2.height = img2.height
                          // console.log(canvas)
                          context2.fillStyle = '#fff'
                          context2.fillRect(0, 0, canvas2.width, canvas2.height)
                          context2.drawImage(img2, 0, 0, canvas2.width, canvas2.height)
                          imgbase64_2 = canvas2.toDataURL('image/png')
                          oThis.chart3 = imgbase64_2
                          // console.log(imgbase64)
                          document.getElementById('echars2').innerHTML = `<img src="${imgbase64_2}"/>`
                        })
                        break
                      case 3:
                        // 图四的小地图
                        /* echarts.registerMap('CQPart1', CQPartJSON)
                        options = {
                          visualMap: {
                            show: false, //  不显示图列比例区间
                            type: 'piecewise', // 定义为分段型 visualMap
                            pieces: [
                              {
                                gt: parseInt(this.model['map11']),
                                lte: parseInt(this.model['map12']),
                                label: this.model['map11'] + '~' + this.model['map12'] + '亿元',
                                color: '#B7E1FF',
                              },
                              {
                                gt: parseInt(this.model['map12']),
                                lte: parseInt(this.model['map13']),
                                label: this.model['map12'] + '~' + this.model['map13'] + '亿元',
                                color: '#93DE8F',
                              },
                              {
                                gt: parseInt(this.model['map13']),
                                lte: parseInt(this.model['map14']),
                                label: this.model['map13'] + '~' + this.model['map14'] + '亿元',
                                color: '#FEE97D',
                              },
                              {
                                gt: parseInt(this.model['map14']),
                                lte: parseInt(this.model['map15']),
                                label: this.model['map14'] + '~' + this.model['map15'] + '亿元',
                                color: '#F7B970',
                              },
                            ],
                            orient: 'horizontal',
                            top: 'top',
                          },
                          series: [
                            {
                              type: 'map',
                              zoom: 0.75,
                              aspectScale: 1,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQPart1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                // formatter: '{b} \n{c}',
                                formatter: function (params) {
                                  if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 12,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 12,
                                fontWeight: 500,
                              },
                              data:
                                item.series ,
                            },
                          ],
                        }
                        let myChart3 = echarts.init(document.getElementById('echars3'))
                        myChart3.resize()
                        myChart3.setOption(options)
                        let src3 = myChart3.getDataURL()
                        console.log(src3, '1111111111')
                        //myChart2.dispose() // echart的销毁
                        console.log(myChart3)
                        let canvas3 = document.getElementById('canvas2')
                        // // // console.log(canvas)
                        let context3 = canvas3.getContext('2d')
                        // console.log(context) // 创建新图片
                        let img3 = new Image()
                        img3.src = src3
                        // console.log(img.src, '222222')
                        let imgbase64_3 = ''
                        img3.addEventListener('load', function () {
                          canvas3.width = img3.width
                          canvas3.height = img3.height
                          // console.log(canvas)
                          context3.fillStyle = '#fff'
                          context3.fillRect(0, 0, canvas3.width, canvas3.height)
                          context3.drawImage(img3, 0, 0, canvas3.width, canvas3.height)
                          imgbase64_3 = canvas3.toDataURL('image/png')
                          // console.log(imgbase64)
                          document.getElementById('echars3').innerHTML = `<img src="${imgbase64_3}"/>`
                        }) */
                        break
                      case 4:
                        echarts.registerMap('CQ1', CQJSON)
                        echarts.registerMap('CQPart1', CQPartJSON)
                        options = {
                          tooltip: {
                            show: true,
                            formatter: function (params) {
                              return params.name + '：' + params.data['value'] + '%'
                            },
                          },
                          title: {
                            left: '88%',
                            top: '7%',
                            text: '单位：%',
                            textStyle: { color: '#000', fontWeight: '100', fontSize: 14 },
                          },
                          visualMap: {
                            type: 'piecewise',
                            itemWidth: 23,
                            itemHeight: 13,
                            showLabel: true,
                            pieces: [
                              {
                                gte: parseFloat(this.model['map21']),
                                lte: parseFloat(this.model['map22']),
                                label: this.model['map21'] + '~' + this.model['map22'] + '%',
                                color: '#B7E1FF',
                              },
                              {
                                gt: parseFloat(this.model['map22']),
                                lte: parseFloat(this.model['map23']),
                                label: this.model['map22'] + '~' + this.model['map23'] + '%',
                                color: '#93DE8F',
                              },
                              {
                                gt: parseFloat(this.model['map23']),
                                lte: parseFloat(this.model['map24']),
                                label: this.model['map23'] + '~' + this.model['map24'] + '%',
                                color: '#FEE97D',
                              },
                              {
                                gt: parseFloat(this.model['map24']),
                                lte: parseFloat(this.model['map25']),
                                label: this.model['map24'] + '~' + this.model['map25'] + '%',
                                color: '#F7B970',
                              },
                            ],
                            textGap: 20,
                            itemGap: 13,
                            top: '20%',
                            left: '5%',
                          },
                          graphic: [
                            {
                              type: 'group',
                              left: '5%',
                              top: '4%',
                              children: [
                                {
                                  type: 'rect',
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 220,
                                    height: 80,
                                  },
                                  style: {
                                    fill: '#fff',
                                    stroke: '#E4E4E4',
                                    lineWidth: 0.8,
                                    shadowBlur: 10,
                                    shadowOffsetX: 5,
                                    shadowOffsetY: 5,
                                    shadowColor: '#E4E4E4',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'center',
                                  top: '-18',
                                  style: {
                                    fill: '#000',
                                    text: '重庆市市级预算收入同比增速',
                                    font: '14px Microsoft YaHei',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'right',
                                  top: '8',
                                  style: {
                                    fill: '#ff6600',
                                    text: this.model['lib_500000'],
                                    font: '17px Microsoft YaHei',
                                  },
                                },
                              ],
                            },
                            {
                              type: 'group',
                              left: '33%',
                              top: '4%',
                              children: [
                                {
                                  type: 'rect',
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 220,
                                    height: 80,
                                  },
                                  style: {
                                    fill: '#fff',
                                    stroke: '#E4E4E4',
                                    lineWidth: 0.8,
                                    shadowBlur: 10,
                                    shadowOffsetX: 5,
                                    shadowOffsetY: 5,
                                    shadowColor: '#E4E4E4',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'center',
                                  top: '-18',
                                  style: {
                                    fill: '#000',
                                    text: '两江新区预算收入同比增速',
                                    font: '14px Microsoft YaHei',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'right',
                                  top: '8',
                                  style: {
                                    fill: '#ff6600',
                                    text: this.model['lib_500999'],
                                    font: '17px Microsoft YaHei',
                                  },
                                },
                              ],
                            },
                            {
                              type: 'group',
                              left: '61%',
                              top: '4%',
                              children: [
                                {
                                  type: 'rect',
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 220,
                                    height: 80,
                                  },
                                  style: {
                                    fill: '#fff',
                                    stroke: '#E4E4E4',
                                    lineWidth: 0.8,
                                    shadowBlur: 10,
                                    shadowOffsetX: 5,
                                    shadowOffsetY: 5,
                                    shadowColor: '#E4E4E4',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'center',
                                  top: '-18',
                                  style: {
                                    fill: '#000',
                                    text: '高新区预算收入同比增速',
                                    font: '14px Microsoft YaHei',
                                  },
                                },
                                {
                                  type: 'text',
                                  left: 'right',
                                  top: '8',
                                  style: {
                                    fill: '#ff6600',
                                    text: this.model['lib_500996'],
                                    font: '17px Microsoft YaHei',
                                  },
                                },
                              ],
                            },
                            {
                              type: 'group',
                              left: '72%',
                              top: '68%',
                              children: [
                                {
                                  type: 'rect',
                                  z: 1,
                                  left: 'center',
                                  top: 'middle',
                                  shape: {
                                    width: 240,
                                    height: 190,
                                  },
                                  style: {
                                    fill: '#fff',
                                    //stroke: '#E4E4E4',
                                    //lineWidth: 0.8,
                                    //shadowBlur: 10,
                                    //shadowOffsetX: 5,
                                    //shadowOffsetY: 5,
                                    //shadowColor: '#E4E4E4',
                                  },
                                },
                              ],
                            },
                          ],
                          xAxis: {
                            show: false,
                          },
                          series: [
                            {
                              type: 'map',
                              zoom: 1.1,
                              left: '3%',
                              top: '22%',
                              aspectScale: 1.0,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQ1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                // formatter: '{b} \n{c}',
                                formatter: function (params) {
                                  if (
                                    params.name == '沙坪坝区' ||
                                    params.name == '大渡口区' ||
                                    params.name == '南岸区' ||
                                    params.name == '渝中区' ||
                                    params.name == '九龙坡区' ||
                                    params.name == '江北区'
                                  )
                                    return params.name
                                  else if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 13,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 13,
                                fontWeight: 500,
                                // position:['50%', '50%'],
                                // align:'center',
                                // verticalAlign:'middle'
                              },

                              data: item.series,/* [
                                { name: '梁平区', value: -31.4 },
                                { name: '沙坪坝区', value: -30.33 },
                                { name: '垫江县', value: -29.14 },
                                { name: '万州区', value: -28.81 },
                                { name: '江北区', value: -27.4 },
                                { name: '九龙坡区', value: -27.0 },
                                { name: '渝北区', value: -21.74 },
                                { name: '大渡口区', value: -19.57 },
                                { name: '涪陵区', value: -14.79 },
                                { name: '江津区', value: -14.73 },
                                { name: '渝中区', value: -11.34 },
                                { name: '永川区', value: -10.93 },
                                { name: '合川区', value: -9.91 },
                                { name: '巫溪县', value: -8.79 },
                                { name: '石柱县', value: -8.75 },
                                { name: '北碚区', value: -7.91 },
                                { name: '荣昌区', value: -6.9 },
                                { name: '黔江区', value: -6.06 },
                                { name: '铜梁区', value: -5.45 },
                                { name: '潼南区', value: -5.0 },
                                { name: '綦江区', value: -4.72 },
                                { name: '忠县', value: -3.39 },
                                { name: '大足区', value: -1.34 },
                                { name: '巫山县', value: -0.93 },
                                { name: '彭水县', value: 0.13 },
                                { name: '巴南区', value: 1.64 },
                                { name: '武隆区', value: 3.58 },
                                { name: '南岸区', value: 3.86 },
                                { name: '长寿区', value: 6.83 },
                                { name: '丰都县', value: 9.77 },
                                { name: '奉节县', value: 12.84 },
                                { name: '璧山区', value: 13.94 },
                                { name: '云阳县', value: 14.89 },
                                { name: '城口县', value: 23.92 },
                                { name: '万盛区', value: 27.5 },
                                { name: '南川区', value: 30.08 },
                                { name: '开州区', value: 36.57 },
                                { name: '酉阳县', value: 36.81 },
                                { name: '秀山县', value: 40.14 },
                              ], */
                              // 自定义名称映射
                              nameMap: {
                                石柱土家族自治县: '石柱县',
                                彭水苗族土家族自治县: '彭水县',
                                酉阳土家族苗族自治县: '酉阳县',
                                秀山土家族苗族自治县: '秀山县',
                                开县: '开州区',
                                武隆县: '武隆区',
                                垫江区: '垫江区',
                                潼南县: '潼南区',
                                璧山县: '璧山区',
                                铜梁县: '铜梁区',
                                大足县: '大足区',
                                荣昌县: '荣昌区',
                                綦江县: '綦江区',
                                梁平县: '梁平区',
                              },
                            },
                            {
                              type: 'map',
                              z: 2,
                              zoom: 0.31,
                              left: '47%',
                              top: '48%',
                              aspectScale: 1,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQPart1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                formatter: function (params) {
                                  if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 12,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 12,
                                fontWeight: 500,
                              },

                              data: data.echartsData[index+1].series/* [
                                { name: '江北区', value: -27.4 },
                                { name: '南岸区', value: 3.86 },
                                { name: '渝中区', value: -11.34 },
                                { name: '大渡口区', value: -19.57 },
                                { name: '沙坪坝区', value: -30.33 },
                                { name: '九龙坡区', value: -27.0 },
                              ], */
                            },
                          ],
                        }
                        /* options = {
                          visualMap: {
                            type: 'piecewise', // 定义为分段型 visualMap
                            pieces: [
                              {
                                gt: parseInt(this.model['map21']),
                                lte: parseInt(this.model['map22']),
                                label: this.model['map21'] + '~' + this.model['map22'] + '亿元',
                                color: '#B7E1FF',
                              },
                              {
                                gt: parseInt(this.model['map22']),
                                lte: parseInt(this.model['map23']),
                                label: this.model['map22'] + '~' + this.model['map23'] + '亿元',
                                color: '#93DE8F',
                              },
                              {
                                gt: parseInt(this.model['map23']),
                                lte: parseInt(this.model['map24']),
                                label: this.model['map23'] + '~' + this.model['map24'] + '亿元',
                                color: '#FEE97D',
                              },
                              {
                                gt: parseInt(this.model['map24']),
                                lte: parseInt(this.model['map25']),
                                label: this.model['map24'] + '~' + this.model['map25'] + '亿元',
                                color: '#F7B970',
                              },
                            ],
                            orient: 'horizontal',
                            top: '3%',
                          },
                          series: [
                            {
                              type: 'map',
                              zoom: 1.2,
                              aspectScale: 1,
                              itemStyle: {
                                normal: {
                                  areaColor: '#272235',
                                  borderColor: '#fff',
                                  fontWeightL: 700,
                                  borderWidth: 1,
                                },
                              },
                              mapType: 'CQ1', // 自定义扩展图表类型
                              label: {
                                show: true,
                                // formatter: '{b} \n{c}',
                                formatter: function (params) {
                                  if (
                                    params.name == '沙坪坝区' ||
                                    params.name == '大渡口区' ||
                                    params.name == '南岸区' ||
                                    params.name == '渝中区' ||
                                    params.name == '九龙坡区' ||
                                    params.name == '江北区'
                                  )
                                    return params.name
                                  else if (params.value < 0)
                                    return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
                                  else return params.name + '\n' + params.value.toFixed(2)
                                },
                                rich: {
                                  a: {
                                    color: 'red',
                                    fontFamily: 'Microsoft YaHei',
                                    fontSize: 11,
                                    fontWeight: 500,
                                  },
                                },
                                fontSize: 11,
                                fontWeight: 500,
                                // position:['50%', '50%'],
                                // align:'center',
                                // verticalAlign:'middle'
                              },
                              data:
                                item.series ,
                              // 自定义名称映射
                              nameMap: {
                                石柱土家族自治县: '石柱县',
                                彭水苗族土家族自治县: '彭水县',
                                酉阳土家族苗族自治县: '酉阳县',
                                秀山土家族苗族自治县: '秀山县',
                                开县: '开州区',
                                武隆县: '武隆区',
                                垫江区: '垫江区',
                                潼南县: '潼南区',
                                璧山县: '璧山区',
                                铜梁县: '铜梁区',
                                大足县: '大足区',
                                荣昌县: '荣昌区',
                                綦江县: '綦江区',
                                梁平县: '梁平区',
                              },
                            },
                          ],
                        } */

                        let myChart4 = echarts.init(document.getElementById('echars4'))
                        myChart4.resize()
                        myChart4.setOption(options)
                        let src4 = myChart4.getDataURL()
                        console.log(src4, '1111111111')
                        myChart4.dispose() // echart的销毁
                        console.log(myChart4)
                        let canvas4 = document.getElementById('canvas3')
                        // // // console.log(canvas)
                        let context4 = canvas4.getContext('2d')
                        // console.log(context) // 创建新图片
                        let img4 = new Image()
                        img4.src = src4
                        // console.log(img.src, '222222')
                        let imgbase64_4 = ''
                        var oThis = this
                        img4.addEventListener('load', function () {
                          canvas4.width = img4.width
                          canvas4.height = img4.height
                          // console.log(canvas)
                          context4.fillStyle = '#fff'
                          context4.fillRect(0, 0, canvas4.width, canvas4.height)
                          context4.drawImage(img4, 0, 0, canvas4.width, canvas4.height)
                          imgbase64_4 = canvas4.toDataURL('image/png')
                          oThis.chart4 = imgbase64_4
                          // console.log(imgbase64)
                          document.getElementById('echars4').innerHTML = `<img src="${imgbase64_4}"/>`
                        })
                        break
 
                    }
                  }
                })
              })
            }
          }
        })
      }
    },
    //取消
    handleCancle() {
      this.isEdit = false
      this.show4 = false
      this.id = +new Date()
    },
    //保存
    async handleOk() {
      // console.log(this.model.HTML_REPORT ? this.model.HTML_REPORT : document.getElementById('HTML_REPORT').innerHTML)
      console.log(this.model.REPORT_ID)
      // debugger
      let params = {
        HTML_REPORT: this.model.HTML_REPORT ? this.model.HTML_REPORT : document.getElementById('HTML_REPORT').innerHTML,
        REPORT_ID: this.model.REPORT_ID,
      }
      //  修改接口
      const res = await editEntityReport(params)
       if (res.result === 'success') {
         console.log(res)
         this.handleClose()
       }
       this.$message[res.result === 'success' ? 'success' : 'warning']
    },
    handleClose() {
      this.isEdit = false
      this.visibleModal = false
      this.show4 = false
    },
    splitChunks(string) {
     var regex = RegExp(".{1,"+Math.ceil(string.length/3)+"}", 'g');
     return string.match(regex);
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
        `${window._CONFIG['domianURL']}/fixedReport/newsFlashQuarter/downLoadCheckList?guoku_id=${
          this.$sessionStorage.ls.get('Login_Userinfo').guokuId
        }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
          this.$sessionStorage.ls.get('Login_Userinfo').id
        }&REPORT_ID=${this.model.REPORT_ID}&state=${this.state}`
      )
      // 判断是否修改了文档   如果没有这个字段 说明未修改 则先保存这个html文件 然后在执行下载操作
      // debugger
      if (!this.model.HTML_REPORT) {
        // 调用保存文档的方法
        let params = {
          HTML_REPORT: document.getElementById('HTML_REPORT').innerHTML,
          REPORT_ID: this.model.REPORT_ID,
          chart1_1:this.splitChunks(this.chart1)[0],
          chart1_2:this.splitChunks(this.chart1)[1],
          chart1_3:this.splitChunks(this.chart1)[2],
          chart2_1:this.splitChunks(this.chart2)[0],
          chart2_2:this.splitChunks(this.chart2)[1],
          chart2_3:this.splitChunks(this.chart2)[2],
          chart3_1:this.splitChunks(this.chart3)[0],
          chart3_2:this.splitChunks(this.chart3)[1],
          chart3_3:this.splitChunks(this.chart3)[2],
          chart4_1:this.splitChunks(this.chart4)[0],
          chart4_2:this.splitChunks(this.chart4)[1],
          chart4_3:this.splitChunks(this.chart4)[2],
        }
        ;(this.html = document.getElementById('HTML_REPORT').innerHTML), 
        console.log(params)
        // debugger
        //  修改月度快报模板的接口  将数据传给后端
        await editEntityReport(params)
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/newsFlashQuarter/downLoadCheckList?guoku_id=${
            this.$sessionStorage.ls.get('Login_Userinfo').guokuId
          }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
            this.$sessionStorage.ls.get('Login_Userinfo').id
          }&REPORT_ID=${this.model.REPORT_ID}&isCover=1&state=${this.state}`
        )
      } else {
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/newsFlashQuarter/downLoadCheckList?guoku_id=${
            this.$sessionStorage.ls.get('Login_Userinfo').guokuId
          }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
            this.$sessionStorage.ls.get('Login_Userinfo').id
          }&REPORT_ID=${this.model.REPORT_ID}&isCover=1&state=${this.state}`
        )
      }
    },
  },
}
</script>

<style scoped>
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
</style>