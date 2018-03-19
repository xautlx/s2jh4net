import * as actionTypes from 'src/actions/actionTypes'

const initialState={
    topay:[],
	all:[],
	activeIndex:"0",		//tab默认选中的index （字符串）
	needRefresh:true,		//是否需要加载   
	historyScrollTop:0,		//listView默认scrollTop
	historyPageIndex:1 		//listVies默认pageIndex
}

export default function auth(state = initialState, action) {
    switch (action.type) {
    	case `${actionTypes.GET_MY_ORDER_TOPAY}_FULFILLED`:
    		return {
				...state,
                topay:action.payload.type==='refresh'?action.payload.data:state.topay.concat(action.payload.data),
				// needRefresh:true,
				// historyScrollTop:0,
				// historyPageIndex:1
			}
    		
    	case `${actionTypes.GET_MY_ORDER_ALL}_FULFILLED`:
    		return {
				...state,
                all:action.payload.type==='refresh'?action.payload.data:state.all.concat(action.payload.data),
				// needRefresh:true,
				// historyScrollTop:0,
				// historyPageIndex:1
			}
		case `${actionTypes.CHANGE_MY_ORDER_ACTIVE_TAB}`:
			return {
				...state,
				activeIndex:action.activeIndex
			}
		case `${actionTypes.CHANGE_MY_ORDER_NEED_REFRESH}`:
			return {
				...state,
				needRefresh:!state.needRefresh
			}
		case `${actionTypes.CHANGE_MY_ORDER_HISTORY_SCROLLTOP}`:
			return {
				...state,
				historyScrollTop:action.historyScrollTop,
				historyPageIndex:action.historyPageIndex
			}
        default:
            return state;
    }
}