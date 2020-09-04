<template>
  <div>
    <h2 style="text-align: center">{{ errMsg }}, {{ time }} 秒后自动关闭</h2>
  </div>
</template>
<script>
export function param2Obj(url) {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}
export default {
  name: 'AuthRedirect',
  data() {
    return {
      errMsg: '',
      time: 5
    }
  },
  created() {
    var timer = setInterval(() => {
      this.time--
      if (this.time <= 0) {
        clearInterval(timer)
        window.close()
      }
    }, 1000)
    this.errMsg = param2Obj(window.location.hash)['msg']
    var token = param2Obj(window.location.hash)['access_token']

    if (token) {
      if (window.localStorage) {
        window.localStorage.setItem('x-admin-oauth-code', token)
        window.localStorage.clear('x-admin-oauth-code')
        window.close()
      }
    }
  }
}
</script>
