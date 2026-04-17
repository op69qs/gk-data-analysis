import Vue from 'vue'

const postRecord = {
  state: {
    record: {} ||localStorage.setItem('record'),
    SUB_ID:{} || localStorage.setItem('SUB_ID'),
    SUB_ID_1:{} || localStorage.setItem('SUB_ID_1'),
    SUB_ID_2:{} || localStorage.setItem('SUB_ID_2'),
    SUB_ID_3:{} || localStorage.setItem('SUB_ID_3'),
    SUB_ID_4:{} || localStorage.setItem('SUB_ID_4'),
    SUB_ID_5:{} || localStorage.setItem('SUB_ID_5'),
    PROC_SUB_ID:{}||localStorage.setItem('PROC_SUB_ID'),
    user:1 || localStorage.setItem('user'),
    tableList:{}||localStorage.setItem('tableList'),
    check_date:{}||localStorage.setItem('check_date'),
  },
  mutations: {
    SET_RECORD_TYPE: (state, type) => {
      state.record = type
      localStorage.setItem('record', JSON.stringify(type))
    },
    SET_SUBID_TYPE_1: (state, type) => {
      state.SUB_ID = type
      localStorage.setItem('SUB_ID_1', JSON.stringify(type))
    },
    SET_SUBID_TYPE_2: (state, type) => {
      state.SUB_ID = type
      localStorage.setItem('SUB_ID_2', JSON.stringify(type))
    },
    SET_SUBID_TYPE_3: (state, type) => {
      state.SUB_ID = type
      localStorage.setItem('SUB_ID_3', JSON.stringify(type))
    },
    SET_SUBID_TYPE_4: (state, type) => {
      state.SUB_ID = type
      localStorage.setItem('SUB_ID_4', JSON.stringify(type))
    },
    SET_SUBID_TYPE_5: (state, type) => {
      state.SUB_ID = type
      localStorage.setItem('SUB_ID_5', JSON.stringify(type))
    },
    SET_SUBID_TYPE: (state, type) => {
      state.SUB_ID = type
      localStorage.setItem('SUB_ID', JSON.stringify(type))
    },
    SET_PROCSUBID_TYPE: (state, type) => {
      state.PROC_SUB_ID = type
      localStorage.setItem('PROC_SUB_ID', JSON.stringify(type))
    },
    SET_USER_TYPE: (state, type) => {
      state.user = type
      localStorage.setItem('user',type)
    },
    SET_TABLELIST: (state, type) => {
      state.tableList = type
      localStorage.setItem('tableList', JSON.stringify(type))
    },
    SET_CHECK_DATE: (state, type) => {
      state.tableList = type
      localStorage.setItem('check_date', JSON.stringify(type))
    },
  },
  actions: {
    postRecord: ({ commit }, type) => {
      commit('SET_RECORD_TYPE', type)
    },
    SET_SUBID: ({ commit }, type) => {
      commit('SET_SUBID_TYPE', type)
    },
    SET_SUBID1: ({ commit }, type) => {
      commit('SET_SUBID_TYPE_1', type)
    },
    SET_SUBID2: ({ commit }, type) => {
      commit('SET_SUBID_TYPE_2', type)
    },
    SET_SUBID3: ({ commit }, type) => {
      commit('SET_SUBID_TYPE_3', type)
    },
    SET_SUBID4: ({ commit }, type) => {
      commit('SET_SUBID_TYPE_4', type)
    },
    SET_SUBID5: ({ commit }, type) => {
      commit('SET_SUBID_TYPE_5', type)
    },
    SET_PROCSUBID: ({ commit }, type) => {
      commit('SET_PROCSUBID_TYPE', type)
    },
    TABLELIST: ({ commit }, type) => {
      commit('SET_TABLELIST', type)
    },
    CHECK_DATE: ({ commit }, type) => {
      commit('SET_CHECK_DATE', type)
    },
    user: ({ commit }, type) => {
      commit('SET_USER_TYPE', type)
    },
  }
}

export default postRecord