<template>
  <a-modal
    title="月度快报详情"
    :maskClosable="false"
    v-model="visibleModal"
    width="60%"
    v-if="show2"
    @cancel="handleCancle"
  >
    <template v-if="model.REPORT_TYPE_ID === '2'">
      <template v-if="model.HTML_REPORT">
        <div v-html="model.HTML_REPORT" :contenteditable="isEdit" :key="id"></div>
      </template>
      <div style="font-family:SimSun;" v-else :contenteditable="isEdit" id="HTML_REPORTS" :key="id">
        <h1 style="text-align: center;font-family:SimSun;margin:0px;">{{model.YEAR}}年{{model.MONTH}}月重庆市国库运行简况</h1>
        <div class="indent" style="text-indent:2em;line-height:38px">
          {{model.YEAR}}年{{model.MONTH}}月，重庆市
          <sup>1</sup>
          地方级国库收入{{model['000001'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000184'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000002'] | dataTreating }}%；支出{{model['000027'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000185'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000028'] | dataTreating }}%。{{model.MONTH}}月末，全辖国库库存余额为{{model['000172'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000175'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000176'] | dataTreating }}%。{{model.MONTH}}月国库运行主要特点如下：
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">一是税收收入与非税收入齐降。</span>
          {{model.MONTH}}月，公共预算收入{{model['000008'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000186'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000009'] | dataTreating }}%，连续{{model.num_000009}}个月{{ model['000009'] ? model['000009'].includes('增长') ? '正':'负':'/'}}增长，{{model.lib000009 | dataTreating }} 幅创新高。其中税收收入{{model['000052'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000053'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000054'] | dataTreating }}%；非税收入{{model['000058'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000187'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000059'] | dataTreating }}%，税收占公共预算收入的比重为{{model['000056'] | dataTreating }}%，居近{{model.num_000056}}个月最高（低）水平。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">二是土地相关收入均呈负增长。</span>
          受疫情叠加房地产市场遇冷影响，土地出让收入及土地相关税收均大幅减收，{{model.MONTH}}月，土地出让收入{{model['000117'] | dataTreating }} {{model.AMT_UNIT_NAME}}，同比{{model['000188'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000120'] | dataTreating }}%，连续{{model.num_000188}}个{{model['000188'] ? model['000188'].includes('增长') ? '正':'负':'/'}}月增长。土地相关的税收（房产税、城镇土地使用税、土地增值税、耕地占用税、契税）{{model['000189'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000190'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000191'] | dataTreating }}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">三是国库支出小幅下降。</span>
          疫情防控及“三保”资金为重点保障领域，{{model.MONTH}}月，重庆辖内各级国库共办理疫情防治紧急拨款X笔，金额X{{model.AMT_UNIT_NAME}}；社会保障和就业、卫生健康、农林水、教育、城乡社区、住房保障等重点保障领域支出{{model['000138'] | dataTreating }}{{model.AMT_UNIT_NAME}}，占国库支出比重为{{model['000141'] | dataTreating }}%。
        </div>
        <b
          style="padding-top:50px;line-height:38px;font-size:15px;font-bold:400;"
          name="remarks"
        >
          <div style="width:225px;height:0px;border-top:solid 1px #000 "></div>
          <div
            style="font-size:15px;"
          >1.地方级国库收入包括一般公共预算收入、基金预算收入和国有资本经营预算收入，即地方政府自有财力，不包含地方政府债务收入和转移性收入。</div>
          <br />
        </b>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          <!-- <span style="color:#fff">占位</span> -->
          <span style="font-weight:bold;">四是库存余额大幅下降。</span>
          {{model.MONTH}}月末，重庆市全辖国库库存余额为{{model['000172'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000176'] | dataTreating }}%。分级次看，市级库存{{model['000177'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000192'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000178'] | dataTreating }}%，为{{model.num_000178}}年以来历史最高（低）位；区县（含乡镇）级库存{{model['000180'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000193'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000181'] | dataTreating }}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">五是首期地方债成功发行。</span>
          {{model.MONTH}}月，重庆{{model.YEAR}}年首批政府债券{{model['000206'] | dataTreating }}{{model.AMT_UNIT_NAME}}成功发行并入库，其中一般债务{{model['000256'] | dataTreating }}{{model.AMT_UNIT_NAME}}，专项债务{{model['000257'] | dataTreating }}{{model.AMT_UNIT_NAME}}，主要用于疫情防控、基础设施建设等方面，涉及X个项目。 {{model.MONTH}}月地方政府债务收入同比{{model['000207'] | dataTreating }}%，地方债还本付息{{model['000258'] | dataTreating }}{{model.AMT_UNIT_NAME}}，债券余额为{{model['000206'] | dataTreating}}{{model.AMT_UNIT_NAME}}。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">六是到期现金管理存款延期收回。</span>
          受新冠肺炎疫情影响，为维护重庆金融市场平稳有序运行，更好地发挥财政金融对疫情防控工作的支持，{{model.YEAR}}年第X期、第X期现金管理存款延至{{model.MONTH}}月份收回，{{model.YEAR}}年初地方国库现金管理余额X{{model.AMT_UNIT_NAME}}，月末余额X{{model.AMT_UNIT_NAME}}。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          <span style="font-weight:bold">七是储蓄国债暂停发行。</span>
          按财政部和人总行要求，为保护广大投资者生命安全与身体健康，{{model.YEAR}}年{{model.MONTH}}月储蓄国债暂停发行。
        </div>
        <br />
        <div
          style="text-align: center;padding-top:20px;line-height:38px"
        >表1：{{model.YEAR}}年{{model.MONTH}}月重庆市国库收支存概况表</div>
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
              <th rowspan="2" width="28%">项目名称</th>
              <th colspan="3" width="36%">本期执行数</th>
              <th colspan="3" width="36%">本年累计数(期末时点数)</th>
            </tr>
            <tr style="backgroundColor :#C0C0C0">
              <th width="12%">金额</th>
              <th width="12%">同比增速</th>
              <th width="12%">占比</th>
              <th width="12%">金额</th>
              <th width="12%">同比增速</th>
              <th width="12%">占比</th>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-weight:700;font-size:14px;text-align:center;">一、收入合计</td>
              <td style="font-size:14px;text-align:center;">{{model.table1 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table2 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model.table4 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table5 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">--</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-weight:700;font-size:14px;text-align:center;">（一）地方政府财力</td>
              <td style="font-size:14px;text-align:center;">{{model.table21 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table22 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
              <td style="font-size:14px;text-align:center;">{{model.table24 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table25| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-weight:700;font-size:14px;text-align:center;">1.地方政府自有财力</td>
              <td style="font-size:14px;text-align:center;">{{model.table31 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table32 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table33 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table34 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table35 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table36 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">（1）一般公共预算收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table41 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table42 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table43 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table44 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table45 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table46 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">其中：地方级税收收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table51 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table52 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table53 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table54 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table55 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table56 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">（2）基金预算收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table61 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table62 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table63 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table64 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table65 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table66 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">其中：土地出让收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table71 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table72 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table73 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table74 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table75 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table76 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">（3）国有资本经营预算收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table81 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table82 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table83 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table84 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table85 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table86 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-weight:700;font-size:14px;text-align:center;">2.转移性收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table91 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table92 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table93 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table94 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table95 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table96 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">（1）税收返还收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table101 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table102 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table103 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table104 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table105 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table106 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">（2）一般性转移支付收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table111 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table112 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table113 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table114 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table115 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table116 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-weight:700;font-size:14px;text-align:center;">3.地方政府债券收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table121 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table122 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table123 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table124 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table125 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table126 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-weight:700;font-size:14px;text-align:center;">（二）中央收入（扣除税返）</td>
              <td style="font-size:14px;text-align:center;">{{model.table131 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table132 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model.table134| dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table135 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">--</td>
            </tr>
            <tr style="backgroundColor :#CCFFFF">
              <td style="font-size:14px;text-align:center;">其中:中央级税收收入</td>
              <td style="font-size:14px;text-align:center;">{{model.table141 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table142 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model.table144 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table145 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">--</td>
            </tr>
            <tr style="backgroundColor:#FFFF99">
              <td style="font-weight:700;font-size:14px;text-align:center;">二、支出合计（国库口径）</td>
              <td style="font-size:14px;text-align:center;">{{model.table151 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table152 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
              <td style="font-size:14px;text-align:center;">{{model.table154 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table155 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
            </tr>
            <tr style="backgroundColor:#FFFF99">
              <td style="font-size:14px;text-align:center;">按科目：（一）一般公共预算支出</td>
              <td style="font-size:14px;text-align:center;">{{model.table161 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table162 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table163 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table164 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table165 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table166 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor:#FFFF99">
              <td style="font-size:14px;text-align:center;">（二）基金预算支出</td>
              <td style="font-size:14px;text-align:center;">{{model.table171 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table172 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table173 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table174 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table175 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table176 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor:#FFFF99">
              <td style="font-size:14px;text-align:center;">（三）国有资本预算支出</td>
              <td style="font-size:14px;text-align:center;">{{model.table181 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table182 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table183 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table184 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table185 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table186 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor:#FFFF99">
              <td style="font-size:14px;text-align:center;">按级次：（一）市级</td>
              <td style="font-size:14px;text-align:center;">{{model.table191 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table192 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table193 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table194 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table195 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table196 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor:#FFFF99">
              <td style="font-size:14px;text-align:center;">（二）区县级(含乡镇)</td>
              <td style="font-size:14px;text-align:center;">{{model.table201 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table202 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table203 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table204 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table205 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table206 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor:#FF9900">
              <td style="font-weight:700;font-size:14px;text-align:center;">三、库款合计（期末时点数）</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model.table214 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table215 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">100.00</td>
            </tr>
            <tr style="backgroundColor:#FF9900">
              <td style="font-size:14px;text-align:center;">（一）市级</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model.table224 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table225 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table226 | dataTreating}}</td>
            </tr>
            <tr style="backgroundColor:#FF9900">
              <td style="font-size:14px;text-align:center;">（二）区县级（含乡镇）</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">--</td>
              <td style="font-size:14px;text-align:center;">{{model.table234 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table235 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;">{{model.table236 | dataTreating}}</td>
            </tr>
          </table>
          <div style="text-align:left;">
            <div
              style="text-indent:0px;font-size:15px"
            >备注：1.X年初地方国库现金管理余额X{{model.AMT_UNIT_NAME}}，X月未进行现金管理操作，月末余额X{{model.AMT_UNIT_NAME}}。</div>
            <div style="text-indent:3em;font-size:15px">2.表中“中央收入”为扣除税收返还收入后的数据。</div>
            <br />
          </div>
        </div>
        <div
          style="text-align: center;padding-top:20px;line-height:38px"
        >表2：{{model.YEAR}}年{{model.MONTH}}月重庆市分地区一般公共预算及税收收入情况表</div>
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
              <td style="backgroundColor :#FF99CC;text-align:center;"></td>
              <td style="font-weight:700;backgroundColor :#FF99CC;font-size:14px;text-align:center;">全市合计</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table421 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table422 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table423 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table424 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">22</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">荣昌区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table221 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table222 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table223 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table224 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;"></td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">区县合计</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table431 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table432 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table433 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table434 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">23</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">大足区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table231 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table232 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table233 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table234 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;"></td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">重庆市本级</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table441 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table442 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table443 | dataTreating}}</td>
              <td style="backgroundColor :#FF99CC;font-size:14px;text-align:center;">{{tab2.table444 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">24</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">璧山区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table241 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table242 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table243 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table244 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;"></td>
              <td style="font-weight:bold;backgroundColor :#CCFFFF;font-size:14px;text-align:center;">主城都市区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table451 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table452 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table453 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table454 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;"></td>
              <td style="font-weight:bold;backgroundColor :#FF9900;font-size:14px;text-align:center;">渝东北三峡库区城镇群</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table461 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table462 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table463 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table464 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">1</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">渝中区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table1 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table2 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table3 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table4 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">25</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">万州区</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table251 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table252 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table253 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table254 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">2</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">沙坪坝区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table21 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table22 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table23 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table24 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">26</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">梁平区</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table261 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table262 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table263 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table264 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">3</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">江北区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table31 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table32 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table33 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table34 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">27</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">城口县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table271 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table272 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table273 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table274 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">4</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">南岸区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table41 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table42 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table43 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table44 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">28</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">丰都县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table281 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table282 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table283 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table284 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">5</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">北碚区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table51 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table52 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table53 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table54 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">29</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">垫江县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table291 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table292 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table293 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table294 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">6</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">九龙坡区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table61 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table62 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table63 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table64 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">30</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">忠县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table301 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table302 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table303 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table304 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">7</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">大渡口区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table71 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table72 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table73 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table74 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">31</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">开州区</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table311 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table312 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table313 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table314 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">8</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">巴南区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table81 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table82 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table83 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table84 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">32</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">云阳县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table321 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table322 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table323 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table324 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">9</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">渝北区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table91 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table92 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table93 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table94| dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">33</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">奉节县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table331 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table332 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table333 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table334 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">10</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">两江新区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table101 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table102| dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table103 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table104 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">34</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">巫山县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table341 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table342 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table343 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table344 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">11</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">高新区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table111 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table112 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table113 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table114 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">35</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">巫溪县</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table351 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table352 | dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table353| dataTreating}}</td>
              <td style="backgroundColor :#FF9900;font-size:14px;text-align:center;">{{tab2.table354 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">12</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">长寿区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table121 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table122 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table123 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table124 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;"></td>
              <td style="font-weight:bold;backgroundColor :#99CC00;font-size:14px;text-align:center;">渝东南武陵山区城镇群</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table471 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table472 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table473 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table474 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">13</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">涪陵区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table131 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table132 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table133 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table134 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">36</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">黔江区</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table361 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table362 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table363 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table364 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">14</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">合川区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table141 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table142 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table143 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table144 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">37</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">武隆区</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table371 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table372 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table373 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table374 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">15</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">江津区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table151 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table152 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table153 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table154 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">38</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">石柱县</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table381 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table382 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table383 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table384 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">16</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">永川区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table161 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table162 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table163 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table164 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">39</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">彭水县</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table391 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table392 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table393 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table394 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">17</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">南川区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table171 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table172 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table173 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table174 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">40</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">酉阳县</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table401 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table402 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table403 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table404 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">18</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">綦江区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table181 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table182 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table183 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table184 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">41</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">秀山县</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table411 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table412 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table413 | dataTreating}}</td>
              <td style="backgroundColor :#99CC00;font-size:14px;text-align:center;">{{tab2.table414 | dataTreating}}</td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">19</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">万盛经济技术开发区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table191 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table192 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table193 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table194 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">20</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">潼南区</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table201 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table202 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table203 | dataTreating}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table204 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
            <tr>
              <td style="backgroundColor  :#CCFFFF;font-size:14px;text-align:center;">21</td>
              <td style="backgroundColor  :#CCFFFF;font-size:14px;text-align:center;">铜梁区</td>
              <td style="backgroundColor  :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table211 | dataTreating}}</td>
              <td style="backgroundColor  :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table212 | dataTreating}}</td>
              <td style="backgroundColor  :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table213 | dataTreating}}</td>
              <td style="backgroundColor  :#CCFFFF;font-size:14px;text-align:center;">{{tab2.table214 | dataTreating}}</td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
              <td style="font-size:14px;text-align:center;"></td>
            </tr>
          </table>
        </div>
        <br />
        <div
          style="text-align: center;padding-top:20px;line-height:38px"
        >表3：{{model.YEAR}}年{{model.MONTH}}月地方国库收入全国比较表</div>
        <div style="text-align: right;padding-right: 50px;line-height:38px">单位:%</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;font-family:SimSun;"
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
              <td style="font-size:14px;text-align:center;">{{item.PROJECT}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center">{{item.T010101_RATE}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center">{{item.T010101_RANK}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center">{{item['101_TATE']}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center">{{item['101_RANK']}}</td>
              <td style="font-size:14px;text-align:center;">{{item.PROJECT_1}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center">{{item.T010101_RATE_1}}</td>
              <td style="backgroundColor :#CCFFFF;font-size:14px;text-align:center">{{item.T010101_RANK_1}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center">{{item['101_TATE_1']}}</td>
              <td style="backgroundColor :#FFFF00;font-size:14px;text-align:center">{{item['101_RANK_1']}}</td>
            </tr>
          </table>
        </div>
        <!-- 尝试绘制地图 -->
       <!--  <h4 style="text-align:center">图3：X年X季度重庆市地方级国库收入分地区统计图</h4> -->
        <!-- 卡片区域 -->
        <!-- <div class="site-card-wrapper">
          <a-row :gutter="24">
            <a-col :span="6">
              <div style="border:1px solid #eee;border-radius:1em;box-shadow:5px 5px 5px gray;">
                <div
                  style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                >重庆市市级预算收入总额</div>
                <div style="font-size:25px;color:#EF6704;font-weight:600;padding-left:19px;">330.09</div>
              </div>
            </a-col>
            <a-col :span="6">
              <div style="border:1px solid #eee;border-radius:1em;box-shadow:3px 3px 3px gray;">
                <div
                  style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                >两江新区预算收入总额</div>
                <div style="font-size:25px;color:#EF6704;font-weight:600;padding-left:24px;">50.81</div>
              </div>
            </a-col>
            <a-col :span="6">
              <div style="border:1px solid #eee;border-radius:1em;box-shadow:5px 5px 5px gray;">
                <div
                  style="font-size:12px;font-weight:600;text-align:center;padding-top:5px;"
                >高新区预算收入总额</div>
                <div style="font-size:25px;color:#EF6704;font-weight:600;padding-left:32px;">4.01</div>
              </div>
            </a-col>
            <a-col :span="4">
              <div style="font-size:12px;font-weight:600;padding-top:5px;padding-right:5px;">单位:亿元</div>
            </a-col>
          </a-row>
        </div> -->
        <!-- 大地图-->
        <!-- <div style="width:680px;height:620px;position:relative;left:0px;top:-20px;">
          <div
            id="echars1"
            style="width:690px;height:630px;position:absolute;left:-10px;top:-10px;"
          ></div>
        </div> -->
        <!-- 小地图-->
        <!-- <div
          style="width:282px;height:220px;position:relative;left:568px;top:-282px;border:1px solid #eee;border-radius:1em;box-shadow:5px 5px 5px gray;"
        >
          <div
            id="echars2"
            style="width:282px;height:220px;position:absolute;"
          ></div>
        </div> -->
      </div>
    </template>
    <!-- <canvas id="canvas" style="border: 1px solid #ccc;display:none"></canvas>
    <canvas id="canvas1" style="border: 1px solid #ccc;display:none"></canvas> -->
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
import { getMonthlyQuickReport, editEntityReport } from '@/api/intelligenceReport'

export default {
  name: 'monthQuickReport',
  data() {
    return {
      visibleModal: false,
      isEdit: false,
      model: {},
      id: +new Date(), //重新渲染dom控制
      tab2: {},
      tab3: [],
      show2:false,
      state:'1'
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
  methods: {
    edit(record) {
      this.visibleModal = true
      this.show2 = true
      this.model = {};
      Object.assign(this.model, record)
      console.log(this.model, 'mmmmmm')
      if (!this.model.HTML_REPORT) {
        let options = {}
        getMonthlyQuickReport(record).then((res) => {
          if (res.result === 'success') {
            let data = res.rows
            console.log(data)
            // debugger
            // 根据数据情况赋值
            if (data.textList) {
              this.model = Object.assign({}, this.model, data.textList, data.AMT_UNIT_NAME)
            }
            if (data.tableParams) {
              this.model = Object.assign({}, this.model, data.tableParams)
            }
            if (data.tableParams2) {
              this.tab2 = Object.assign({}, data.tableParams2)
            }
            if (data.tableParams3) {
              this.tab3 = Object.assign({}, data.tableParams3)
              console.log(this.tab3, 'tttttttttttt333333333')
            }
            // 尝试画地图
            // 图三的大地图
           /*  echarts.registerMap('CQ1', CQJSON)
            options = {
              visualMap: {
                type: 'piecewise', // 定义为分段型 visualMap
                pieces: [
                  { gt: 0, lte: 3.9, label: '0~3.9亿元', color: '#B7E1FF' },
                  { gt: 3.9, lte: 7, label: '3.9~7亿元', color: '#93DE8F' },
                  { gt: 7, lte: 14, label: '7~14亿元', color: '#FEE97D' },
                  { gt: 14, lte: 36, label: '14~36亿元', color: '#F7B970' },
                ],
                orient: 'horizontal',
                top: '10%',
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
                      else if (params.value < 0) return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
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
                  data: [
                    { name: '城口县', value: 1.81 },
                    { name: '巫溪县', value: 2.04 },
                    { name: '巫山县', value: 2.49 },
                    { name: '奉节县', value: 4.75 },
                    { name: '云阳县', value: 4.71 },
                    { name: '开州区', value: 6.69 },
                    { name: '万州区', value: 17.76 },
                    { name: '梁平区', value: 10.19 },
                    { name: '忠县', value: 9.17 },
                    { name: '石柱县', value: 3.68 },
                    { name: '垫江县', value: 3.91 },
                    { name: '丰都县', value: 5.13 },
                    { name: '长寿区', value: 20.57 },
                    { name: '涪陵区', value: 28.33 },
                    { name: '武隆区', value: 2.56 },
                    { name: '彭水县', value: 3.64 },
                    { name: '黔江区', value: 6.99 },
                    { name: '酉阳县', value: 2.36 },
                    { name: '秀山县', value: 2.69 },
                    { name: '渝北区', value: 13.87 },
                    { name: '江北区', value: 14.22 },
                    { name: '南岸区', value: 10.32 },
                    { name: '渝中区', value: 8.93 },
                    { name: '大渡口区', value: 4.07 },
                    { name: '沙坪坝区', value: 7.4 },
                    { name: '九龙坡区', value: 7.56 },
                    { name: '巴南区', value: 11.39 },
                    { name: '南川区', value: 7.89 },
                    { name: '万盛区', value: 4.1 },
                    { name: '綦江区', value: 4.36 },
                    { name: '合川区', value: 18.71 },
                    { name: '北碚区', value: 4.8 },
                    { name: '璧山区', value: 35.83 },
                    { name: '铜梁区', value: 17.61 },
                    { name: '江津区', value: 25.08 },
                    { name: '永川区', value: 15.45 },
                    { name: '大足区', value: 17.11 },
                    { name: '潼南区', value: 3.83 },
                    { name: '荣昌区', value: 12.89 },
                  ],
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

            let myChart1 = echarts.init(document.getElementById('echars1'))
            myChart1.resize()
            myChart1.setOption(options)
            let src = myChart1.getDataURL()
            console.log(src, '1111111111')
            myChart1.dispose() // echart的销毁
            console.log(myChart1)
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
              document.getElementById('echars1').innerHTML = `<img src="${imgbase64}"/>`
            }) */
            // ================================================================================
      /*       echarts.registerMap('CQPart1', CQPartJSON)
            options = {
              visualMap: {
                show: false, //  不显示图列比例区间
                type: 'piecewise', // 定义为分段型 visualMap
                pieces: [
                  { gt: 0, lte: 3.9, label: '0~3.9亿元', color: '#B7E1FF' },
                  { gt: 3.9, lte: 7, label: '3.9~7亿元', color: '#93DE8F' },
                  { gt: 7, lte: 14, label: '7~14亿元', color: '#FEE97D' },
                  { gt: 14, lte: 36, label: '14~36亿元', color: '#F7B970' },
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
                      if (params.value < 0) return params.name + '\n' + '{a|' + params.value.toFixed(2) + '}'
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
                    // position: ['50%', '50%'],
                    // align:'center',
                    // verticalAlign:'middle'
                  },
                  data: [
                    { name: '江北区', value: 14.22 },
                    { name: '南岸区', value: 10.32 },
                    { name: '渝中区', value: 8.93 },
                    { name: '大渡口区', value: 4.07 },
                    { name: '沙坪坝区', value: 7.4 },
                    { name: '九龙坡区', value: 7.56 },
                  ],
                },
              ],
            }
            let myChart2 = echarts.init(document.getElementById('echars2'))
            myChart2.resize()
            myChart2.setOption(options)
            debugger
            let src1 = myChart2.getDataURL()
            console.log(src, '1111111111')
            //myChart2.dispose() // echart的销毁
            console.log(myChart2)
            let canvass = document.getElementById('canvas1')
            // // // console.log(canvas)
            let contexts = canvass.getContext('2d')
            // console.log(context) // 创建新图片
            let imgs = new Image()
            imgs.src = src1
            // console.log(img.src, '222222')
            let imgbase64s = ''
            imgs.addEventListener('load', function () {
              canvass.width = imgs.width
              canvass.height = imgs.height
              // console.log(canvas)
              contexts.fillStyle = '#fff'
              contexts.fillRect(0, 0, canvass.width, canvass.height)
              contexts.drawImage(imgs, 0, 0, canvass.width, canvass.height)
              imgbase64s = canvass.toDataURL('image/png')
              // console.log(imgbase64)
              document.getElementById('echars2').innerHTML = `<img src="${imgbase64s}"/>`
            }) */
          }
        })
      }
    },
    //取消
    handleCancle() {
      this.isEdit = false
      this.show2 = false
      this.id = +new Date()
    },
    //保存
    async handleOk() {
      // console.log(this.model.HTML_REPORT ? this.model.HTML_REPORT : document.getElementById('HTML_REPORT').innerHTML)
      console.log(this.model.REPORT_ID)
      // debugger
      let params = {
        HTML_REPORT: this.model.HTML_REPORT
          ? this.model.HTML_REPORT
          : document.getElementById('HTML_REPORTS').innerHTML,
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
      this.show2 = false
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
        `${window._CONFIG['domianURL']}/fixedReport/monthReport/downLoadCheckList?guoku_id=${
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
          HTML_REPORT: document.getElementById('HTML_REPORTS').innerHTML,
          REPORT_ID: this.model.REPORT_ID,
        }
        console.log(params, 'pppppppp')
        // debugger
        //  将数据传给后端
        await editEntityReport(params)
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/newsFlash/downLoadCheckList?guoku_id=${
            this.$sessionStorage.ls.get('Login_Userinfo').guokuId
          }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
            this.$sessionStorage.ls.get('Login_Userinfo').id
          }&REPORT_ID=${this.model.REPORT_ID}&isCover=1&state=${this.state}`
        )
      } else {
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/newsFlash/downLoadCheckList?guoku_id=${
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
  word-break: break-all;
  text-align: justify;
  line-height: 30px;
}

.ant-modal-body {
  overflow-y: auto;
}
</style>