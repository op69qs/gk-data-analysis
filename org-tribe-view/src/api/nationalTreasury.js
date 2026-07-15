import {getAction, deleteAction, putAction, postAction, formPostAction} from '@/api/manage'
/*行业树*/
const getIndustryTree = params => postAction('/industryDim/getIndustryTree', params)
/*条款*/
const addQuestionRule = params => postAction('/inspection/questionRuleController/addQuestionRule', params)
const editQuestionRule = params => postAction('/inspection/questionRuleController/editQuestionRule', params)
//制度依据走审批
const addQuestionRule1 = params => postAction('/inspection/questionRuleTempController/addQuestionRule', params)
// 制度依据修改（退回/待提交）
const editQuestionRuleTemp = params => postAction('/inspection/questionRuleTempController/editQuestionRule', params)
/*问题数*/
const getQuestionBankData = params => postAction('/inspection/questionBankController/getQuestionBankData', params)
/*问题库查询接口*/
const getQuestionBankTreeNew = params => postAction('/inspection/questionBankController/getQuestionBankTreeNew', params)
const addQuestionBank = params => postAction('/inspection/questionBankController/addQuestionBank', params)
const editQuestionBank = params => postAction('/inspection/questionBankController/editQuestionBank', params)
// 问题库修改（退回/待提交）
const editQuestionBankTemp = params => postAction('/inspection/questionBankTempController/editQuestionBank', params)
//问题库新增弹框新增修改接口
const questionBankddQuestionBank = params =>
    postAction('/inspection/questionBankTempController/addQuestionBank', params)
const saveQuestionRuleRelation = params =>
    postAction('/inspection/questionBankController/saveQuestionRuleRelation', params)
const getQuestionRuleByRelation = params =>
    postAction('/inspection/questionRuleController/getQuestionRuleByRelation', params)
const delQuestionRuleRelation = params =>
    postAction('/inspection/questionBankController/delQuestionRuleRelation', params)
/*国库树*/
const getGuoKuTree = params => postAction('/talentpool/talentPoolController/getGuoKuTreeTrans', params)

const getGuoKuIdTreeTrans = params => postAction('/talentpool/talentPoolController/getGuoKuIdTreeTrans', params)

// 区县级国库库存月度监测表的国库接口/miller
const getGuokuInfo = params => postAction('/reportcenter/generalCondition/getGuokuInfo', params)

// 重点关注国库的添加
const addIg = params => postAction('/reportcenter/importantGuokuController/addIg', params)
// 重点关注国库的查询
const getImportantGuoku = params => postAction('/reportcenter/importantGuokuController/getData', params)

/*新增人才库*/
const addTalentPool = params => postAction('/talentpool/talentPoolController/addTalentPool', params)

/*修改人才库*/
const editTalentPool = params => postAction('/talentpool/talentPoolController/editTalentPool', params)

/*获取用户信息*/
const getUserInfo = params => getAction('/sys/user/list', params)

/*枚举*/
const getEnumType = params => postAction('/EnumController/getEnumType', params)
const getEnumTypeTree = params => postAction('/EnumController/getTree', params)
const getEnumData = params => postAction('/EnumController/getData', params)
const addEnum = params => postAction('/EnumController/addEnum', params)
const editEnum = params => postAction('/EnumController/editEnum', params)
const delEnum = params => postAction('/EnumController/delEnum', params)

/*根据枚举类型获取枚举数据*/
const getEnumTypeAll = params => postAction('/EnumController/getEnumTypeAll', {ENUM_TYPE_ID: params})
const getEnumTypeAllParams = params => postAction('/EnumController/getEnumTypeAll', params)

/*检测值*/
const getDetectionType = params => postAction('/DetectionController/getDetectionType', params)
const getDetectionData = params => postAction('/DetectionController/getData', params)
const addDetection = params => postAction('/DetectionController/addDetection', params)
const editDetection = params => postAction('/DetectionController/editDetection', params)
const delDetection = params => postAction('/DetectionController/delDetection', params)

/*根据检测类型获取检测值接口*/
const getDetectionTypeAll = params => postAction('/EnumController/getDetectionTypeAll', {DETECTION_TYPE_ID: params})

/*核算主体*/
// const getBookOrgList = (params) => postAction("/GuokuController/getBookOrgList", params);
const addBookOrg = params => postAction('/GuokuController/addBookOrg', params)
const editBookOrg = params => postAction('/GuokuController/editBookOrg', params)
const getOrgTree = params => postAction('/GuokuController/getOrgTree', params)

/*国库*/
const getGuokuTree = params => postAction('/GuokuController/getGuokuTree', params)
/*获取地区tree*/
const getAreaTree = params => postAction('/GuokuController/getAreaTree', params)
const addGuoku = params => postAction('/GuokuController/addGuoku', params)
const editGuoku = params => postAction('/GuokuController/editGuoku', params)
/*地区*/
const addArea = params => postAction('/areaController/addArea', params)
const editArea = params => postAction('/areaController/editArea', params)
const getArea = params => postAction('/areaController/getArea', params)
//征收机关
const levyingBodiesAdd = params => postAction('/levyingBodies/add', params)
const levyingBodiesEdit = params => postAction('/levyingBodies/edit', params)
//==================================预算科目
const subjectImportAdd = params => postAction('/subjectImport/add', params)
const subjectImportEdit = params => postAction('/subjectImport/edit', params)
//步骤一导入
const subjectImportReadExcel = params => postAction('/subjectImport/readExcel', params)
//步骤二导入
const subjectImportReadExcelStat = params => postAction('/subjectImport/readExcelStat', params)
//步骤三导入
const subjectImportReadExcelT = params => postAction('/subjectImport/readExcelT', params)
/*根据核算主体id获取国库信息*/
const getGKbyBook = params => postAction('/inspection/inspectionTaskController/getGKbyBook', params)
/*根据国库id获取核算主体*/
const getBookbyGuokuId = params => postAction('/inspection/inspectionTaskController/getBookbyGuokuId', params)
/*根据国库id获取管辖国库*/
const getPGuoKu = params => postAction('/talentpool/talentPoolController/getPGuoKu', params)
//获取检查组
const getInspectionPlanInspected = params => postAction('/inspection/inspection/getInspectionPlanInspected', params)
//获取任务list
//新增检查任务
const addInspectionTask = params => postAction('/inspection/inspectionTaskController/addInspectionTask', params)
//修改检查任务
const editInspectionTask = params => postAction('/inspection/inspectionTaskController/editInspectionTask', params)
//通过任务id获取任务详情
const getInspectionTaskData = params => postAction('/inspection/inspectionTaskController/getInspectionTaskData', params)
//通过任务id获取任务详情 事后监督
const getInspectionTaskDataSV = params =>
    postAction('/inspection/inspectionPostSVTaskController/getInspectionTaskData', params)
//新增检查人员
const saveInspectUser = params => postAction('/inspection/inspection/saveInspectUser', params)
//检查人员详情
const getTalentPool = params => postAction('/inspection/inspection/getTalentPool', params)
//修改检查人员职务接口
const editUserDuties = params => postAction('/inspection/inspection/editUserDuties', params)
//检查任务里的立即提醒
const addImmediatelyReminder = params => postAction('/inspection/inspectionReminder/addImmediatelyReminder', params)
//检查任务里的定时提醒
const addTimerReminder = params => postAction('/inspection/inspectionReminder/addTimerReminder', params)
//检查计划里的立即提醒
const addPlanImmediatelyReminder = params =>
    postAction('/inspection/inspectionReminder/addPlanImmediatelyReminder', params)
//检查计划里的定时提醒
const addPlanTimerReminder = params => postAction('/inspection/inspectionReminder/addPlanTimerReminder', params)
//获取任务流程主流程
const getInspectionProcData = params => postAction('/inspection/inspectionProcController/getInspectionProcData', params)
//获取流程子流程
const getInspectionProcSubData = params =>
    postAction('/inspection/inspectionProcController/getInspectionProcSubData', params)
//新增计划
const saveInspect = params => postAction('/inspection/inspection/saveInspect', params)
//新增计划
const saveInspectPlan = params => postAction('/inspection/inspection/saveInspectPlan', params)
//修改计划
const editInspect = params => postAction('/inspection/inspection/editInspect', params)
//生成任务
const buildInspectTaskList = params => postAction('/inspection/inspection/buildInspectTaskList', params)
//获取组员信息
const getInspectionUserData = params => postAction('/inspection/inspectionUserController/getInspectionUserData', params)
//根据检查任务ID获取该台账添加人
const getLedgerAddUserByTaskId = params =>
    postAction('/inspection/inspectionQuestionLedger/getLedgerAddUserByTaskId', params)
//事后监督据检查任务ID获取该台账添加人
const getLedgerAddUserByTaskId1 = params =>
    postAction('/inspection/inspectionPostSVLedger/getLedgerAddUserByTaskId', params)
//根据用户ID任务ID获取问题台账
const getQuestionLedgerByUserIdTaskID = params =>
    postAction('/inspection/inspectionQuestionLedger/getQuestionLedgerByUserIdTaskID', params)
//事后监督根据用户ID任务ID获取问题台账
const getQuestionLedgerByUserIdTaskID1 = params =>
    postAction('/inspection/inspectionPostSVLedger/getQuestionLedgerByUserIdTaskID', params)
//获取问题分类树
const getQuestionBankTree = params => postAction('/inspection/questionBankController/getQuestionBankTree', params)
//获取问题分类页
const getQuestionBankPage = params => postAction('/inspection/questionBankController/getQuestionBankPage', params)
//问题台账新增
const addQuestionLedger = params => postAction('/inspection/inspectionQuestionLedger/addQuestionLedger', params)
//问题台账新增
const addQuestionLedgerOneToMany = params =>
    postAction('/inspection/inspectionQuestionLedger/addQuestionLedgerOneToMany', params)
//事后监督问题台账新增
const addQuestionLedger1 = params => postAction('/inspection/inspectionPostSVLedger/addQuestionLedger', params)
//事后监督问题台账新增
const addQuestionLedgerOneToMany1 = params =>
    postAction('/inspection/inspectionPostSVLedger/addQuestionLedgerOneToMany', params)
//问题台账编辑
const editQuestionLedgerByLedgerID = params =>
    postAction('/inspection/inspectionQuestionLedger/editQuestionLedgerByLedgerID', params)
//问题台账编辑
const editQuestionLedgerOneToMany = params =>
    postAction('/inspection/inspectionQuestionLedger/editQuestionLedgerOneToMany', params)
//事后监督问题台账编辑
const editQuestionLedgerByLedgerID1 = params =>
    postAction('/inspection/inspectionPostSVLedger/editQuestionLedgerByLedgerID', params)
//事后监督问题台账编辑
const editQuestionLedgerOneToMany1 = params =>
    postAction('/inspection/inspectionPostSVLedger/editQuestionLedgerOneToMany', params)
//删除问题台账
const delQuestionLedgerByLedgerId = params =>
    postAction('/inspection/inspectionQuestionLedger/delQuestionLedgerByLedgerId', params)
//获取金融机构3级机构
const getFinanceOrgLv3 = params => postAction('/inspection/inspectionQuestionLedger/getFinanceOrgLv3', params)
//获取金融机构4级机构
const getFinanceOrgLv4 = params => postAction('/inspection/inspectionQuestionLedger/getFinanceOrgLv4', params)
//删除问题台账
const delQuestionLedgerOneToMany = params =>
    postAction('/inspection/inspectionQuestionLedger/delQuestionLedgerOneToMany', params)
//事后监督删除问题台账
const delQuestionLedgerByLedgerId1 = params =>
    postAction('/inspection/inspectionPostSVLedger/delQuestionLedgerByLedgerId', params)
//事后监督删除问题台账
const delQuestionLedgerOneToMany1 = params =>
    postAction('/inspection/inspectionPostSVLedger/delQuestionLedgerOneToMany', params)
//根据当前任务ID获取核算主体包含的国库
const getCurTreCodeByTaskId = params =>
    postAction('/inspection/inspectionCheckAccountSheet/getCurTreCodeByTaskId', params)
//获取征收机构信息
const getTaxOrgInfo = params => postAction('/inspection/inspectionCheckAccountSheet/getTaxOrgInfo', params)
//新的获取征收机构信息
const getCheckTaxOrgInfo = params => postAction('/inspection/inspectionCheckAccountSheet/getCheckTaxOrgInfo', params)
//新增对账登记表
const addCheckAccountSheet = params =>
    postAction('/inspection/inspectionCheckAccountSheet/addCheckAccountSheet', params)
//获取对账内容
const getCheckContent = params => postAction('/inspection/inspectionCheckAccountSheet/getCheckContent', params)
//根据任务ID获取对账登记表信息
const getCheckAccInfoByTaskId = params =>
    postAction('/inspection/inspectionCheckAccountSheet/getCheckAccInfoByTaskId', params)
//问题台账问题分类树
const getQuestionBankTreeForQuestionLedger = params =>
    postAction('/inspection/inspectionQuestionLedger/getQuestionBankTreeForQuestionLedger', params)
//事后监督问题台账问题分类树
const getQuestionBankTreeForQuestionLedger1 = params =>
    postAction('/inspection/inspectionPostSVLedger/getQuestionBankTreeForQuestionLedger', params)
//编辑对账登记表
const editCheckAccountSheet = params =>
    postAction('/inspection/inspectionCheckAccountSheet/editCheckAccountSheet', params)
//删除对账登记表
const delCheckAccountSheet = params =>
    postAction('/inspection/inspectionCheckAccountSheet/delCheckAccountSheet', params)
//根据对账登记表ID获取详细信息
const getCheckAccSubInfoBySheetId = params =>
    postAction('/inspection/inspectionCheckAccountSheet/getCheckAccSubInfoBySheetId', params)
//通过任务id  获取人员相关信息
const getUserData = params => postAction('/inspection/inspectionUserController/getUserData', params)
//检查人员表激活子流程
const submitGroupInfo = params => postAction('/inspection/inspectionGroupController/submitGroupInfo', params)
//新增通知书
const addInspectionNotice = params => postAction('/inspection/inspectionNoticeController/addInspectionNotice', params)
//删除通知书
const delInspectionNotice = params => postAction('/inspection/inspectionNoticeController/delInspectionNotice', params)
//修改通知书
const editInspectionNotice = params => postAction('/inspection/inspectionNoticeController/editInspectionNotice', params)
//获取通知书
const getInspectionNoticeData = params =>
    postAction('/inspection/inspectionNoticeController/getInspectionNoticeData', params)
//上传附件（监督检查流程）
const fileUploadfiles = window._CONFIG['domianURL'] + '/inspection/inspectionFileController/fileUpload'
//获取全部附件
const getFiles = params => postAction('/inspection/inspectionFileController/getFiles', params)
//删除附件
const delFile = params => postAction('/inspection/inspectionFileController/delFile', params)
//获取结构化台账信息
const getStructuredContent = params => postAction('/inspection/inspectionWorkingPaper/getStructuredContent', params)
//新增工作底稿
const addWorkingPaper = params => postAction('/inspection/inspectionWorkingPaper/addWorkingPaper', params)
//编辑问题清单
const addIssueList = params => postAction('/inspection/inspectionIssueList/addIssueList', params)
//问题清单详情
const getStructuredIssueList = params => postAction('/inspection/inspectionIssueList/getStructuredIssueList', params)
//借阅清单新增
const addInspectionBorrow = params => postAction('/inspection/inspectionBorrowController/addInspectionBorrow', params)
//获取借阅清单
const getInspectionBorrowData = params =>
    postAction('/inspection/inspectionBorrowController/getInspectionBorrowData', params)
//修改借阅清单
const editInspectionBorrow = params => postAction('/inspection/inspectionBorrowController/editInspectionBorrow', params)
//删除借阅清单
const delInspectionBorrow = params => getAction('/inspection/inspectionBorrowController/delInspectionBorrow', params)
//添加被查国库部门负责人
const editBorrowCharge = params => postAction('/inspection/inspectionBorrowController/editBorrowCharge', params)
//文件预览接口
const viewDoc = params => postAction('/inspection/inspectionFileController/viewDoc', params)
//文件预览下载
const downFile = window._CONFIG['domianURL'] + '/inspection/inspectionFileController/downFile'
//文件批量下载
const downFileBatch = window._CONFIG['domianURL'] + '/inspection/inspectionFileController/downFileBatch'
//检查报告
const getStructuredReport = params => postAction('/inspection/inspectionReport/getStructuredReport', params)
//编辑检查报告
const addReportList = params => postAction('/inspection/inspectionReport/addReportList', params)
//获取发现问题汇总表
const getStatisticsTable = params => postAction('/inspection/inspectionStatisticsTable/getStatisticsTable', params)
//问题台账list
const getInspectionReformPage = params =>
    postAction('/inspection/inspectionReformController/getInspectionReformPage', params)
//问题台账list
const schemeInfo = params => postAction('/inspection/inspectionReformController/schemeInfo ', params)
const getInspectionReformPage1 = params =>
    postAction('/inspection/inspectionPostSVReformController/getInspectionReformPage', params)
const schemeInfo1 = params => postAction('/inspection/inspectionPostSVReformController/schemeInfo', params)
//问题台账整改方案数据
const getSchemeReplay = params => postAction('/inspection/inspectionReformController/getSchemeReplay', params)
const getSchemeReplay1 = params => postAction('/inspection/inspectionPostSVReformController/getSchemeReplay', params)
//新增方案
const addInspectionScheme = params => postAction('/inspection/inspectionReformController/addInspectionScheme', params)
const addInspectionScheme1 = params =>
    postAction('/inspection/inspectionPostSVReformController/addInspectionScheme', params)
//新增回复
const addInspectionReplay = params => postAction('/inspection/inspectionReformController/addInspectionReplay', params)
const addInspectionReplay1 = params =>
    postAction('/inspection/inspectionPostSVReformController/addInspectionReplay', params)
//删除方案
const delInspectionReformScheme = params =>
    getAction('/inspection/inspectionReformController/delInspectionReformScheme', params)
const delInspectionReformScheme1 = params =>
    getAction('/inspection/inspectionPostSVReformController/delInspectionReformScheme', params)
//删除回复
const delInspectionReformReplay = params =>
    getAction('/inspection/inspectionReformController/delInspectionReformReplay', params)
const delInspectionReformReplay1 = params =>
    getAction('/inspection/inspectionPostSVReformController/delInspectionReformReplay', params)
//公务接待函新增
const addInspectionReception = params =>
    postAction('/inspection/inspectionReceptionController/addInspectionReception', params)
//公务接待函修改
const editInspectionReception = params =>
    postAction('/inspection/inspectionReceptionController/editInspectionReception', params)
//公务接待函获取
const getInspectionReceptionData = params =>
    postAction('/inspection/inspectionReceptionController/getInspectionReceptionData', params)
//问题汇总表修改
const getCurTaskQuestion_1 = params => postAction('/inspection/inspectionStatisticsTable/getCurTaskQuestion_1', params)
//任务计划汇总表
const getCurTaskQuestion_2 = params => postAction('/inspection/inspection/getCurTaskQuestion_1', params)
//查询任务计划汇总表
const getStatisticsTableByPlanId = params => postAction('/inspection/inspection/getStatisticsTableByPlanId', params)
//人才库关联用户验证
const isExistRelation = params => postAction('/talentpool/talentPoolController/isExistRelation', params)
//人才库回显
const getRelationTalentPoolName = params => postAction('/sys/user/getRelationTalentPoolName', params)
//任务锁定接口
const editTaskLock = params => postAction('/inspection/inspectionTaskController/editTaskLock', params)
//根据用户id查询是主查还是组长（1组长2主查）
const getUserBySysId = params => postAction('/inspection/inspectionUserController/getUserBySysId', params)
//推送案例
const toAddCase = params => postAction('/inspection/inspectionReformController/toAddCase', params)
//整改方案完成
const editReform = params => postAction('/inspection/inspectionReformController/editReform', params)
//检查分类树
const getInspectionTypeClassTree = params =>
    postAction('/inspection/inspectionTypeClass/getInspectionTypeClassTree', params)
//通过案例库id查询审批相关信息
const getInspectionApprovalData = params =>
    postAction('/inspection/inspectionApprovalController/getInspectionApprovalData', params)
//审批接口 0通过 1退回
const editInspectionApproval = params =>
    postAction('/inspection/inspectionApprovalController/editInspectionApproval', params)
//通知
const pushInspectionNotification = params =>
    postAction('/inspection/inspectionNotificationPush/pushInspectionNotification', params)
//根据用户id查询权限
const getRoleBySysId = params => postAction('/inspection/inspectionUserController/getRoleBySysId', params)
// 新增数据核对(统计)
const addDataCheckList = params => postAction('/inspection/inspectionDataCheck/addDataCheckList', params)
// 修改数据核对(统计)
const editDataCheckList = params => postAction('/inspection/inspectionDataCheck/editDataCheckList', params)
// 根绝id查询详情
const getInspectionCheckOne = params => postAction('/inspection/inspectionDataCheck/getInspectionCheckOne', params)
//获取所有数据核对列表
const getInspectionCheck = params => postAction('/inspection/inspectionDataCheck/getInspectionCheck', params)
//删除
const delInspectionCheck = params => postAction('/inspection/inspectionDataCheck/delInspectionCheck', params)
//获取根据国库和taskId获取每个被查库数据
const getInspectionCheckInspected = params =>
    postAction('/inspection/inspectionDataCheck/getInspectionCheckInspected', params)
//获取所有数据核对列表-已兑付
const getInspectionCheckBond = params => postAction('/inspection/inspectionCashBond/getInspectionCheck', params)
//已兑付国家债券现场检查账实核对表-新增
const addCashBondList = params => postAction('/inspection/inspectionCashBond/addCashBondList', params)
//已兑付国家债券现场检查账实核对表-修改
const editCashBondList = params => postAction('/inspection/inspectionCashBond/editCashBondList', params)
//已兑付国家债券现场检查账实核对表-删除
const delInspectionCashBond = params => postAction('/inspection/inspectionCashBond/delInspectionCashBond', params)
//获取所有数据核对列表-收款单
const getInspectionCheckReceipt = params => postAction('/inspection/inspectionReceipt/getInspectionCheck', params)
//国家债券收款单现场检查账实核对表-新增
const addReceiptList = params => postAction('/inspection/inspectionReceipt/addReceiptList', params)
//国家债券收款单现场检查账实核对表-修改
const editReceiptList = params => postAction('/inspection/inspectionReceipt/editReceiptList    ', params)
//国家债券收款单现场检查账实核对表-删除
const delInspectionCheckReceipt = params => postAction('/inspection/inspectionReceipt/delInspectionCheck', params)
//子流程跳过
const skipInspection = params => postAction('/inspection/inspectionReceipt/skipInspection', params)
//国债业务实地检查记录表
const getDebtRecordList = params => postAction('/inspection/inspectionIssueList/getDebtRecordList', params)
//新增国债业务实地检查记录表
const addDebtRecordList = params => postAction('/inspection/inspectionIssueList/addDebtRecordList', params)
//分辨流程省市县
const getTreInfoByTaskId = params => postAction('/inspection/inspectionRegisterBook/getTreInfoByTaskId', params)
//获取检查薄详情
const getRegisterBookInfo = params => postAction('/inspection/inspectionRegisterBook/getRegisterBookInfo', params)
//新增检查薄
const addRegisterBook = params => postAction('/inspection/inspectionRegisterBook/addRegisterBook', params)
//新增检查内容
const addContent = params => postAction('/inspection/inspectionContentController/addContent', params)
//修改检查内容
const editContent = params => postAction('/inspection/inspectionContentController/editContent', params)
//获取取证记录
const getEvidenceRecord = params => postAction('/inspection/inspectionEvidenceRecord/getEvidenceRecord', params)
//新增取证记录
const addEvidenceRecord = params => postAction('/inspection/inspectionEvidenceRecord/addEvidenceRecord', params)
//获取通知书
const getEvidenceNotification = params =>
    postAction('/inspection/inspectionEvidenceNotification/getEvidenceNotification', params)
//新增通知书
const addEvidenceNotification = params =>
    postAction('/inspection/inspectionEvidenceNotification/addEvidenceNotification', params)
//执法检查底稿查询
const getEnforceLawWorkingPaper = params =>
    postAction('/inspection/enforceLawWorkingPaper/getEnforceLawWorkingPaper ', params)
//执法检查新增底稿
const addEnforceLawWorkingPaper = params =>
    postAction('/inspection/enforceLawWorkingPaper/addEnforceLawWorkingPaper ', params)
//执法检查立项审批表（新增）
const addApprovalList = params => postAction('/inspection/approvalListController/addApprovalList', params)
//执法检查立项审批表（修改）
const editApprovalList = params => postAction('/inspection/approvalListController/editApprovalList', params)
//执法检查立项审批表（获取）
const getApprovalListData = params => postAction('/inspection/approvalListController/getApprovalListData', params)
//执法检查通知书（新增）
const addLegalNotice = params => postAction('/inspection/legalNoticeController/addLegalNotice', params)
//执法检查通知书（修改）
const editLegalNotice = params => postAction('/inspection/legalNoticeController/editLegalNotice', params)
//执法检查通知书（获取）
const getLegalNoticeData = params => postAction('/inspection/legalNoticeController/getLegalNoticeData', params)
//送达回证（新增）
const addLegalReceipt = params => postAction('/inspection/legalReceiptController/addLegalReceipt', params)
//送达回证（修改）
const editLegalReceipt = params => postAction('/inspection/legalReceiptController/editLegalReceipt', params)
//送达回证（获取）
const getLegalReceiptData = params => postAction('/inspection/legalReceiptController/getLegalReceiptData', params)
//进场检查（新增）
const addEntryRecord = params => postAction('/inspection/entryRecordController/addEntryRecord', params)
//进场检查（修改）
const editEntryRecord = params => postAction('/inspection/entryRecordController/editEntryRecord', params)
//进场检查（获取）
const getEntryRecordData = params => postAction('/inspection/entryRecordController/getEntryRecordData', params)
//执法检查会谈纪要（新增）
const addMeetingMinutes = params => postAction('/inspection/meetingMinutesController/addMeetingMinutes', params)
//执法检查会谈纪要（修改）
const editMeetingMinutes = params => postAction('/inspection/meetingMinutesController/editMeetingMinutes', params)
//执法检查会谈纪要（获取）
const getMeetingMinutesData = params => postAction('/inspection/meetingMinutesController/getMeetingMinutesData', params)
//认定书详情
const getFindingsOfFact = params => postAction('/inspection/findingsOfFact/getFindingsOfFact', params)
//认定书新增
const addFindingsOfFact = params => postAction('/inspection/findingsOfFact/addFindingsOfFact', params)
//执法检查调阅清单（新增）
const addLegalBorrow = params => postAction('/inspection/legalBorrowController/addLegalBorrow', params)
//综合执法借阅清单-保存项目名称
const addInspectProjectName = params => postAction('/inspection/legalBorrowController/addInspectProjectName', params)
//执法检查调阅清单（获取）
const getLegalBorrowData = params => postAction('/inspection/legalBorrowController/getLegalBorrowData', params)
//执法检查调阅清单（修改）
const editLegalBorrow = params => postAction('/inspection/legalBorrowController/editLegalBorrow', params)
//执法检查调阅清单（删除）
const delLegalBorrow = params => getAction('/inspection/legalBorrowController/delLegalBorrow', params)
//执法检查检查报告详情
const getEnforceLawReport = params => postAction('/inspection/enforceLawReport/getEnforceLawReport', params)
//执法检查检查报告新增
const addEnforceLawReport = params => postAction('/inspection/enforceLawReport/addEnforceLawReport', params)
//询问笔录（新增）
const addStatement = params => postAction('/inspection/statementController/addStatement', params)
//询问笔录（获取）
const getStatementData = params => postAction('/inspection/statementController/getStatementData', params)
//询问笔录（修改）
const editStatement = params => postAction('/inspection/statementController/editStatement', params)
//询问笔录（删除）
const delStatement = params => getAction('/inspection/statementController/delStatement', params)
//外部新增查询
const getEvidenceRecordMainInfo = params =>
    postAction('/inspection/inspectionEvidenceRecord/getEvidenceRecordMainInfo', params)
//删除外部
const deleteEvidenceRecord = params => postAction('/inspection/inspectionEvidenceRecord/deleteEvidenceRecord', params)
//自查问题台账新增
const addSelfLedger = params => postAction('/inspection/inspectionSelfLedger/addSelfLedger', params)
//自查问题台账新增
const addSelfLedgerOneToMany = params => postAction('/inspection/inspectionSelfLedger/addSelfLedgerOneToMany', params)
//自查问题台账根据id获取添加人
const selfgetLedgerAddUserByTaskId = params =>
    postAction('/inspection/inspectionSelfLedger/getLedgerAddUserByTaskId', params)
//自查问题台账获取详情
const selfgetQuestionLedgerByLedgerId = params =>
    postAction('/inspection/inspectionSelfLedger/getQuestionLedgerByLedgerId', params)
//自查问题台账人获取详情
const selfgetSelfLedgerByUserIdTaskID = params =>
    postAction('/inspection/inspectionSelfLedger/getSelfLedgerByUserIdTaskID', params)
//自查删除问题台账
const delSelfLedgerByLedgerId = params => postAction('/inspection/inspectionSelfLedger/delSelfLedgerByLedgerId', params)
//自查删除问题台账
const delSelfLedgerOneToMany = params => postAction('/inspection/inspectionSelfLedger/delSelfLedgerOneToMany', params)
//自查编辑问题台账
const editSelfLedgerByLedgerID = params =>
    postAction('/inspection/inspectionSelfLedger/editSelfLedgerByLedgerID', params)
//自查编辑问题台账
const editSelfLedgerOneToMany = params => postAction('/inspection/inspectionSelfLedger/editSelfLedgerOneToMany', params)
//自查问题清单查询
const getCurSelfSumInfo = params => postAction('/inspection/inspectionSelfSummary/getCurSelfSumInfo', params)
//自查问题清单编辑i
const submitCurSelfSumInfo = params => postAction('/inspection/inspectionSelfSummary/submitCurSelfSumInfo', params)
//自查通知
const pushSelfNotificationAttachment = params =>
    formPostAction('/inspection/inspectionNotificationPush/pushSelfNotificationAttachment', params)
//执法检查意见书新增
const addEnforceLawOpinion = params => postAction('/inspection/enforceLawOpinion/addEnforceLawOpinion', params)
//执法检查意见书详情
const getEnforceLawOpinion = params => postAction('/inspection/enforceLawOpinion/getEnforceLawOpinion', params)
//临时检查汇总表查询
const getLedgerReformInfoByTaskId = params =>
    postAction('/inspection/inspectionTemporarySum/getLedgerReformInfoByTaskId', params)
//事后监督获取主流程
const getInspectionProcData1 = params =>
    postAction('/inspection/inspectionPostSVProcController/getInspectionProcData', params)
//事后监督获取子流程
const getInspectionProcSubData1 = params =>
    postAction('/inspection/inspectionPostSVProcController/getInspectionProcSubData', params)
//事后监督清单查询
const getFullInfo = params => postAction('/inspection/inspectionPostSVList/getFullInfo', params)
//事后监督清单新增
const addHandOverList = params => postAction('/inspection/inspectionPostSVList/addHandOverList', params)
//事后监督工作日志获取
const getPostSVWorkDiary = params => postAction('/inspection/postSVWorkDiary/getPostSVWorkDiary', params)
//事后监督工作日志新增
const addPostSVWorkDiary = params => postAction('/inspection/postSVWorkDiary/addPostSVWorkDiary', params)
//事后监督通知书查询
const getPostSVNotice = params => postAction('/inspection/PostSVNotice/getPostSVNotice', params)
//事后监督通知书新增
const addPostSVNotice = params => postAction('/inspection/PostSVNotice/addPostSVNotice', params)
//季度报告详情
const getPostSVReport = params => postAction('/inspection/postSVReport/getPostSVReport', params)
//季度报告编辑
const editPostSVReport = params => postAction('/inspection/postSVReport/editPostSVReport', params)

//指标机构树查询
const selectIndexRelationTree = params =>
    postAction('/indicatorsLib/indexRelationController/selectIndexRelationTree', params)
//新的树
const selectIndexEchartsTree = params =>
    postAction('/indicatorsLib/indexRelationController/selectIndexEchartsTree', params)
//新增指标
const addMineNew = params => postAction('/indicatorsLib/indicatorsMine/addMineNew', params)
//指标查询
const getIndicatorsTable = params => postAction('/indicatorsLib/indexRelationController/getIndicatorsTable', params)
//指标方案查询
const selectSchemeTable = params => postAction('/indicatorsLib/indexSchemeController/selectSchemeTable', params)
//指标方案新增
const saveIndexScheme = params => postAction('/indicatorsLib/indexSchemeController/saveIndexScheme', params)
//执行指标方案
const selectSchemeData = params => postAction('/indicatorsLib/indexSchemeController/selectSchemeData', params)
//删除指标方案
const deleteScheme = params => postAction('/indicatorsLib/indexSchemeController/deleteScheme', params)
//导出指标
const downLoadSchemeData = params => postAction('/indicatorsLib/indexSchemeController/downLoadSchemeData', params)
//指标图
const getIndicatorsECharts = params => postAction('/indicatorsLib/indexRelationController/getIndicatorsECharts', params)
//指标账期数组
const getDateInterval = params => postAction('/indicatorsLib/indexRelationController/getDateInterval', params)
//指标维度数组
const getDimensionSelect = params => postAction('/indicatorsLib/indexRelationController/getDimensionSelect', params)
//指标数详情
const getIndexDetails = params => postAction('/indicatorsLib/indexRelationController/getIndexDetails', params)
//指标导出
const downloadIndexInfo = params => postAction('/indicatorsLib/indexRelationController/downloadIndexInfo', params)
//柱状折线图
const getColumnLineChart = params => postAction('/indicatorsLib/indexRelationController/getColumnLineChart', params)
//指标管理查询
const getIndexManageList = params => postAction('/indicatorsLib/indicatorsMine/getIndexManageList', params)
//指标管理审批提交
const submitApproval = params => postAction('/indicatorsLib/indexApproval/submitApproval', params)
//指标删除
const deleteMineIndex = params => postAction('/indicatorsLib/indicatorsMine/deleteMineIndex', params)
//指标编辑
const updateMineIndex = params => postAction('/indicatorsLib/indicatorsMine/updateMineIndex', params)
//sql试运行
const pilotRunSQL = params => postAction('/indicatorsLib/indicatorsMine/pilotRunSQL', params)
//新增指标级次
const getIndexLevels = params => postAction('/indicatorsLib/indexRelationController/getIndexLevels', params)
//新增指标辖属
const getIndexJurisdiction = params => postAction('/indicatorsLib/indexRelationController/getIndexJurisdiction', params)
//新增指标维度
const getIndexDimnsn = params => postAction('/indicatorsLib/indicatorsMine/getIndexDimnsn', params)
//新增指标周期
const getIndexPeriod = params => postAction('/indicatorsLib/indicatorsMine/getIndexPeriod', params)
//指标父级
const getIndexParentInfo = params => postAction('/indicatorsLib/indicatorsMine/getIndexParentInfo', params)
//指标提交
const submitScheme = params => postAction('/indicatorsLib/indexSchemeController/submitScheme', params)
//指标推送
const insertPublicScheme = params => postAction('/indicatorsLib/indexSchemeController/insertPublicScheme', params)
//指标方案推送
const pushIndexToVS = params => postAction('/indicatorsLib/indexSchemeController/pushIndexToVS', params)
//指标方案公共菜单
const selectPublicScheme = params => postAction('/indicatorsLib/indexSchemeController/selectPublicScheme', params)
//删除个人常用公共方案
const deletePublicScheme = params => postAction('/indicatorsLib/indexSchemeController/deletePublicScheme', params)
//公共指标管理查询
const getPublicIndexManageList = params => postAction('/indicatorsLib//indicatorsMine/getPublicIndexManageList', params)
//编辑公共指标
const updatePublicIndex = params => postAction('/indicatorsLib//indicatorsMine/updatePublicIndex', params)
//删除公共指标
const deletePublicIndex = params => postAction('/indicatorsLib//indicatorsMine/deletePublicIndex', params)
//指标库跑批
const historyRunBatch = params => postAction('/indicatorsLib/indicatorsMine/historyRunBatch', params)
//指标方案重命名
const schemeRename = params => postAction('/indicatorsLib/indexSchemeController/schemeRename', params)
//检查频次维护 获取数据
const getRoutinePeriodInfo = params => postAction('/inspection/routinePeriod/getRoutinePeriodInfo', params)
//检查频次维护 编辑数据
const editRoutinePeriodInfo = params => postAction('/inspection/routinePeriod/editRoutinePeriodInfo', params)
//获取-系统字典数据
const getDictItems = params => getAction('/sys/dict/getDictItems/' + params)

//数据报表查询
const getReportAll = params => postAction('/reportcenter/incomeReport/getReportAll', params)
//数据报表-报表类型
const getReportType = params => postAction('/reportcenter/incomeReport/getReportType', params)
//收入报表查询
const getIncomeReportData = params => postAction('/reportcenter/incomeReport/getIncomeReportData', params)
//预算科目树行结构
const getIncomeSubject = params => postAction('/reportcenter/incomeReport/getIncomeSubject', params)
//支出预算科目树行结构
const getPayoutSubject = params => postAction('/reportcenter/stockReport/getPayoutSubject', params)
//支出查询接口
const getPayoutReportData = params => postAction('/reportcenter/payoutReport/getPayoutReportData', params)
//库存查询接口
const getStockReportData = params => postAction('/reportcenter/stockReport/getStockReportData', params)

//国债巡查查询列表
const getNationalDebtTreeList = params =>
    postAction('/inspection/InspectionNationalDebt/getNationalDebtTreeList', params)
//国债巡查查询列表新增
const insertNationalDebtData = params => postAction('/inspection/InspectionNationalDebt/insertNationalDebtData', params)
//编辑查看
const queryNationalDebtData = params => postAction('/inspection/InspectionNationalDebt/queryNationalDebtData', params)
//编辑提交
const editNationalDebtData = params => postAction('/inspection/InspectionNationalDebt/editNationalDebtData', params)
//汇总数据保存
const insertProjectSummary = params => postAction('/inspection/InspectionNationalDebt/insertProjectSummary', params)
//撤销汇总
const revokeNationalSummary = params => postAction('/inspection/InspectionNationalDebt/revokeNationalSummary', params)
//汇总数据
const queryProjectSummary = params => postAction('/inspection/InspectionNationalDebt/queryProjectSummary', params)
//汇总数据提交
const submitProjectSummary = params => postAction('/inspection/InspectionNationalDebt/submitProjectSummary', params)
//单个数据详情
const selectCheckDataTable = params => postAction('/inspection/InspectionNationalDebt/selectCheckDataTable', params)

//数据表维护 根据数据源查询数据源下的数据库下拉选
const getDataBaseSelection = params => postAction('/seo/dataAuxiliaryController/getDataBaseSelection', params)
//数据表维护 获取数据配置的数据源下拉选
const getDataSourceSelection = params => postAction('/seo/dataAuxiliaryController/getDataSourceSelection', params)
//根据选择的数据表据查询该数据表注释
const getDataTableComments = params => postAction('/seo/dataAuxiliaryController/getDataTableComments', params)
//根据数据源和数据库查询数据库下的数据表下拉选
const getDataTableSelection = params => postAction('/seo/dataAuxiliaryController/getDataTableSelection', params)
//获取数据表一级分类下拉选
const getFirstClassifySelection = params => postAction('/seo/dataAuxiliaryController/getFirstClassifySelection', params)
//二级分类下拉数据
const getSecondClassifySelection = params =>
    postAction('/seo/dataAuxiliaryController/getSecondClassifySelection', params)
//新增数据表接口
const addDataTable = params => postAction('/seo/dataTableController/addDataTable', params)
//根据主键值查询数据表数据接口
const getDataTableData = params => postAction('/seo/dataTableController/getDataTableData', params)
//编辑数据表接口
const editDataTable = params => postAction('/seo/dataTableController/editDataTable', params)
//数据关系表Tree
const getDataSourceTree = params => postAction('/seo/dataTableController/getDataSourceTree', params)
//数据库下的数据表Tree
const getRelationTree = params => postAction('/seo/dataTableController/getRelationTree', params)

//智能报告
//获取新段落UUID
const getNewPGTextID = params => postAction('/aireport/paragraphTemp/getNewPGTempID', params)
//日期类型时间参数
const getDateParams = params => postAction('/aireport/reportUtils/getDateParams', params)
//日期时间参数提交
const addPGDateParam = params => postAction('/aireport/paragraphDateParam/addPGDateParam', params)
//日期时间参数点击编辑
const getParamsData = params => postAction('/aireport/paragraphTemp/getPGTempParamInfo', params)
//指标参数提交
const saveIndexParams = params => postAction('/aireport/paragraphIndexParam/addPGIndexParam', params)
//sql参数提交
const saveSQLParams = params => postAction('/aireport/paragraphSQL/addPGSQLParam', params)
//sql试运行
const pilotRunSQLs = params => postAction('/aireport/paragraphSQL/SQLPilotRun', params)
//图表提交
const saveImageParams = params => postAction('/aireport/paragraphGraph/addPGGraphParam', params)
//表格提交
const saveTableParams = params => postAction('/aireport/paragraphTable/addPGTableParam', params)
//表格列表
const getReportInfo = params => postAction('/aireport/reportUtils/getReportInfo', params)
//段落模板提交
const addPGTextParamInfo = params => postAction('/aireport/paragraphTemp/addPGTempParamInfo', params)
//段落模板list查询
const getPGTextSummary = params => postAction('/aireport/paragraphTemp/getPGTempSummary', params)
//段落模板编辑
const getParamTypeMaxIndex = params => postAction('/aireport/paragraphTemp/getParamTypeMaxIndex', params)
//报告模板编辑
const getPGTempSummaryById = params => postAction('/aireport/reportTemplate/getPGTempSummaryById', params)
//段落模板删除
const delPGTempInfo = params => postAction('/aireport/paragraphTemp/delPGTempInfo', params)
//段落模板提交
const submitPGTempSummary = params => postAction('/aireport/paragraphTemp/submitPGTempSummary', params)
//生成模板
const viewReportEntity = params => postAction('/aireport/entityReport/viewReportEntity', params)
//运算符枚举
const getLogicalOperator = params => postAction('/EnumController/getLogicalOperator', params)

//报表现金流预测查询
const gkList = params => postAction('/reportcenter/gkForeCast/gkList', params)
//现金流预测科目树
const getUserSubjectTreeId = params => postAction('/reportcenter/gkForeCast/getUserSubjectTreeId', params)
//库存报表查询
const queryInventoryReport = params => postAction('/reportcenter/InventoryReport/queryInventoryReport', params)
//库存第一次下钻
const queryJurisdictionData = params => postAction('/reportcenter/InventoryReport/queryJurisdictionData', params)
//库存第二次下钻
const queryAreaData = params => postAction('/reportcenter/InventoryReport/queryAreaData', params)
//国库与商业银行资金流动
const queryCapitalFlowData = params => postAction('/reportcenter/capitalFlow/queryCapitalFlowData', params)
//银行树
const getBankOrgSelect = params => postAction('/reportcenter/capitalFlow/getBankOrgSelect', params)

//支出科目顶层表查询
const queryExpenseReport = params => postAction('/reportcenter/expenseSubject/queryExpenseReport', params)
//获取预算科目下拉选值
const getBudgetSubjectSelect = params => postAction('/reportcenter/expenseSubject/getBudgetSubjectSelect', params)
//支出下钻一查询
const queryCorporationExpense = params => postAction('/reportcenter/expenseSubject/queryCorporationExpense', params)
//支出下钻二查询
const queryExpenseDetails = params => postAction('/reportcenter/expenseSubject/queryExpenseDetails', params)
//支出下钻三查询
const queryExpenseAccount = params => postAction('/reportcenter/expenseSubject/queryExpenseAccount', params)
export {
    getIndustryTree,
    addQuestionRule,
    editQuestionRule,
    addQuestionRule1,
    editQuestionRuleTemp,
    getQuestionBankData,
    getQuestionBankTreeNew,
    addQuestionBank,
    editQuestionBank,
    editQuestionBankTemp,
    questionBankddQuestionBank,
    saveQuestionRuleRelation,
    getQuestionRuleByRelation,
    delQuestionRuleRelation,
    getGuoKuTree,
    addTalentPool,
    editTalentPool,
    getUserInfo,
    getEnumType,
    getEnumTypeTree,
    getEnumData,
    addEnum,
    editEnum,
    delEnum,
    getEnumTypeAll,
    getEnumTypeAllParams,
    getDetectionType,
    getDetectionData,
    addDetection,
    editDetection,
    delDetection,
    getDetectionTypeAll,
    addBookOrg,
    editBookOrg,
    getOrgTree,
    getGuokuTree,
    getAreaTree,
    addGuoku,
    editGuoku,
    addArea,
    editArea,
    getArea,
    levyingBodiesAdd,
    levyingBodiesEdit,
    subjectImportAdd,
    subjectImportEdit,
    subjectImportReadExcel,
    subjectImportReadExcelStat,
    subjectImportReadExcelT,
    getGKbyBook,
    getBookbyGuokuId,
    getPGuoKu,
    getInspectionPlanInspected,
    addInspectionTask,
    editInspectionTask,
    getInspectionTaskData,
    getInspectionTaskDataSV,
    saveInspectUser,
    getTalentPool,
    editUserDuties,
    addImmediatelyReminder,
    addTimerReminder,
    addPlanImmediatelyReminder,
    addPlanTimerReminder,
    getInspectionProcData,
    getInspectionProcSubData,
    saveInspect,
    saveInspectPlan,
    editInspect,
    buildInspectTaskList,
    getInspectionUserData,
    getLedgerAddUserByTaskId,
    getQuestionLedgerByUserIdTaskID,
    getQuestionBankTree,
    getQuestionBankPage,
    addQuestionLedger,
    editQuestionLedgerByLedgerID,
    editQuestionLedgerOneToMany,
    delQuestionLedgerByLedgerId,
    delQuestionLedgerOneToMany,
    getCurTreCodeByTaskId,
    getTaxOrgInfo,
    getCheckTaxOrgInfo,
    addCheckAccountSheet,
    getCheckContent,
    getCheckAccInfoByTaskId,
    getQuestionBankTreeForQuestionLedger,
    editCheckAccountSheet,
    delCheckAccountSheet,
    getCheckAccSubInfoBySheetId,
    getUserData,
    submitGroupInfo,
    addInspectionNotice,
    delInspectionNotice,
    editInspectionNotice,
    getInspectionNoticeData,
    fileUploadfiles,
    getFiles,
    delFile,
    getStructuredContent,
    addWorkingPaper,
    addIssueList,
    getStructuredIssueList,
    addInspectionBorrow,
    getInspectionBorrowData,
    editInspectionBorrow,
    delInspectionBorrow,
    editBorrowCharge,
    addInspectProjectName,
    viewDoc,
    downFile,
    downFileBatch,
    getStructuredReport,
    addReportList,
    getStatisticsTable,
    getInspectionReformPage,
    schemeInfo,
    schemeInfo1,
    getInspectionReformPage1,
    getSchemeReplay,
    getSchemeReplay1,
    addInspectionScheme,
    addInspectionScheme1,
    addInspectionReplay,
    addInspectionReplay1,
    delInspectionReformScheme,
    delInspectionReformScheme1,
    delInspectionReformReplay,
    delInspectionReformReplay1,
    addInspectionReception,
    editInspectionReception,
    getInspectionReceptionData,
    getCurTaskQuestion_1,
    getCurTaskQuestion_2,
    getStatisticsTableByPlanId,
    isExistRelation,
    getRelationTalentPoolName,
    getGuoKuIdTreeTrans,
    editTaskLock,
    getUserBySysId,
    toAddCase,
    editReform,
    getInspectionTypeClassTree,
    getInspectionApprovalData,
    editInspectionApproval,
    pushInspectionNotification,
    getRoleBySysId,
    addDataCheckList,
    editDataCheckList,
    getInspectionCheckOne,
    getInspectionCheck,
    delInspectionCheck,
    getInspectionCheckInspected,
    getInspectionCheckBond,
    addCashBondList,
    editCashBondList,
    delInspectionCashBond,
    getInspectionCheckReceipt,
    addReceiptList,
    editReceiptList,
    delInspectionCheckReceipt,
    skipInspection,
    getDebtRecordList,
    addDebtRecordList,
    getTreInfoByTaskId,
    getRegisterBookInfo,
    addRegisterBook,
    addContent,
    editContent,
    getEvidenceRecord,
    addEvidenceRecord,
    getEvidenceNotification,
    addEvidenceNotification,
    getEnforceLawWorkingPaper,
    addEnforceLawWorkingPaper,
    addApprovalList,
    editApprovalList,
    getApprovalListData,
    addLegalNotice,
    editLegalNotice,
    getLegalNoticeData,
    addLegalReceipt,
    getLegalReceiptData,
    editLegalReceipt,
    addEntryRecord,
    editEntryRecord,
    getEntryRecordData,
    addMeetingMinutes,
    editMeetingMinutes,
    getMeetingMinutesData,
    getFindingsOfFact,
    addFindingsOfFact,
    addLegalBorrow,
    getLegalBorrowData,
    editLegalBorrow,
    delLegalBorrow,
    getEnforceLawReport,
    addEnforceLawReport,
    addStatement,
    delStatement,
    editStatement,
    getStatementData,
    getEvidenceRecordMainInfo,
    deleteEvidenceRecord,
    addSelfLedger,
    addSelfLedgerOneToMany,
    selfgetLedgerAddUserByTaskId,
    selfgetQuestionLedgerByLedgerId,
    selfgetSelfLedgerByUserIdTaskID,
    delSelfLedgerByLedgerId,
    delSelfLedgerOneToMany,
    editSelfLedgerByLedgerID,
    editSelfLedgerOneToMany,
    getCurSelfSumInfo,
    submitCurSelfSumInfo,
    pushSelfNotificationAttachment,
    getEnforceLawOpinion,
    addEnforceLawOpinion,
    getLedgerReformInfoByTaskId,
    getInspectionProcSubData1,
    getInspectionProcData1,
    addHandOverList,
    getFullInfo,
    getLedgerAddUserByTaskId1,
    getQuestionLedgerByUserIdTaskID1,
    addQuestionLedger1,
    addQuestionLedgerOneToMany,
    addQuestionLedgerOneToMany1,
    editQuestionLedgerByLedgerID1,
    editQuestionLedgerOneToMany1,
    delQuestionLedgerByLedgerId1,
    delQuestionLedgerOneToMany1,
    getQuestionBankTreeForQuestionLedger1,
    addPostSVWorkDiary,
    getPostSVWorkDiary,
    addPostSVNotice,
    getPostSVNotice,
    editPostSVReport,
    getPostSVReport,
    selectIndexRelationTree,
    selectIndexEchartsTree,
    addMineNew,
    getIndicatorsTable,
    selectSchemeTable,
    saveIndexScheme,
    selectSchemeData,
    deleteScheme,
    downLoadSchemeData,
    getIndicatorsECharts,
    getDateInterval,
    getDimensionSelect,
    getIndexDetails,
    downloadIndexInfo,
    getIndexManageList,
    submitApproval,
    deleteMineIndex,
    updateMineIndex,
    pilotRunSQL,
    getIndexLevels,
    getIndexJurisdiction,
    getIndexDimnsn,
    getIndexPeriod,
    getIndexParentInfo,
    submitScheme,
    selectPublicScheme,
    deletePublicScheme,
    getPublicIndexManageList,
    updatePublicIndex,
    deletePublicIndex,
    historyRunBatch,
    getColumnLineChart,
    insertPublicScheme,
    pushIndexToVS,
    schemeRename,
    getRoutinePeriodInfo,
    editRoutinePeriodInfo,
    getDictItems,
    getIncomeReportData,
    getIncomeSubject,
    getPayoutSubject,
    getReportAll,
    getReportType,
    getPayoutReportData,
    getStockReportData,
    getNationalDebtTreeList,
    insertNationalDebtData,
    queryNationalDebtData,
    editNationalDebtData,
    insertProjectSummary,
    revokeNationalSummary,
    queryProjectSummary,
    submitProjectSummary,
    selectCheckDataTable,
    getDataBaseSelection,
    getDataSourceSelection,
    getDataTableComments,
    getDataTableSelection,
    getFirstClassifySelection,
    getSecondClassifySelection,
    addDataTable,
    getDataTableData,
    editDataTable,
    getDataSourceTree,
    getRelationTree,
    getDateParams,
    addPGDateParam,
    getParamsData,
    saveIndexParams,
    pilotRunSQLs,
    saveSQLParams,
    saveImageParams,
    saveTableParams,
    getReportInfo,
    getNewPGTextID,
    addPGTextParamInfo,
    getPGTextSummary,
    getParamTypeMaxIndex,
    getPGTempSummaryById,
    delPGTempInfo,
    submitPGTempSummary,
    viewReportEntity,
    getFinanceOrgLv3,
    getFinanceOrgLv4,
    getLogicalOperator,
    gkList,
    getUserSubjectTreeId,
    queryInventoryReport,
    queryJurisdictionData,
    queryAreaData,
    queryCapitalFlowData,
    getBankOrgSelect,
    getBudgetSubjectSelect,
    queryExpenseReport,
    queryCorporationExpense,
    queryExpenseDetails,
    queryExpenseAccount,
    getGuokuInfo,
    addIg,
    getImportantGuoku
}
