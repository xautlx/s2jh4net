import React from 'react'
import AraleQRCode from 'arale-qrcode'

// https://github.com/aralejs/qrcode
export default class MyQrCode extends React.Component {

  componentDidMount () {
    this._ref.appendChild(new AraleQRCode({...this.props}))
  }

  render () {
    return (
      <div ref={ref=>this._ref=ref} />
    )
  }
}