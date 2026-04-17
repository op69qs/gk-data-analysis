import Vue from 'vue'

const schemeRecord = {
  state: {
    records: {} ||localStorage.setItem('records'),
  },
  mutations: {
    SET_RECORD_TYPE: (state, type) => {
      state.records = type
      localStorage.setItem('records', JSON.stringify(type))
    },
  },
  actions: {
    schemeRecord: ({ commit }, type) => {
      commit('SET_RECORD_TYPE', type)
    },
  }
}

export default schemeRecord