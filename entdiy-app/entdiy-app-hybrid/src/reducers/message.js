import * as actionTypes from 'src/actions/actionTypes'

const initialState={
	count:0
}

export default function auth(state = initialState, action) {
    switch (action.type) {
    	case `${actionTypes.MESSAGE_GET_COUNT}_FULFILLED`:
    		return {
    			...state,
    			count:action.payload
    		}

        default:
            return state;
    }
}