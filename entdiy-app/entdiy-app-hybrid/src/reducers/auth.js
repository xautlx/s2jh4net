import * as actionTypes from 'src/actions/actionTypes'

export default function auth(state = {}, action) {
    switch (action.type) {
    	case `${actionTypes.AUTH_LOGIN}_FULFILLED`:
    		return action.payload
    	case `${actionTypes.AUTH_LOGIN_OUT}`:
    		return {}
        default:
            return state;
    }
}