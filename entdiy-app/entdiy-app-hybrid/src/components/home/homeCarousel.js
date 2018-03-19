import React from 'react'
import { Carousel } from 'antd-mobile'

export default class HomeCarousel extends React.Component {
  state = {
    height: window.innerWidth / 750 * 372,
  }

  timer=null

  componentDidMount() {
    window.addEventListener('resize', this.resize, false)
  }

  componentWillUnmount() {
    window.removeEventListener('resize', this.resize, false)
  }

  resize = () => {
    this.setState({height: window.innerWidth / 750 * 372})

    if (this.timer===null) {
      this.timer = setTimeout(() => {
        window.dispatchEvent(new Event('resize')) //修复容器高度不会立即刷新的问题
        this.timer = null
      })
    }
  }


  render () {
    let imgStyle = {height: this.state.height}
    return (
      <Carousel
        infinite={true}
        autoplay={true}
        autoplayInterval={5000}
        >
        <img style={imgStyle} src={require('src/images/home_banner1@2x.png')} alt="" />
        <img style={imgStyle} src={require('src/images/home_banner2@2x.png')} alt="" />
        <img style={imgStyle} src={require('src/images/home_banner3@2x.png')} alt="" />
      </Carousel>
    )
  }
}