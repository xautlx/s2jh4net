import React from 'react'
import { NavBar, TextareaItem, Button, WingBlank, WhiteSpace, Toast } from 'antd-mobile'
import { showLoading, hideLoading } from 'react-redux-loading-bar'
import store from 'src/store' 
import './index.less'

export default class APP extends React.Component {
  constructor(){
    super()
    this.state={
      textValue:''
    }
  }

  // 分享
  share () {
  	const val = this.state.textValue.trim();
    if (!val) {
      return Toast.fail('分享内容不能为空')
    }
    store.dispatch(showLoading());
  	this.props.share(val).then(()=>{
  		store.dispatch(hideLoading());
  		Toast.success('分享成功',1.5)
  	}).catch(err=>{
  		Toast.fail(err.message);
  		throw err
  	})
  }

  render () {
  	const {onLeftClick,title} = this.props;
    return (
      <div>
        <NavBar
          onLeftClick={()=>onLeftClick()}
          >分享</NavBar>
        <div className="share-page">
          <WingBlank>
            <h4 className="title">{title}</h4>
            <TextareaItem
              placeholder="说点什么吧"
              rows={8}
              value={this.state.textValue}
              onChange={(val)=>this.setState({
                textValue:val
              })}
            />
            <WhiteSpace size='lg' />
            <Button type="primary" onClick={()=>this.share()}>分享到社交圈</Button>
          </WingBlank>
        </div>
      </div>
    )
  }
}