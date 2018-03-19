import api from 'src/utils/api'
import * as actionTypes from './actionTypes'

//获取消息数量
export const getMessageCount = () => ({
    type: actionTypes.MESSAGE_GET_COUNT,
    payload: api.get('account-message/count', {})
        .then(res => res.data)
})

//标记为已读
export const markRead = (dataClass, id, index) => ({
    type: actionTypes.MESSAGE_CHANG_MARK_READ,
    payload: api.post('account-message/read', {id})
        .then((res) => {
            if (res.data.result.result === 'success') {
                return {
                    dataClass,
                    index
                }
            }
        })
})