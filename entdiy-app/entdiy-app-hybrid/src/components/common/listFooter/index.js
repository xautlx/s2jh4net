import React from 'react'
import { Flex, ActivityIndicator } from 'antd-mobile'

export default class ListFooter extends React.Component {

  static propTypes = {
    hasMore: React.PropTypes.bool.isRequired,
  }

  static defaultProps = {
    hasMore: true,
  }
  
  render () {
    return (
      <Flex align="center" justify="center">
        {this.props.hasMore ? <ActivityIndicator text="加载中..." /> : <span>没有更多数据了</span>}
      </Flex>
    )
  }
}