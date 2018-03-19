import api from 'src/utils/api'
import * as actionTypes from './actionTypes'

// 获取订单详情
export const getDetail = (id) => ({
  type: actionTypes.ORDER_DETAIL_FETCH,
  payload: api.get('demo/order/detail', {
    id
  }).then(res => res.data)
})