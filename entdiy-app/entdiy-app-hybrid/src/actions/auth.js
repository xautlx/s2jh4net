import api from 'src/utils/api'
import * as actionTypes from './actionTypes'

export const login = (username, password) => {
    let params = {username, password}

    return {
        type: actionTypes.AUTH_LOGIN,
        payload: api.post('login', params).then(res => res.data)
    }
}

export const loginOut = () => {
    return {
        type: actionTypes.AUTH_LOGIN_OUT
    }
}

export const modifyPassword = (old_password, new_password) => {
    return {
        type: actionTypes.PASSWORD_MODIFY,
        payload: api.post('user/password', {
            oldpasswd: old_password,
            newpasswd: new_password
        }).then(res => res)
    }
}

export const setGpass = (pass) => {
    return {
        type: actionTypes.GESTURE_PASSWORD_SET,
        payload: pass
    }
}
