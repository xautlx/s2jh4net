import {createTransform} from 'redux-persist'

function filter (state, keys) {
  let _state = { ...state }
  keys.forEach(key => delete _state[key])
  return _state
}


export default function (blacklist) {

  return createTransform(
    (state, key) => state,
    (state, key) => blacklist[key] ? filter(state, blacklist[key]) : state
  )
}