import * as actionTypes from 'src/actions/actionTypes'

const initState = {
  info: {
    order: {}
  },
  activeTabIndex:'0'
}

const orderDetail = (state = initState, action) => {
  switch (action.type) {

    case actionTypes.ORDER_DETAIL_RESET:
      return {
        ...state,
        info: {
          order: {}
        },
        ...action.payload,
      }

    case `${actionTypes.ORDER_DETAIL_FETCH}_FULFILLED`:
      return {
        ...state,
        info: action.payload,
      }

    default: 
      return state
  }
}

export default orderDetail