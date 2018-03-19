import React from 'react'
import Cropper from 'cropperjs'
import 'cropperjs/dist/cropper.min.css'
import './cropAvatar.less'

export default class CropAvatar extends React.Component {
  _ref = null
  cropper = null

  static defaultProps = {
    src: '',
    options: {},
    onCancel: () => {},
    onSelect: () => {},
  }

  static propTypes = {
    src: React.PropTypes.string,
    options: React.PropTypes.object,
    onCancel: React.PropTypes.func,
    onSelect: React.PropTypes.func,
  }

  componentWillReceiveProps (props) {
    this.cropper.replace(props.src)
  }

  componentDidMount () {
    this.cropper = new Cropper(this._ref, this.props.options)
  }

  render () {
    let { onCancel, src, onSelect } = this.props

    return (
      <div className="cropAvatarBox" style={{height: window.innerHeight}}>
        <img style={{maxWidth: '100%'}} ref={ref=>this._ref=ref} src={src} alt=""/>
        <div className="foot-actions">
          <span onClick={onCancel}>取消</span>
          <span onClick={e=>onSelect(this.cropper)}>确定</span>
        </div>
      </div>
    )
  }
}