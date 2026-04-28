<template>
  <div style="display:flex;flex-direction:column;align-items:center;justify-content:center;height:100vh;">
    <template v-if="error">
      <a-result
        status="error"
        :title="error"
        sub-title="单点登录失败，请重试或使用账号密码登录">
        <template #extra>
          <a-button type="primary" @click="backToLogin">返回登录</a-button>
        </template>
      </a-result>
    </template>
    <template v-else>
      <a-spin tip="正在登录，请稍候..." size="large" />
    </template>

    <!-- Department selection modal (mirrors Login.vue depart logic) -->
    <a-modal
      title="登录部门选择"
      :width="450"
      :visible="departVisible"
      :closable="false"
      :maskClosable="false">
      <template slot="footer">
        <a-button type="primary" @click="departOk">确认</a-button>
      </template>
      <a-form>
        <a-form-item
          :labelCol="{span:4}"
          :wrapperCol="{span:20}"
          style="margin-bottom:10px"
          :validate-status="validateStatus">
          <a-tooltip placement="topLeft">
            <template slot="title">
              <span>您隶属于多部门，请选择登录部门</span>
            </template>
            <a-avatar style="backgroundColor:#87d068" icon="gold" />
          </a-tooltip>
          <a-select
            @change="departChange"
            :class="{'valid-error': validateStatus === 'error'}"
            placeholder="请选择登录部门"
            style="margin-left:10px;width:80%">
            <a-icon slot="suffixIcon" type="gold" />
            <a-select-option
              v-for="d in departList"
              :key="d.id"
              :value="d.orgCode">
              {{ d.departName }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { mapActions } from 'vuex'
  import { putAction } from '@/api/manage'
  import { USER_INFO } from '@/store/mutation-types'
  import store from '@/store/'

  export default {
    name: 'OAuthCallback',
    data() {
      return {
        error: '',
        departVisible: false,
        departList: [],
        departSelected: '',
        departUsername: '',
        validateStatus: ''
      }
    },
    mounted() {
      this.handleCallback()
    },
    methods: {
      ...mapActions(['OAuthLogin']),

      handleCallback() {
        const code = this.$route.query.code
        const state = this.$route.query.state

        if (!code) {
          this.error = '缺少授权码，无法完成登录'
          return
        }

        // CSRF guard: verify state against what was stored before redirect
        const savedState = sessionStorage.getItem('oauth_state')
        sessionStorage.removeItem('oauth_state')
        if (savedState && state !== savedState) {
          this.error = '登录状态验证失败（state 不匹配），请重新发起登录'
          return
        }

        this.OAuthLogin({ code })
          .then(res => {
            if (!res.success) {
              this.error = res.message || 'OAuth登录失败'
              return
            }
            const multi_depart = res.result.multi_depart
            if (multi_depart === 0) {
              this.$notification.warn({
                message: '提示',
                description: '您尚未归属部门，请确认账号信息',
                duration: 3
              })
              this.loginSuccess()
            } else if (multi_depart === 2) {
              this.departList = res.result.departs
              this.departUsername = res.result.userInfo && res.result.userInfo.username
              this.departVisible = true
            } else {
              this.loginSuccess()
            }
          })
          .catch(err => {
            this.error = (err && err.message) ? err.message : 'OAuth登录请求失败'
          })
      },

      loginSuccess() {
        const redirect = this.$route.query.redirect
        this.$router.push({ path: redirect || '/dashboard/analysis' })
        this.$notification.success({
          message: '欢迎',
          description: '登录成功'
        })
      },

      backToLogin() {
        this.$router.push({ path: '/user/login' })
      },

      departChange(value) {
        this.validateStatus = 'success'
        this.departSelected = value
      },

      departOk() {
        if (!this.departSelected) {
          this.validateStatus = 'error'
          return
        }
        putAction('/sys/selectDepart', {
          orgCode: this.departSelected,
          username: this.departUsername
        }).then(res => {
          if (res.success) {
            const userInfo = res.result.userInfo
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            store.commit('SET_INFO', userInfo)
            this.departVisible = false
            this.loginSuccess()
          } else {
            this.error = res.message || '部门选择失败'
            this.departVisible = false
          }
        })
      }
    }
  }
</script>
