import React from 'react'
import { NavBar } from 'antd-mobile'
import { showLoading, hideLoading } from 'react-redux-loading-bar'
import store from 'src/store' 
import util from 'src/utils/util'

export default class Iframe extends React.Component {

  constructor (props) {
    super(props)
    this.load = false
    this.state = {
      url: decodeURIComponent(props.match.params.url),
      title: util.parseSearch(props.location.search).title || '',
    }
  }

  componentWillUnmount() {
    if (!this.load) {
      store.dispatch(hideLoading())
    }
  }

  componentDidMount () {
    store.dispatch(showLoading())
    this._ref.onload = () => {
      store.dispatch(hideLoading())
      this.load = true
    }
  }

  render () {
    const style = {
      width: '100%',
      height: '100%',
      border: 'none',
      overflow: 'hidden',
      background: '#ececec',
    }

    const box = {
      position: 'absolute',
      top: util.navbarHeight,
      left: '0',
      right: '0',
      bottom: '0',
      margin: 'auto',
    }



    return (
      <div>
        <NavBar onLeftClick={e=>history.back()}>{this.state.title}</NavBar>
        <div style={box}>
          <iframe className="common-iframe"
            ref={ref=>this._ref=ref} 
            // eslint-disable-next-line
            style={style} 
            src={this.state.url}
            />
        </div>
      </div>
    )
  }
}

