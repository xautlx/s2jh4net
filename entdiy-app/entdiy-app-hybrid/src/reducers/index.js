import {combineReducers} from 'redux';
import {loadingBarReducer} from 'react-redux-loading-bar';

import orderDetail from './orderDetail'
import orderList from './orderList'

import auth from './auth'
import auth2 from './auth2'
import user from './user'
import message from './message'
import load from './load'
import update from './update'

const reducers = combineReducers({
	auth,
  	auth2,
	orderList,
	orderDetail,
 	user,
    message,
    load,
    update,
	loadingBar: loadingBarReducer
})

export default reducers;