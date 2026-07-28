/*综合查询*/
import {
    getAction,
    deleteAction,
    putAction,
    postAction,
    formPostAction,
    postActionFormData,
    downFilePost
} from '@/api/manage'

/**
 * 获取表名
 * @param params
 * @returns {*}
 */
const getTableName = (params) => postAction('/seo/seoController/getTableName', params);
/**
 * 获取列名
 * @param params TABLE_SIGN 表名
 * @returns {*}
 */
const getColumn = (params) => postAction('/seo/seoController/getColumn', params);
/**
 * 查询
 * @param params  筛选 {WHERE_LEFT左  WHERE_MIDDLE中 WHERE_RIGHT右}  table 表名
 * @returns {*}
 */
const executeSql = (params) => postActionFormData('/seo/seoController/executeSql', params);

/**
 * 获取数据库类型
 * @param params
 * @returns {*}
 */
const getDataSourceEnumSelect = (params) => postAction('/seo/dataSourceController/getDataSourceEnumSelect', params);
/**
 * 根据数据库类型获取数据源
 * @param params
 * @returns {*}
 */
const getDataBase = (params) => postAction('/seo/dataSourceController/getDataBase', params);
/**
 * 数据源编辑
 * @param params
 * @returns {*}
 */
const editDataSource = (params) => postAction('/seo/dataSourceController/editDataSource', params);
/**
 * 根据数据库，IP，端口号获取数据库名
 * @param params
 * @returns {*}
 */
const getDataSource = (params) => postAction('/seo/dataSourceController/getDataSource', params);
/**
 * 新增数据源
 * @param params
 * @returns {*}
 */
const addDataSource = (params) => postAction('/seo/dataSourceController/addDataSource', params);
/**
 * 测试链接
 * @param params
 * @returns {*}
 */
const testConnection = (params) => postAction('/seo/dataSourceController/testConnection', params);
/**
 * 校验数据库名称是否重复
 * @param params
 * @returns {*}
 */
const getDataSourceName = (params) => postAction('/seo/dataSourceController/getDataSourceName', params);
/**
 * 指标计算
 * @param params
 * @returns {*}
 */
const calculate = (params) => postAction('/seo/seoController/calculate', params);
/**
 * 删除redis缓存
 * @param params
 * @returns {*}
 */
const delRedis = (params) => postAction('/seo/seoController/delRedis', params);
/**
 * 方案保存
 * @param params
 * @returns {*}
 */
const addSchemeMain = (params) => postActionFormData('/seo/seoController/addSchemeMain', params);
/**
 * 方案保存
 * @param params
 * @returns {*}
 */
const editSchemeMain = (params) => postActionFormData('/seo/seoController/editSchemeMain', params);
/**
 * 查重名
 * @param params
 * @returns {*}
 */
const checkScheme = (params) => postActionFormData('/seo/seoController/checkScheme', params);
/**
 * 获取方案-分页
 * @param params
 * @returns {*}
 */
const getSchemeMainPage = (params) => postActionFormData('/seo/seoController/getSchemeMainPage', params);
/**
 * 获取方案-不分页
 * @param params
 * @returns {*}
 */
const getSchemeMain = (params) => postActionFormData('/seo/seoController/getSchemeMain', params);
/**
 * 删除方案
 * @param params
 * @returns {*}
 */
const delSchemeMain = (params) => postActionFormData('/seo/seoController/delSchemeMain', params);
/**
 * 获取方案具体字段
 * @param params
 * @returns {*}
 */
const getSchemeColumn = (params) => postActionFormData('/seo/seoController/getSchemeColumn', params);
/**
 * 获取方案具体字段
 * @param params
 * @returns {*}
 */
const getSchemeTable = (params) => postActionFormData('/seo/seoController/getSchemeTable', params);
/**
 * 获取方案详情
 * @param params
 * @returns {*}
 */
const getSchemeInfo = (params) => postActionFormData('/seo/seoController/getSchemeInfo', params);
/**
 * 点击方案自动执行查询
 * @param params
 * @returns {*}
 */
const executeSqlFromFont = (params) => postActionFormData('/seo/seoController/executeSqlFromFont', params);
const download = (params, fileName, total) => downFilePost('/seo/seoController/download', params).then(data => {
    if (!data || data.size === 0) {
        this.$message.warning('文件下载失败');
        return
    }
    if (typeof window.navigator.msSaveBlob !== 'undefined') {
        if (total > 60000) {
            window.navigator.msSaveBlob(new Blob([data]), fileName + '.zip');
        } else {
            window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls');
        }
    } else {
        let url = window.URL.createObjectURL(new Blob([data]));
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        if (total > 60000) {
            link.setAttribute('download', fileName + '.zip');
        } else {
            link.setAttribute('download', fileName + '.xls');
        }
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);//下载完成移除元素
        window.URL.revokeObjectURL(url); //释放掉blob对象
    }
});

/**
 * 获取文件列表
 * @param params
 * @returns {*}
 */
const skip = (params) => postActionFormData('/seo/forSkip/skipJump', params);
/**
 * 预览
 * @param params
 * @returns {*}
 */
const viewDoc = (params) => postActionFormData('/seo/forSkip/viewDoc', params);

/**
 * pdf预览
 * @param params
 * @returns {*}
 */
const viewPdf = (params) => postActionFormData('/seo/forSkip/viewPdf', params);
export {
    getTableName,
    getColumn,
    executeSql,
    getDataSourceEnumSelect,
    getDataBase,
    editDataSource,
    getDataSource,
    addDataSource,
    testConnection,
    getDataSourceName,
    calculate,
    delRedis,
    addSchemeMain,
    editSchemeMain,
    checkScheme,
    getSchemeMainPage,
    getSchemeMain,
    delSchemeMain,
    getSchemeColumn,
    getSchemeTable,
    getSchemeInfo,
    executeSqlFromFont,
    download,
    skip,
    viewDoc,
    viewPdf
}
