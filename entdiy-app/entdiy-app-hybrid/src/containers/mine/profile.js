import React from 'react'
import objectPath from 'object-path'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as actions from 'src/actions/user'
import {DatePicker, List, Modal, NavBar, Toast} from 'antd-mobile'
import moment from 'moment'

const ListItem = List.Item

class Profile extends React.Component {
    componentDidMount() {
        let {actions} = this.props
        actions.getUserInfo({})
    }

    render() {
        let {userInfo, history} = this.props

        return (
            <div>
                <NavBar onLeftClick={() => history.goBack()}>个人信息</NavBar>

                <List renderHeader={() => '基础信息'}>
                    <ListItem arrow="empty" extra={userInfo.account.authUid}>登录账号</ListItem>
                    <ListItem arrow="horizontal" extra={userInfo.gender === 'F' ? '女' : userInfo.gender === 'M' ? '男' : '保密'}
                              onClick={this.setSex.bind(this)}>性别</ListItem>
                    <DatePicker
                        mode="date"
                        value={moment(userInfo.birthDay || Date.now())}
                        minDate={moment('1900-01-01')}
                        maxDate={moment()}
                        onChange={this.setDate.bind(this)}
                    >
                        <ListItem arrow="horizontal">生日</ListItem>
                    </DatePicker>
                    <ListItem arrow="horizontal" extra={userInfo.account.email} onClick={this.handePrompt.bind(this, 'account.email', '修改邮箱')}>邮箱</ListItem>
                    <ListItem arrow="horizontal" extra={userInfo.account.mobile} onClick={this.handePrompt.bind(this, 'account.mobile', '修改手机号')}>手机号</ListItem>
                </List>

            </div>
        )
    }

    updateUser(type, text) {
        let {userInfo} = this.props
        if (!text || objectPath.get(userInfo, type) === text) {
            return
        }

        this.props.actions.updateUserInfoByAjax({
            [type]: text
        })

    }

    handePrompt(type, title) {
        Modal.prompt(
            title,
            null,
            (text) => {
                text = text.trim()

                if (type === 'account.email' && !/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test(text)) {
                    return Toast.fail('邮箱格式错误')
                }

                if (type === 'account.mobile' && !/^1\d{10}$/.test(text)) {
                    return Toast.fail('手机格式错误')
                }

                this.updateUser(type, text)
            }
        )
    }

    setSex() {
        Modal.alert(
            '修改性别',
            null,
            [
                {
                    text: '男', onPress: () => {
                        this.updateUser('gender', 'M')
                    }
                },
                {
                    text: '女', onPress: () => {
                        this.updateUser('gender', 'F')
                    }
                },
                {
                    text: '保密', onPress: () => {
                        this.updateUser('gender', 'S')
                    }
                },
                {text: '取消'}
            ]
        )
    }

    setDate(date) {
        this.updateUser('birthDay', date.format('YYYY-MM-DD'))
    }
}

const mapStateToProps = state => ({
    userInfo: state.user.info,
})

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(actions, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(Profile)