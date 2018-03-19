import React from 'react'
import {Icon, NavBar, Popover, Toast} from 'antd-mobile'
import utils from 'src/utils/util'
import {connect} from 'react-redux'

/*
    Toobar为一特殊组件，内部扫描二维码后有fetch请求，并连接了redux取userId
    正常不应该在此写相关代码
*/

class Toolbar extends React.Component {
    static propTypes = {
        title: React.PropTypes.string.isRequired,
    }

    state = {
        visible: false
    }

    hanldeSelect(opt) {
        let key = opt.props.value
        this.setState({visible: false})
        const {history} = this.props;
        switch (key) {

            case 'scan':
                utils.scanQrcode((res) => {
                    try {
                        if (res.indexOf('http://') > -1 || res.indexOf('https://') > -1) {
                            history.push(`/iframe/${encodeURIComponent(res)}?title=${encodeURIComponent('扫一扫')}`);
                            return;
                        }

                        const result = JSON.parse(res);
                        //JSON数据解析处理
                        console.log(result);
                    } catch (err) {
                        Toast.fail('无效的二维码')
                    }
                });
                break;
            default:
        }
    }

    render() {
        let {children, title, history} = this.props
        let overlay = [
            <Popover.Item value="scan">扫一扫</Popover.Item>
        ]

        return (
            <div>
                <NavBar
                    iconName={null}
                    leftContent={<Icon type="search" onClick={e => history.push('/todo')}/>}
                    rightContent={
                        <Popover
                            mask={true}
                            visible={this.state.visible}
                            popupAlign={{
                                overflow: {adjustY: 0, adjustX: 0},
                                offset: [-26, 15],
                            }}
                            overlay={overlay}
                            onSelect={this.hanldeSelect.bind(this)}
                            onVisibleChange={visible => this.setState({visible})}
                        >
                            <div style={{
                                height: '100%',
                                padding: '0 0.3rem',
                                marginRight: '-0.3rem',
                                display: 'flex',
                                alignItems: 'center',
                            }}>
                                <Icon type="ellipsis"/>
                            </div>
                        </Popover>
                    }
                >{title}</NavBar>

                <div>{children}</div>

                <div style={{paddingBottom: '1rem'}}/>
            </div>
        )
    }
}

const select = (state) => {
    return {
        userId: state.auth.user_id
    }
}

export default connect(select)(Toolbar)