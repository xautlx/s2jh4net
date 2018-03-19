import React from 'react'
import {connect} from 'react-redux'
import {loginOut, setGpass} from 'src/actions/auth'
import {Button, List, Modal, NavBar, Switch, Toast, WhiteSpace, WingBlank} from 'antd-mobile'

const ListItem = List.Item

class Setting extends React.Component {
    state = {
        size: 0
    }

    render() {
        let {history} = this.props

        return (
            <div>
                <NavBar
                    onLeftClick={() => history.goBack()}
                >设置</NavBar>
                <WhiteSpace size="lg"/>
                <List>
                    <ListItem arrow="horizontal" onClick={()=>history.push('/password/modify')}>修改密码</ListItem>
                    {/*
          <ListItem extra={<Switch onClick={this.toggleGpass.bind(this)} platform="ios" checked={this.props.isGpassed} />}>手势密码</ListItem>
          */}
                </List>
                <WhiteSpace size="lg"/>
                <List>
                    <ListItem arrow="horizontal" onClick={this.clearCache.bind(this)}>清除缓存</ListItem>
                </List>
                <WhiteSpace size="lg"/>
                <List>
                    <ListItem onClick={()=>history.push('/about')} arrow="horizontal">关于</ListItem>
                </List>
                <WhiteSpace size="xl"/>
                <WingBlank>
                    <Button onClick={() => this.loginOut()}>注销退出</Button>
                </WingBlank>
            </div>
        )
    }

    loginOut() {
        this.props.loginOut();
        this.props.history.push('/login')
    }

    toggleGpass() {
        let {history, isGpassed} = this.props

        if (isGpassed) {
            this.props.setGpass('')
        } else {
            history.push('/password/gesture')
        }
    }

    clearCache() {
        if (!window.cordova) return

        window.resolveLocalFileSystemURL('cdvfile://localhost/temporary/expfile', (entry) => {
            this.countSize(entry).then(size => {
                Modal.alert(`总计：${Math.round(size / 1024 / 1024)}MB`, '确定要清除缓存吗？', [
                    {
                        text: '取消', onPress: () => {
                        }
                    },
                    {
                        text: '确定', onPress: () => {
                            entry.removeRecursively()
                            Toast.info('清除成功')
                        }
                    },
                ])
            }).catch(err => alert(err))
        }, (err) => {
            if (err.code === 1) {
                Toast.info('暂无缓存，无需清除')
            } else {
                Toast.fail('错误代码：' + err.code)
            }
        })

    }

    // 计算大小
    countSize(entry) {
        if (entry.isFile) {
            return new Promise((resolve, reject) => {
                entry.getMetadata(f => resolve(f.size), err => reject(err))
            })
        }
        if (entry.isDirectory) {
            return new Promise((resolve, reject) => {
                var reader = entry.createReader()
                reader.readEntries(entries => {
                    Promise.all(entries.map(e => this.countSize(e))).then(size => {
                        var dirsize = size.reduce((prv, crt) => prv + crt, 0)
                        resolve(dirsize)
                    }).catch(err => reject(err))
                }, err => reject(err))
            })
        }
    }
}

const mapStateToProps = state => ({
    isGpassed: !!state.auth2.gpass
})

const mapDispatchToProps = dispatch => ({
    setGpass: pass => dispatch(setGpass(pass)),
    loginOut: () => dispatch(loginOut())
})

export default connect(mapStateToProps, mapDispatchToProps)(Setting)