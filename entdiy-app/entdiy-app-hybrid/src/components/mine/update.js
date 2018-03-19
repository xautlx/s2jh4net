import React from 'react'
import { connect } from 'react-redux'
import { Modal } from 'antd-mobile'
import { bindActionCreators } from 'redux'
import * as actions from 'src/actions/update'

class Update extends React.Component {

  componentDidMount () {
    if (window.cordova) {
      window.cordova.getAppVersion.getVersionNumber().then(version => {
        this.props.actions.getUpdateInfo(version)
      })
    }
  }

  render () {
    let { info } = this.props
    let title = `发现新版本: ${info.version}`
    let footer = [
      { text: '取消', onPress: () => { this.onClose(); } },
      { text: '更新', onPress: () => {
        let url = info.update_url
        window.cordova.InAppBrowser.open(url, '_system')
      } },
    ]

    if (info.force_update) {
      footer.shift()
    }

    return (
      <div>
        <Modal
          title={title}
          transparent
          maskClosable={false}
          visible={info.update}
          onClose={this.onClose.bind(this)}
          footer={footer}
          >
            <pre>{info.update_info}</pre>
          </Modal>
      </div>
    )
  }

  onClose () {
    this.props.actions.changeUpdateInfo({ update: false })
  }
}

const mapStateToProps = state => ({
  info: state.update.info,
})

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(actions, dispatch),
})

export default connect(mapStateToProps, mapDispatchToProps)(Update)