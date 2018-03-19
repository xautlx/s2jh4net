import React from 'react'
import { NavBar } from 'antd-mobile'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import * as actions from 'src/actions/update'
import './about.less'

class About extends React.Component {
  state = {
    name: 'EntDIY APP',
    version: '0.0.0',
  }

  constructor (props) {
    super(props)
    if (window.cordova) {
      window.cordova.getAppVersion.getVersionNumber().then(version => {
        this.setState({ version })
        this.props.actions.getUpdateInfo(version)
      })
    }
  }

  render () {
    let { history } = this.props

    return (
      <div className="page-about">
        <NavBar
          onLeftClick={()=>history.goBack()}
          >关于</NavBar>
        <div className="head">
          <img className="bg" src={require('src/images/about_bckground.png')} alt=""/>
          <img className="logo" src={require('src/images/about.png')} alt=""/>
        </div>
        <div className="info">
          <h3 className="title">{this.state.name}</h3>
          <p className="ver">Version: {this.state.version}</p>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  // info: state.update.info,
})

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(actions, dispatch),
})

export default connect(mapStateToProps, mapDispatchToProps)(About)