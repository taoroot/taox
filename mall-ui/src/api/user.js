import request from '@/utils/request'

export function getUsers(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

export function delItem(ids) {
  return request({
    url: `/user`,
    method: 'delete',
    params: {
      ids: ids
    }
  })
}

export function saveItem(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateItem(data) {
  return request({
    url: `/user`,
    method: 'put',
    data
  })
}

export function updatePermission(userId, menuIds) {
  return request({
    url: `/user/${userId}/authorities`,
    method: 'put',
    params: {
      authorityIds: menuIds
    }
  })
}

export function getPermission(userId) {
  return request({
    url: `/user/${userId}/authorities`,
    method: 'get'
  })
}

export function changeUserStatus(id, enabled) {
  const data = {
    id,
    enabled
  }
  return request({
    url: `/user`,
    method: 'put',
    data: data
  })
}
