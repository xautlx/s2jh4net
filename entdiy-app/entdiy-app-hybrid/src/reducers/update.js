import * as actionTypes from 'src/actions/actionTypes'

const initState = {
  info: {},
}

export default function reducer(state = initState, action) {
  switch (action.type) {
    case `${actionTypes.UPDATE_INFO_FETCH}_FULFILLED`: {
      return {
        ...state,
        info: action.payload,
      }
    }

    case actionTypes.UPDATE_INFO_CHANGE: {
      return {
        ...state,
        info: {
          ...state.info,
          ...action.payload,
        }
      }
    }

    default:
      return state
  }
}