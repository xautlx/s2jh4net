import React from 'react'

export default class Image extends React.Component {
  static propTypes = {
    src: React.PropTypes.string,
    defaultSrc: React.PropTypes.string,
    className: React.PropTypes.string,
  }

  constructor (props) {
    super(props)
    this.state = {
      src: props.defaultSrc,
    }

    this.handleImgLoad(props.src)
  }

  componentWillReceiveProps (props) {
    if (props.src !== this.props.src) {
      this.handleImgLoad(props.src)
    }
  }
 
  render () {
    let { type, className = 'common-img' } = this.props
    let { src } = this.state

    if (type === 'bgi') {
      return <div className={className} style={{ 'backgroundImage': `url(${src})` }} />
    }

    return (
      <img className={className} src={src} alt="" />
    )
  }

  handleImgLoad (src) {
    var img = document.createElement('IMG')
    img.onload = () => { this.setState({ src: src }); img = null }
    img.onerror = () => { img = null }
    img.src = src
  }
}