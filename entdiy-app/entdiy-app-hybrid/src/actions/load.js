import * as actionTypes from './actionTypes'

//首页
export const homeLoaded = () => ({
  type: actionTypes.LOADED_HOME
})

//我的订单
export const orderLoaded = () => ({
  type: actionTypes.LOADED_ORDER
})

//个人中心（我的）
export const mineLoaded = () => ({
  type: actionTypes.LOADED_MINE
})