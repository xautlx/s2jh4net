import React from 'react'
import Routes from 'src/routes'
import {Provider} from 'react-redux'
import store from 'src/store'
import LoadingBar from 'react-redux-loading-bar';
import {persistStore} from 'redux-persist'
import transform from 'src/storeFilter'

const subBlackList = {
	// orderDetail: ['info', 'like'],
	questionList:['needRefresh','historyScrollTop','historyPageIndex','activeIndex'],
	orderList:['needRefresh','historyScrollTop','historyPageIndex','activeIndex'],
	experience:['needRefresh','historyScrollTop','historyPageIndex','activeIndex'],
	task:['needRefresh','historyScrollTop','historyPageIndex'],
	newsDynamic:['needRefresh','historyScrollTop','historyPageIndex'],
	questionDirectory:['needRefresh','historyScrollTop','historyPageIndex','activeIndex'],
	//orderList2:['needRefresh','historyScrollTop','historyPageIndex'],
	socialDynamic:['needRefresh','historyScrollTop','historyPageIndex'],
}

export default class Root extends React.Component {
	constructor () {
	    super()
	    this.state = {
	      isReady: false
	    }
	    
	    persistStore(store, {
	      blacklist: ['loadingBar', 'follow', 'message','load', 'search', 'update','orderList2','ask'],
	      transforms: [transform(subBlackList)],
	    }, () => {
	      this.setState({ isReady: true })
	    })
	}
	
	render () {
		if (!this.state.isReady) {
	      return null
	    }
	    
	    return (
			<Provider store={store} >
				<div>
	        		<LoadingBar className="custom-loading-bar"/>
	        		<Routes />
				</div>
			</Provider>
	    )
	}
}