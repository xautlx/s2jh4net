import React from 'react'
import Image from 'src/components/common/image'

const avatar_male = require('./avatar_male.jpeg')
const avatar_female = require('./avatar_female.jpeg')

export default class UserAvatar extends React.Component {
  static defaultProps = {
    type: '',
    src: '',
    gender: '',
  }

  static propTypes = {
    type: React.PropTypes.string,
    src: React.PropTypes.string,
    gender: React.PropTypes.string.isRequired,
    className: React.PropTypes.string,
  }

  render () {
    let { type, src, gender, className } = this.props
    let isMale = gender !== '女' && gender !== 'F'
    let defaultSrc = isMale ? avatar_male : avatar_female

    return (
      <Image className={className} type={type} src={src} defaultSrc={defaultSrc} />
    )
  }
}