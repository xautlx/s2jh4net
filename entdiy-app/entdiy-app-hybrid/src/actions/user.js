import objectPath from 'object-path'
import api from '../utils/api'
import * as actionTypes from './actionTypes'
import store from 'src/store'

export const getUserInfo = (params) => {
    return {
        type: actionTypes.USER_INFO_FETCH,
        payload: api.get('demo/user/info', params)
            .then(res => res.data)
    }
}

export const uploadAvatar = (data) => {
    return {
        type: actionTypes.USER_INFO_AVATAR_UPDATE,
        payload: api.request('demo/user/photo/edit', {
            method: 'POST',
            body: data
        }).then(res => res.data.url)
    }
}

export const updateUserInfo = (payload) => {
    return {
        type: actionTypes.USER_INFO_UPDATE,
        payload: payload,
    }
}

export const updateUserInfoByAjax = (params) => dispatch => {
    let prev = store.getState().user.info

    let updated = Object.assign({}, prev)
    for (let [key, val] of Object.entries(params)) {
        objectPath.set(updated, key, val)
    }
    dispatch(updateUserInfo(updated))

    return api.post('demo/user/info/edit', params)
        .then(res => res.data)
        .catch(err => {
            dispatch(updateUserInfo(prev))
            throw err
        })
}