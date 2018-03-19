import React from 'react'
import { TabBar,Toast } from 'antd-mobile'
import './index.less'
import {connect} from 'react-redux'

/*
  tabs为一个特殊组件，内部与store中message连接，并抛出了组件
  此做法不适用所有组件,connect请尽量在container中使用
*/

class Tabs extends React.Component {
  constructor(){
    super()
    //用户第一次点击退出的时间
    this.firstExitTime=0;

    //监听用户点击退出键，若两秒内连续点击两次，则退出APP
    if(navigator.app){
      document.on('backbutton',()=>{
        if(this.firstExitTime&&this.firstExitTime+2000>new Date().getTime()){
          navigator.app.exitApp();
        }else{
          this.setFirstExitTime()
        }
      })
    }
  }
  
  //设置用户第一次点击退出键的时间，作双击退出用
  setFirstExitTime(){
    this.firstExitTime=new Date().getTime();
    Toast.info('再点一下退出程序')
  }
  
  //组件销毁时取消双击退出的事件
  componentWillUnmount(){
    if(navigator.app){
      document.off('backbutton')
    }
  }

  render () {
  	const {selectedKey,children,history,msgCount} = this.props;
    return (
      <TabBar>
        <TabBar.Item
          key="home"
          title="首页"
          selected={selectedKey===0}
          onPress={()=>history.replace('/')}
          icon={ <img src={require('src/images/Tabbar_home@2x.png')} style={{width:'.4rem',height:'.4rem'}} alt="order"/> }
          selectedIcon={ <img src={require('src/images/slider_undone_s@2x.png')} style={{width:'.4rem',height:'.4rem'}} alt="home"/> }
          >
          {children}
        </TabBar.Item>
        <TabBar.Item
          key="order"
          title="订单"
          selected={selectedKey===1}
          onPress={()=>history.replace('/order')}
          icon={ <img src={require('src/images/Tabbar_order@2x.png')} style={{width:'.4rem',height:'.4rem'}} alt="order"/> }
          selectedIcon={ <img src={require('src/images/Tabbar_order_s@2x.png')} style={{width:'.4rem',height:'.4rem'}} alt="order"/> }
          >
          {children}
        </TabBar.Item>
        <TabBar.Item
          key="my"
          title="我的"
          selected={selectedKey===2}
          icon={
            <div className="tabsUser">
              {
                msgCount&&<span className="bico">{msgCount}</span>
              }
              <img src={require('src/images/Tabbar_my@2x.png')} style={{width:'.4rem',height:'.4rem'}} alt="my"/>
            </div> 
          }
          selectedIcon={ 
            <div className="tabsUser">
              {
                msgCount&&<span className="bico">{msgCount}</span>
              }
              <img src={require('src/images/Tabbar_my_s@2x.png')} style={{width:'.4rem',height:'.4rem'}} alt="home"/>
            </div> 
          }
          onPress={()=>history.replace('/mine')}
          >
          {children}
        </TabBar.Item>
      </TabBar>
    )
  }
}


function select(state){
    return {
      msgCount:state.message.count||null
    }
}

export default connect(select)(Tabs)