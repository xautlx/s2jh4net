import React, { Component } from 'react'
import { Button, InputItem, Toast,  } from 'antd-mobile'
import {connect} from 'react-redux'
import {login} from 'src/actions/auth'
import './login.less'

class Login extends Component {
  constructor(){
    super()
    this.state={
      username: 'customer',
      password: '123456'
    }
  }
  
  componentWillMount(){
    let { from } = this.props.location.state || { from: { pathname: '/' } }
    if(JSON.stringify(this.props.auth)!=='{}'){
      this.props.history.push(from);
    }
  }

  render () {
    return (
      <div className="page-login" style={{height: document.documentElement.clientHeight}}>
        <div className="center">
          <img className="loginImg" src={require('src/images/app_logo.png')} alt="" />
          <InputItem className="ipt" type="text" value={this.state.username} onChange={username=>this.setState({username})} placeholder="请输入用户名" />
          <InputItem className="ipt" type="password" value={this.state.password} onChange={password=>this.setState({password})} placeholder="请输入密码" />
          <Button className="btn" type="primary" onClick={()=>this.handleLogin()}>立即登录</Button>
          <div style={{height: '2rem'}} />
        </div>
      </div>
    )

  }

  handleLogin () {
    let username = this.state.username.trim()
    let password = this.state.password.trim()

    if (!username || !password) {
      return Toast.fail('用户名或密码不能为空')
    }

    const {dispatch}=this.props;
    Toast.loading('登录中',9999)

    dispatch(login(username,password))
    .then(()=>{
      Toast.hide();
      this.props.history.push('/aq');
    })
    .catch(err => Toast.fail(err.message))
  }
}

function select(state){
    return {
      auth:state.auth
    }
}

export default connect(select)(Login)