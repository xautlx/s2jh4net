import * as actionTypes from 'src/actions/actionTypes'

const initState = {
  gpass: '',
}

export default function auth(state = initState, action) {
  switch (action.type) {
    case actionTypes.GESTURE_PASSWORD_SET:
      return {
        ...state,
        gpass: action.payload
      }

    default:
      return state
  }
}