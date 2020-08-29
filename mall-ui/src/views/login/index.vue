<template>
  <div class="login-container">
    <el-form ref="loginForm"  class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">MALL 后台管理系统</h3>
      </div>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="oauth2Button('mall', 540)">TAO-X 授 权 登 录 </el-button>
    </el-form>
  </div>
</template>

<script>
import openWindow from '@/utils/open-window'

export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      redirect: undefined,
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  destroyed() {
    window.removeEventListener('storage', this.afterQRScan)
  },
  created() {
    window.addEventListener('storage', this.afterQRScan)
  },
  methods: {
    oauth2Button(thirdpart, wdith) {
      this.loading = true;
      const redirect_uri = encodeURIComponent(window.location.origin + '/#/auth-redirect')
      const url = process.env.VUE_APP_BASE_API + `${thirdpart}/oauth2/authorization/${thirdpart}?redirect_uri=${redirect_uri}`;
      openWindow(url, thirdpart, wdith, 540)
      this.loading = false
    },
    afterQRScan(e) {
      if (e.key === 'x-admin-oauth-code') {
        this.$store.dispatch('user/saveToken', e.newValue)
        this.$router.push({ path: this.redirect || '/' })
      }
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#2b2f3a;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 76%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2b2f3a;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;

  }

  .is-unactive {
    color: #454545;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 60px auto;
      text-align: center;
      font-weight: bold;
    }
  }
}
</style>
