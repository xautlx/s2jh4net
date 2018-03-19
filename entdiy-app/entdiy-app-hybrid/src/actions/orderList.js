import api from '../utils/api'
import * as actionTypes from './actionTypes'

//获取我的订单
export const getMyOrder = (status, type, page) => {
    let params = {
        'search[NU_payTime]': status === 'topay' ? 'true' : '',
        page,
        rows: 10
    }
    return {
        type: status === 'topay' ? actionTypes.GET_MY_ORDER_TOPAY : actionTypes.GET_MY_ORDER_ALL,
        payload: api.get('demo/order/list', params)
            .then(res => {
                return {type, data: res.data.content || []}
            })
    }
}

//改变我的订单展示的tabIndex
export const changeActiveTab = (activeIndex) => {
    return {
        type: actionTypes.CHANGE_MY_ORDER_ACTIVE_TAB,
        activeIndex
    }
}

//改变我的订单是否需要刷新的状态
export const changeNeedRefresh = () => {
    return {
        type: actionTypes.CHANGE_MY_ORDER_NEED_REFRESH
    }
}

//改变我的订单历史scrollTop及历史pageIndex
export const changeHistoryScrollTop = (historyScrollTop, historyPageIndex) => {
    return {
        type: actionTypes.CHANGE_MY_ORDER_HISTORY_SCROLLTOP,
        historyScrollTop,
        historyPageIndex
    }
}
