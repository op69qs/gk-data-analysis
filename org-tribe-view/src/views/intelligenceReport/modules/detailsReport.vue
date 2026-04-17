<template>
  <a-modal
    title="报告详情"
    :maskClosable="false"
    v-model="visibleModal"
    width="55%"
    @cancel="handleCancle"
    v-if="show1"
  >
    <template v-if="model.REPORT_TYPE_ID === '1'">
      <template v-if="model.HTML_REPORT">
        <div v-html="model.HTML_REPORT" :contenteditable="isEdit" :key="id"></div>
      </template>
      <div v-else :contenteditable="isEdit" id="HTML_REPORT" :key="id">
        <h1
          style="text-align: center;font-family:SimSun;margin:0px;"
        >{{model.YEAR}}年{{model.MONTH}}月重庆市国库资金运行报告</h1>
        <h2 class="indent" style="padding:8px 0;">一、国库收支概况</h2>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          {{model.YEAR | dataTreating}}年{{model.MONTH | dataTreating}}月重庆市地方自有财力{{model['000001'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000002'] |
          dataTreating
          }}%，较去年同期{{model['000003'] | dataTreating }}个百分点。其中，公共预算收入{{model['000008'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000009'] |
          dataTreating }}%，占比{{model['000011'] | dataTreating }}%；基金预算收入{{model['000015'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000016']
          | dataTreating }}%，占比{{model['000017'] | dataTreating }}%。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          国库支出{{model['000027'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000028'] | dataTreating }}%，较去年同期{{model['000029'] |
          dataTreating }}个百分点。其中，公共预算支出{{model['000034'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000035'] | dataTreating
          }}%，占比{{model['000036'] | dataTreating }}%；基金预算支出{{model['000040'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000041'] |
          dataTreating }}%，占比{{model['000042'] | dataTreating }}%（详见表1）。
        </div>
        <div style="text-align: center;line-height:38px;">
          表1 {{model.YEAR | dataTreating }}年{{model.MONTH | dataTreating
          }}月重庆市国库收支情况表
        </div>
        <div style="text-align: center;">
          <table border="1" width="100%" style="word-break:break-all;border-collapse:collapse;">
            <tr>
              <th colspan="2" rowspan="2" width="34%" style="font-size:16px;font-family:SimSun;">项目</th>
              <th colspan="3" width="33%" style="font-size:16px;font-family:SimSun;">当月</th>
              <th colspan="3" width="33%" style="font-size:16px;font-family:SimSun;">累计</th>
            </tr>
            <tr>
              <th width="11%" style="font-size:16px;font-family:SimSun;">
                金额
                <br />
                （{{model.AMT_UNIT_NAME}}）
              </th>
              <th width="11%" style="font-size:16px;font-family:SimSun;">
                同比增长
                <br />（%）
              </th>
              <th width="11%" style="font-size:16px;font-family:SimSun;">
                占 比
                <br />（%）
              </th>
              <th width="11%" style="font-size:16px;font-family:SimSun;">
                金额
                <br />
                （{{model.AMT_UNIT_NAME}}）
              </th>
              <th width="11%" style="font-size:16px;font-family:SimSun;">
                同比增长
                <br />（%）
              </th>
              <th width="11%" style="font-size:16px;font-family:SimSun;">
                占 比
                <br />（%）
              </th>
            </tr>
            <tr>
              <td rowspan="4" width="8%" style="font-size:14px;text-align:center;font-family:SimSun;">收入</td>
              <td width="26%" style="font-size:14px;text-align:center;font-family:SimSun;">公共预算收入</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000008'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000009_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000011'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000012'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000013'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000014'] | dataTreating }}</td>
            </tr>
            <tr>
              <td width="26%" style="font-size:14px;text-align:center;font-family:SimSun;">基金预算收入</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000015'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000016_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000017'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000018'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000019'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000020'] | dataTreating }}</td>
            </tr>
            <tr>
              <td width="26%" style="font-size:14px;text-align:center;font-family:SimSun;">国有资本经营预算收入</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000021'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000022'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000023'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000024'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000025'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000026'] | dataTreating }}</td>
            </tr>
            <tr>
              <td width="26%" style="font-weight: bold;font-size:14px;text-align:center;font-family:SimSun;">
                国库收入合计
                <br />（地方自有财力）
              </td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000001'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000002_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">100</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000005'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000006'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">100</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-family:SimSun;" rowspan="4" width="8%">支出</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;" width="26%">公共预算支出</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000034'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000035_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000036'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000037'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000038'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000039'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-family:SimSun;" width="26%">基金预算支出</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000040'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000041_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000042'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000043'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000044'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000045'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-family:SimSun;" width="26%">国有资本经营预算支出</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000046'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000047'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000048'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000049'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000050'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000051'] | dataTreating }}</td>
            </tr>
            <tr>
              <td style="font-size:14px;text-align:center;font-family:SimSun;font-weight: bold" width="26%">国库支出合计</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000027'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000028_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">100</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000031'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">{{model['000032_lib'] | dataTreating }}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;">100</td>
            </tr>
          </table>
        </div>
        <h2 class="indent" style="padding:8px 0;">二、国库收支主要特点</h2>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          （一）公共预算收入变化特点。{{model.MONTH | dataTreating }}月,公共预算收入{{model['000008'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000009']
          | dataTreating }}%，增速较去年同期{{model['000010'] | dataTreating }}个百分点。分级次看，市级公共预算收入{{model['000433'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000434'] | dataTreating }}%，区县（含乡镇）级公共预算收入{{model['000435'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000436'] | dataTreating }}%，其中有{{model['000435_lib'] | dataTreating }}个区县公共预算收入实现正增长,{{model['435_lib_30'] | dataTreating }}个区县公共预算收入降幅超过30%。分税种看，税收收入{{model['000052'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000054'] | dataTreating }}%，占比{{model['000056'] | dataTreating }}%；非税收入{{model['000058'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000059'] | dataTreating }}%，占比{{model['000061'] | dataTreating
          }}%。税收收入占公共预算收入比重为{{model['000056'] | dataTreating }}%，较去年同期{{model['000057'] | dataTreating }}个百分点（见图1）。
        </div>
        <div id="echars0" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">
          图1：{{model.year_4 | dataTreating }}.01-{{model.YEAR | dataTreating
          }}.{{model.MONTH | dataTreating1 }}公共预算收入增长情况及税收占比情况
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          （二）税收收入变化特点。{{model.MONTH | dataTreating }}月，税收收入{{model['000052'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000053'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000054'] | dataTreating }}%，增速较去年同期{{model['000055'] | dataTreating
          }}个百分点（见图2）。其中，{{model['lib_000437_subject'] | dataTreating }}占比居前三位，分别为{{model['000437'] | dataTreating }}（见表2）。其中，主体税种（增值税、企业所得税、个人所得税）{{model['000062'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000063'] | dataTreating
          }}%，地方税种{{model['000064'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000065'] | dataTreating }}%。分项目看，增值税{{model['000066']
          | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000067'] | dataTreating }}%，企业所得税{{model['000069'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000070'] | dataTreating }}%，个人所得税{{model['000072'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000073'] |
          dataTreating }}%；房产税{{model['000075'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000076'] | dataTreating
          }}%，城镇土地使用税{{model['000078'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000079'] | dataTreating }}%，土地增值税{{model['000081']
          | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000082'] | dataTreating }}%，耕地占用税{{model['000084'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000085'] | dataTreating }}%，契税{{model['000086'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000087'] |
          dataTreating }}%，城市维护建设税{{model['000089'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000090'] | dataTreating
          }}%，资源税{{model['000092'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000093'] | dataTreating }}%，印花税{{model['000094'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000095'] | dataTreating }}%，环保税{{model['000096'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000097'] | dataTreating }}%。 地方税种中，土地相关税收（房产税、城镇土地使用税、土地增值税、耕地占用税、契税）{{model['000394'] | dataTreating }}{{model.AMT_UNIT_NAME}}，{{model['000395'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000396'] | dataTreating }}%。
        </div>
        <div id="echars1" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">
          图2：{{model.year_4 | dataTreating }}.01-{{model.YEAR | dataTreating
          }}.{{model.MONTH | dataTreating1 }}重庆市税收收入增长情况
        </div>
        <div style="text-align: center;line-height:38px;">
          表2 {{model.YEAR | dataTreating }}年{{model.MONTH | dataTreating
          }}月重庆市国库收支情况表
        </div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="order-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;"
          >
            <tr>
              <th width="25%" style="font-size:16px;font-family:SimSun;">科目名称</th>
              <th
                width="25%"
                style="font-size:16px;font-family:SimSun;"
              >收入金额（{{model.AMT_UNIT_NAME}}）</th>
              <th width="25%" style="font-size:16px;font-family:SimSun;">同比增速（%）</th>
              <th width="25%" style="font-size:16px;font-family:SimSun;">占税收收入比重（%）</th>
            </tr>
            <tr v-for="(item,index) in tab2" :key="index">
              <td style="font-size:16px;font-family:SimSun;">{{item.SUBJECT_DSCR}}</td>
              <td style="font-size:16px;font-family:SimSun;">{{item.INDEX_VALUE}}</td>
              <td style="font-size:16px;font-family:SimSun;">{{item.VALUE_YEAR}}</td>
              <td style="font-size:16px;font-family:SimSun;">{{item.AMOUNT_RATIO}}</td>
            </tr>
          </table>
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          （三）非税收入变化特点。{{model.MONTH | dataTreating }}月，非税收入{{model['000058'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000059'] |
          dataTreating }}%，较去年同期{{model['000060'] | dataTreating }}个百分点（见图3）。其中，占比前三位是{{model['lib_000438_subject'] | dataTreating }}，分别为{{model['000438'] | dataTreating }}（见图3）。从项目看，国有资源（资产）有偿使用收入{{model['000098'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000099'] | dataTreating }}%，专项收入{{model['000101'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000102'] | dataTreating }}%，罚没收入{{model['000104'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比
          {{model['000105'] | dataTreating }}%，行政事业性收费收入{{model['000107'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000108'] |
          dataTreating }}%，政府住房基金收入{{model['000110'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000111'] | dataTreating
          }}%，捐赠收入{{model['000113'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000114'] | dataTreating }}%。其他收入{{model['000439'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000440'] | dataTreating }}%。
        </div>
        <div id="echars2" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">
          图3 ：{{model.YEAR | dataTreating }}年{{model.MONTH | dataTreating
          }}月重庆市地方级非税收入占比情况
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          （四）土地出让收入变化特点。{{model.MONTH | dataTreating }}月，重庆市土地出让收入（科目1030148+1030146+1030147+1030718）{{model['000117'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000120'] | dataTreating }}%，占地方自有财力比重为{{model['000121'] | dataTreating
          }}%，较去年同期{{model['000122'] | dataTreating }}个百分点（见图4）。分级次看，市级土地出让收入{{model['000125'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000126'] | dataTreating }}%，占比{{model['000127'] | dataTreating
          }}%；区县（含乡镇）级土地出让收入{{model['000128'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000129'] | dataTreating
          }}%，占比{{model['000130'] | dataTreating }}%。其中{{areaAll.area_1 | dataTreating }}金额位列前三，分别为{{areaAll.value_1 | dataTreating }}。因土地出让收入占基金预算收入比重为{{model['000123'] | dataTreating }}%，带动基金预算收入下降{{model['000016'] |
          dataTreating }}%。
        </div>
        <div id="echars3" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;">
          图4：{{model.year_4 | dataTreating }}.01- {{model.YEAR | dataTreating }}.{{model.MONTH | dataTreating1 }}重庆市土地出让收入增长情况
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px;">
          （五）国库支出变化特点。{{model.MONTH | dataTreating }}月，国库支出{{model['000027'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000028'] |
          dataTreating }}%，增速较去年同期{{model['000029'] | dataTreating }}个百分点。其中，公共预算支出{{model['000034'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000035'] | dataTreating }}%，基金预算支出{{model['000040'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000041'] |
          dataTreating }}%；市级支出{{model['000131'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000132'] | dataTreating
          }}%，区县（含乡镇）级支出{{model['000133'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000134'] | dataTreating
          }}%（见表3）。分项目看：一般公共服务支出{{model['000135'] | dataTreating }}{{model.AMT_UNIT_NAME}}，占比{{model['000137'] | dataTreating
          }}%，同比{{model['000136'] | dataTreating }}%；社会保障和就业、卫生健康、农林水、教育、城乡社区、住房保障等与民生直接相关的领域共计支出{{model['000138'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，
          {{model['000139'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000143'] |
          dataTreating }}%，占国库支出比重为{{model['000141'] | dataTreating }}%，较去年同期{{model['000142'] | dataTreating
          }}个百分点。分金额看，支出金额排名前3位的为{{areaAll.subject_1 | dataTreating }}，金额分别{{areaAll.value_4 | dataTreating
          }}，占比分别为{{areaAll.ratio_1 | dataTreating }}。具体为社会保障和就业支出{{model['000146'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000147'] | dataTreating }}%，卫生健康支出{{model['000149'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000150'] |
          dataTreating }}%，农林水支出{{model['000152'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000153'] | dataTreating
          }}%，教育支出{{model['000154'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000155'] | dataTreating }}%，城乡社区支出{{model['000157'] |
          dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000158'] | dataTreating }}%，住房保障支出{{model['000160'] | dataTreating
          }}{{model.AMT_UNIT_NAME}}，同比{{model['000161'] | dataTreating }}%。
        </div>
        <div style="text-align: center;line-height:38px;">
          表3 {{model.YEAR | dataTreating }}年{{model.MONTH | dataTreating
          }}月重庆市国库支出主要科目情况
        </div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="order-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;"
          >
            <tr>
              <th width="25%" style="font-size:16px;font-family:SimSun;">科目名称</th>
              <th
                width="25%"
                style="font-size:16px;font-family:SimSun;"
              >支出金额（{{model.AMT_UNIT_NAME}}）</th>
              <th width="25%" style="font-size:16px;font-family:SimSun;">增长比率（％）</th>
              <th width="25%" style="font-size:16px;font-family:SimSun;">在国库支出中的占比（％）</th>
            </tr>
            <tr v-for="(item,index) in tab3" :key="index">
              <td style="font-size:16px;font-family:SimSun;">{{item.SUBJECT_DSCR}}</td>
              <td style="font-size:16px;font-family:SimSun;">{{item.INDEX_VALUE}}</td>
              <td style="font-size:16px;font-family:SimSun;">{{item.VALUE_YEAR}}</td>
              <td style="font-size:16px;font-family:SimSun;">{{item.AMOUNT_RATIO}}</td>
            </tr>
          </table>
        </div>
        <h2 class="indent" style="padding:8px 0;">三、国库库存变化特点</h2>
        <div class="indent" style="text-indent:2em;line-height:38px">
          {{model.MONTH | dataTreating }}月，重庆市全辖国库日均库存为{{model['000168'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000169'] |
          dataTreating }}%，库存最高值为{{model['000170'] | dataTreating }}{{model.AMT_UNIT_NAME}}，最低值为{{model['000171'] | dataTreating }}{{model.AMT_UNIT_NAME}}。（见图5）。
        </div>
        <div class="indent" style="text-indent:2em;line-height:38px">
          {{model.MONTH | dataTreating }}月末，重庆市全辖国库库存余额为{{model['000172'] | dataTreating }}{{model.AMT_UNIT_NAME}}，同比{{model['000176'] |
          dataTreating }}%。分级次看，市级库存{{model['000177'] | dataTreating }}{{model.AMT_UNIT_NAME}}、占比{{model['000179'] | dataTreating
          }}%，同比{{model['000178'] | dataTreating }}%；区县（含乡镇）级库存{{model['000180'] | dataTreating }}{{model.AMT_UNIT_NAME}}，占比{{model['000182']
          | dataTreating }}%，同比{{model['000181'] | dataTreating }}%。其中，有{{model.num_1 | dataTreating
          }}个区县库存较去年同期增长，有{{model.num_2 | dataTreating }}个区县库存较去年同期下降，增幅超过50%的区县有{{model.num_3 | dataTreating
          }}个，降幅超过10%的区县有{{model.num_4 | dataTreating }}个；有{{model.num_5 | dataTreating }}个区县库存较上月末增长，有{{model.num_6 |
          dataTreating }}个区县库存较上月末下降，增幅超过50%的区县有{{model.num_7 | dataTreating }}个，降幅超过10%的区县有{{model.num_8 | dataTreating
          }}个。库存居前三位的区县是{{areaAll.stock_area_1 | dataTreating }}，金额分别为{{areaAll.value_7 | dataTreating
          }}，{{areaAll.stock_area_min | dataTreating }}的库存最低，仅为{{areaAll.value_10 | dataTreating }}{{model.AMT_UNIT_NAME}}（见表4）。
        </div>
        <div id="echars4" style="height:300px;width:100%;text-align:center;"></div>
        <div style="text-align: center;line-height:38px;">
          图5: {{model.year_4 | dataTreating }}.01-{{model.YEAR | dataTreating }}.{{model.MONTH | dataTreating1 }}库存走势图
        </div>
        <div style="page-break-before: always;"></div>   <!-- this works! -->
        <div style="text-align: center;line-height:38px;">
          表4 {{model.YEAR | dataTreating }}年{{model.MONTH | dataTreating
          }}月{{model.LAST_DAY | dataTreating }}日重庆市全辖库存情况
        </div>
        <div style="text-align: right;padding-right: 50px;">单位：{{model.AMT_UNIT_NAME}}</div>
        <div style="text-align: center;">
          <table
            border="1"
            width="100%"
            style="border-collapse: collapse;word-break:break-all; word-wrap:break-all;border-collapse: collapse;"
          >
            <tr>
              <th style="font-size:14px;font-family:SimSun;" rowspan="2" width="10%">地区名称</th>
              <th style="font-size:14px;font-family:SimSun;" rowspan="2" width="8%">金额</th>
              <th style="font-size:14px;font-family:SimSun;" colspan="2" width="16%">比去年同期</th>
              <th style="font-size:14px;font-family:SimSun;" colspan="2" width="16%">比上月末</th>
              <th style="font-size:14px;font-family:SimSun;" rowspan="2" width="10%">地区名称</th>
              <th style="font-size:14px;font-family:SimSun;" rowspan="2" width="8%">金额</th>
              <th style="font-size:14px;font-family:SimSun;" colspan="2" width="16%">比去年同期</th>
              <th style="font-size:14px;font-family:SimSun;" colspan="2" width="16%">比上月末</th>
            </tr>
            <tr>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增加额</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增幅</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增加额</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增幅</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增加额</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增幅</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增加额</th>
              <th width="8%" style="font-size:14px;font-family:SimSun;">增幅</th>
            </tr>
            <!-- 按照从大到小排列 -->
            <tr v-for="(item,index) in tabLeft4" :key="index">
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.AREA_DSCR}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE_2}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE_3}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE_4}}</td>
              <td style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;">{{item.INDEX_VALUE_5}}</td>
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
              <td
                style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;"
                v-if="tabRight4[index]"
              >{{tabRight4[index].INDEX_VALUE_4}}</td>
              <td
                style="font-size:14px;text-align:center;font-family:SimSun;padding:0px;"
                v-if="tabRight4[index]"
              >{{tabRight4[index].INDEX_VALUE_5}}</td>
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
import CQJSON from '@/CQmapJson/CQ.json'
import { getMonthlyReport, editEntityReport } from '@/api/intelligenceReport'

export default {
  name: 'detailsReport',
  data() {
    return {
      visibleModal: false,
      isEdit: false,
      model: {},
      id: +new Date(), //重新渲染dom控制
      tabRight4: [{}], //表四右边列的整体数据
      tabLeft4: [{}], //表四左边列的整体数据
      tabBig4: [{}], //表四的整体数据
      tab2: [{}], // 表二的数据
      tab3: [{}], // 表三的数据
      areaAll: {}, //地区数据,
      show1:false,
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
    dataTreating1(value) {
      // console.log(value)
      /* if (!value) return '/'
      if (value.length <= 3) {
        return value
      } else {
        return value.replace(/\|/g, ',')
      } */
      if(value<10){
        return '0'+value
      }
    },
  },
  methods: {
    edit(record) {
      this.visibleModal = true
      this.show1 = true
      this.model = {};
      Object.assign(this.model, record)
      // console.log(this.model, 'mmmmmm')
      if (!this.model.HTML_REPORT) {
        // console.log(this.model, 'mmmmmm')
        getMonthlyReport(record).then((res) => {
          if (res.result === 'success') {
            let data = res.rows
            // console.log(data)
            // debugger
            if (data.textList) {
              this.model = Object.assign({}, this.model, data.textList)
            }
            if (data.tableParams) {
              // console.log(data.tableParams, 'ddddd')
              this.tabBig4 = Object.assign([{}], data.tableParams)
              // 表4的数据
              // console.log(this.tabBig4, 'tabBig4')
              this.tabLeft4 = this.tabBig4.slice(0, Math.ceil(this.tabBig4.length / 2))
              // console.log(this.tabLeft4, 'tttttlllll4')
              this.tabRight4 = this.tabBig4.slice(Math.ceil(this.tabBig4.length / 2))
              // console.log(this.tabRight4, 'tttttrrrr4')
            }
            if (data.tableParams2) {
              this.tab2 = Object.assign([{}], data.tableParams2)
              // console.log(this.tab2, 'tab22222')
            }
            if (data.tableParams3) {
              this.tab3 = Object.assign([{}], data.tableParams3)
              // console.log(this.tab3, 'tab3')
            }
            if (data.numAll) {
              this.model = Object.assign({}, this.model, data.numAll)
            }
            if (data.areaAll) {
              // console.log(data.areaAll, 'aaa')
              this.areaAll = Object.assign({}, data.areaAll)
              // console.log(this.areaAll, 'aaaaaAAAAA')
            }
            //echarts图 处理
            if (data.echartsData && data.echartsData.length > 0) {
              // console.log(data.echartsData, 'dddddddddddeeeeeeeeedddd')
              this.$nextTick((event) => {
                data.echartsData.map((item, index) => {
                  // console.log(item, index)
                  // console.log(index, item.data_dateAll.replace(/\[|]/g, '').split(','))
                  let options = {}
                  //月报模板
                  if (this.model.REPORT_TYPE_ID === '1') {
                    // debugger
                    switch (index) {
                      // 折线图
                      case 0:
                        options = {
                          legend: {
                            data: ['公共预算收入增长情况', '税收占比情况'],
                          },
                          animation: false,
                          xAxis: {
                            name: '账期',
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: {
                            type: 'value',
                            axisLabel: {
                              show: true,
                              interval: 'auto',
                              formatter: '{value} %',
                            },
                          },
                          series: [
                            {
                              name: '公共预算收入增长情况',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#6666ff', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#6666ff', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '税收占比情况',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#ff6600', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#ff6600', //改变折线颜色
                                  },
                                },
                              },
                              animation: false,
                            },
                          ],
                        }
                        break
                      // 柱状折线图
                      case 1:
                        // console.log(item.series2, 's2')
                        // console.log(this.model.AMT_UNIT)
                        let aa = '单月税收金额(' + this.model.AMT_UNIT_NAME + ')'
                        // console.log(aa)
                        options = {
                          legend: {
                            data: ['单月税收同比增幅', aa],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: [
                            {
                              type: 'value',
                              min: item.MIN_VAL,
                              max: item.MAX_VAL,
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
                              name: aa,
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'bar',
                              barWidth: 8, //柱图宽度
                              itemStyle: {
                                normal: {
                                  color: '#5B9BD5', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#5B9BD5 ', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: '单月税收同比增幅',
                              data: item.series2.replace(/\[|]/g, '').split(','),

                              type: 'line',
                              yAxisIndex: 1,
                              itemStyle: {
                                normal: {
                                  color: '#FF6600', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF6600', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 饼图
                      case 2:
                        options = {
                          //提示框组件,鼠标移动上去显示的提示内容
                          color: [
                          "#5B9BD5",
                          "#ED7D31",
                          "#A5A5A5",
                          "#FFC000",
                          "#4472C4",
                          "#70AD47",
                          "#255E91"
                           ],
                          tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b}: {c} ({d}%)', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。
                          },
                          animation: false,
                          legend: {
                            orient: 'vertical',
                            y: 'center', //延Y轴居中
                            x: 'right', //居右显示
                            data: item.series,
                          },
                          series: [
                            {
                              type: 'pie',
                              radius: '60%',
                              data: item.series,
                              //标签
                              label: {
                                normal: {
                                  show: true,
                                  position: 'outside',
                                  formatter: '{c}%', //模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比
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
                        break
                      // 折线图
                      case 3:
                        options = {
                          legend: {
                            data: ['土地出让收入当月增幅', '土地出让收入累计值增幅'],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: {
                            type: 'value',
                            min: item.MIN_VAL,
                            max: item.MAX_VAL,
                            axisLabel: {
                              formatter: function (value, index) {
                                return value.toFixed(2)
                              },
                            },
                          },
                          series: [
                            {
                              name: '土地出让收入当月增幅',
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              //barWidth: 30, //柱图宽度
                              symbol: 'rectangle',
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
                              name: '土地出让收入累计值增幅',
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              symbol: 'triangle',
                              itemStyle: {
                                normal: {
                                  color: '#FFDA46', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFDA46', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                      // 折线图
                      case 4:
                        let DataArr = []
                        DataArr = item.data_dateYear.split(',')
                        // console.log(DataArr, 'dA')
                        options = {
                          legend: {
                            data: DataArr || [],
                          },
                          animation: false,
                          xAxis: {
                            type: 'category',
                            data: item.data_dateAll.replace(/\[|]/g, '').split(','),
                          },
                          yAxis: {
                            type: 'value',
                          },
                          series: [
                            {
                              name: item.data_dateYear1,
                              data: item.series1.replace(/\[|]/g, '').split(','),
                              type: 'line',

                              itemStyle: {
                                normal: {
                                  color: '#3A6BFF', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#3A6BFF', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: item.data_dateYear2,
                              data: item.series2.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FF6B09', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FF6B09', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: item.data_dateYear3,
                              data: item.series3.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#999999', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#999999', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: item.data_dateYear4,
                              data: item.series4.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#FFCD09', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#FFCD09', //改变折线颜色
                                  },
                                },
                              },
                            },
                            {
                              name: item.data_dateYear5,
                              data: item.series5.replace(/\[|]/g, '').split(','),
                              type: 'line',
                              itemStyle: {
                                normal: {
                                  color: '#46AAC5', //改变折线点的颜色
                                  lineStyle: {
                                    color: '#46AAC5', //改变折线颜色
                                  },
                                },
                              },
                            },
                          ],
                        }
                        break
                    }
                    let myChart1 = echarts.init(document.getElementById('echars' + index))
                    myChart1.setOption(options)
                    // console.log(myChart1)
                    let src = myChart1.getDataURL()
                    // console.log(src, '1111111111')
                    myChart1.dispose() // echart的销毁
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
            }
          }
        })
      }
    },
    //取消
    handleCancle() {
      this.isEdit = false
      this.show1 = false
      this.id = +new Date()
    },
    //保存
    async handleOk() {
      // console.log(this.model.HTML_REPORT ? this.model.HTML_REPORT : document.getElementById('HTML_REPORT').innerHTML)
      // console.log(this.model.REPORT_ID)
      // debugger
      let params = {
        HTML_REPORT: this.model.HTML_REPORT ? this.model.HTML_REPORT : document.getElementById('HTML_REPORT').innerHTML,
        REPORT_ID: this.model.REPORT_ID,
      }
      const res = await editEntityReport(params)
      if (res.result === 'success') {
        // console.log(res)
        this.handleClose()
      }
      this.$message[res.result === 'success' ? 'success' : 'warning']
    },
    handleClose() {
      this.isEdit = false
      this.visibleModal = false
      this.show1 = false
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
          HTML_REPORT: document.getElementById('HTML_REPORT').innerHTML,
          REPORT_ID: this.model.REPORT_ID,
        }
        console.log(params)
        // debugger
        await editEntityReport(params)
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/monthReport/downLoadCheckList?guoku_id=${
            this.$sessionStorage.ls.get('Login_Userinfo').guokuId
          }&X-Access-Token=${JSON.parse(localStorage['pro__Access-Token']).value}&ADD_USERID=${
            this.$sessionStorage.ls.get('Login_Userinfo').id
          }&REPORT_ID=${this.model.REPORT_ID}&isCover=0&state=${this.state}`
        )
      } else {
        // debugger
        window.open(
          `${window._CONFIG['domianURL']}/fixedReport/monthReport/downLoadCheckList?guoku_id=${
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
}

/* table tr td {
  font-weight: normal;
  font-size: 10px;
} */

td {
  padding: 5px;
}
</style>