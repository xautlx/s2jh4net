
import api from '../utils/api'
import * as actionTypes from './actionTypes'

export const getUpdateInfo = (currentVersion) => {
  let params = {
    currentVersion,
  }
  return {
    type: actionTypes.UPDATE_INFO_FETCH,
    payload: api.post('v2/update/check-version-update', params)
      .then(res => res.data.result || {})
  }
}

export const changeUpdateInfo = (obj) => ({
  type: actionTypes.UPDATE_INFO_CHANGE,
  payload: obj,
})