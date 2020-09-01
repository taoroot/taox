import request from '@/utils/request'

export function login(params) {
  return request({
    url: '/login',
    method: 'post',
    params
  })
}

export function loginPhone(params) {
  return request({
    url: '/login/phone',
    method: 'post',
    params
  })
}

export function getInfo() {
  return request({
    url: '/upms/user_info',
    method: 'get'
  })
}

export function getUserSocial() {
  return request({
    url: '/upms/user_socials',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/mall/logout',
    method: 'post'
  })
}

export function getUserProfile() {
  return request({
    url: '/upms/user_info',
    method: 'get'
  })
}

export function unbindUserSocial(id) {
  return request({
    url: `/upms/user_social/${id}`,
    method: 'delete'
  })
}

export function updateUser(data) {
  return request({
    url: `/upms/user_info`,
    method: 'put',
    data
  })
}

export function resetPassword(data) {
  return request({
    url: `/upms/user_password`,
    method: 'put',
    data
  })
}
