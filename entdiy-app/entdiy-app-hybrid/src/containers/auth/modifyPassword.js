import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as authActions from 'src/actions/auth'
import {Button, InputItem, List, NavBar, Toast} from 'antd-mobile'

const ListItem = List.Item

class ModifyPassword extends React.Component {
    state = {
        password: '',
        password_new: '',
        password_repeat: '',
    }

    render() {
        let {history} = this.props
        return (
            <div>
                <NavBar onLeftClick={() => history.goBack()}>修改密码</NavBar>
                <List renderHeader={() => '修改密码'}>
                    <InputItem
                        type="password"
                        value={this.state.password}
                        onChange={password => this.setState({password})}
                        placeholder="请输入旧密码">旧密码</InputItem>
                    <InputItem
                        type="password"
                        value={this.state.password_new}
                        onChange={password_new => this.setState({password_new})}
                        placeholder="请输入新密码">新密码</InputItem>
                    <InputItem
                        type="password"
                        value={this.state.password_repeat}
                        onChange={password_repeat => this.setState({password_repeat})}
                        placeholder="请再次输入新密码">重复新密码</InputItem>
                    <ListItem>
                        <Button type="primary" onClick={this.modifyPassword.bind(this)}>确定修改</Button>
                    </ListItem>
                </List>
            </div>
        )
    }

    modifyPassword() {
        let password = this.state.password.trim()
        let password_new = this.state.password_new.trim()
        let password_repeat = this.state.password_repeat.trim()
        let reg = /^[a-zA-Z0-9_]{6,16}$/

        if (!password || !password_new || !password_repeat) {
            return Toast.fail('密码不能为空')
        }

        if (!reg.test(password_new)) {
            return Toast.fail('新密码格式错误(6-16位字母数字下划线)')
        }

        if (password_new !== password_repeat) {
            return Toast.fail('两次密码不一致')
        }

        this.props.actions.modifyPassword(
            password,
            password_new,
        ).then(() => {
            Toast.info('修改成功')
            history.back()
        }).catch(err => Toast.fail(err.message))
    }
}

const mapStateToProps = state => ({})

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(authActions, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(ModifyPassword)