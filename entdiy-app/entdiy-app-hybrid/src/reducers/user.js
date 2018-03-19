import * as actionTypes from 'src/actions/actionTypes'

const initState = {
    info: {
        account: {}
    }
}


const reducer = (state = initState, action) => {
    switch (action.type) {
        case `${actionTypes.USER_INFO_FETCH}_FULFILLED`:
            return {
                ...state,
                info: action.payload,
            }

        case `${actionTypes.USER_INFO_AVATAR_UPDATE}_FULFILLED`:
            return {
                ...state,
                info: {
                    ...state.info,
                    headPhotoUrl: action.payload,
                }
            }

        case actionTypes.USER_INFO_UPDATE:
            return {
                ...state,
                info: {
                    ...state.info,
                    ...action.payload,
                }
            }

        default:
            return state
    }
}

export default reducer