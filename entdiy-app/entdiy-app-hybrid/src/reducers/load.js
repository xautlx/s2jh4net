import * as actionTypes from 'src/actions/actionTypes'

// 此处判断页面是否已初次加载，若加载后再次返回相关页面则不重新加载数据
// 此state不记录在持久化中

const initialState={
	home:false,			//首页
	order:false,		//我的订单
	mine:false 			//个人中心
}

export default function auth(state = initialState, action) {
    switch (action.type) {
    	case actionTypes.LOADED_HOME:
    		return {
				...state,
				home:true
    		}

    	case actionTypes.LOADED_ORDER:
    		return {
				...state,
				order:true
    		}

        case actionTypes.LOADED_MINE:
            return {
                ...state,
                mine:true
            }

        default:
            return state;
    }
}