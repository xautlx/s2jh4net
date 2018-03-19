import {createStore, applyMiddleware, compose} from 'redux'
import thunk from 'redux-thunk'
import {createLogger} from 'redux-logger'
import promiseMiddleware from 'redux-promise-middleware';
import {loadingBarMiddleware} from 'react-redux-loading-bar';
import reducers from './reducers';

// redux store 持久化
// 参考：https://github.com/rt2zz/redux-persist
import {autoRehydrate} from 'redux-persist'

const isDev = process.env.NODE_ENV !== 'production'

let logger = createLogger({
  collapsed: true,
  duration: true,
  predicate: (getState, action) => {
    if (isDev) {
      return !/(^loading-bar\/)|(_PENDING$)/.test(action.type)
    } else {
      return false
    }
  },
})

const store = createStore(
  reducers,
  compose(
    applyMiddleware(
      thunk,
      promiseMiddleware(),
      loadingBarMiddleware(),
      logger,
    ),
    autoRehydrate()
  )
)

export default store

