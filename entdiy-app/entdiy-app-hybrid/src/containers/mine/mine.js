import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as actions from 'src/actions/user'
import {ActionSheet, Icon, List, Modal, Toast, WhiteSpace} from 'antd-mobile'
import Tabars from 'src/components/tabs'
import Toolbar from 'src/components/home/toolbar'
import util from 'src/utils/util'
import UserAvatar from 'src/components/common/userAvatar'
import QrCode from 'src/components/common/qrcode'
import CropAvatar from 'src/components/mine/cropAvatar'
import './mine.less'
import {mineLoaded} from 'src/actions/load'

class Mine extends React.Component {
    _refs = {}

    state = {
        qrshow: false,
        imgSrc: '',
    }

    componentDidMount() {
        let {actions, loaded} = this.props

        if (!loaded) {
            // 获取用户信息
            actions.getUserInfo({})
                .then(() => {
                    this.props.mineLoaded()
                })
        }


        // 图片上传
        if (!window.cordova) {
            this.bindAvatarEvent()
        }
    }

    // 绑定上传图片事件
    bindAvatarEvent() {
        let reader = new FileReader()
        let ipt = this._refs.ipt

        reader.addEventListener('load', e => {
            this.setState({imgSrc: reader.result})
        })

        ipt.addEventListener('change', function (e) {
            let file = e.target.files[0]
            if (file && /^image\//.test(file.type)) {
                reader.readAsDataURL(file)
            }
        })
    }

    // 关闭头像裁剪
    closeAvatarModal() {
        this._refs.ipt.value = ''
        this.setState({imgSrc: ''})
    }

    // 上传头像
    uploadAvatarByWeb(cropper) {
        cropper.getCroppedCanvas({
            width: 300,
            height: 300,
        }).toBlob(blob => {
            this.closeAvatarModal()
            this.uploadAvatar(blob)
        })
    }

    showActionSheet(evt) {
        evt.stopPropagation()
        if (!window.cordova) return

        ActionSheet.showActionSheetWithOptions({
            options: ['拍照', '从手机相册选择', '取消'],
            cancelButtonIndex: 2,
            // title: '上传头像',
            // message: '选择方式',
            // maskClosable: true,
        }, index => {
            if (index === 0) {
                this.getPicture(1)
            }
            if (index === 1) {
                this.getPicture(0)
            }
        })
    }

    getPicture(sourceType) {
        if (!window.cordova) return
        let camera = window.navigator.camera
        let Camera = window.Camera

        camera.getPicture(
            data => {
                window.resolveLocalFileSystemURL(data, fileEntry => {
                    fileEntry.file(file => {
                        var reader = new FileReader()
                        reader.onload = evt => {
                            var blob = new Blob([evt.target.result], {type: 'image/jpeg'})
                            this.uploadAvatar(blob)
                        }
                        reader.readAsArrayBuffer(file)
                    })
                })
            },
            err => {
                Toast.fail(err)
            },
            {
                quality: 50,
                destinationType: Camera.DestinationType.FILE_URI,
                sourceType: sourceType,
                allowEdit: true,
                targetWidth: 300,
                targetHeight: 300,
            },
        )
    }

    uploadAvatar(blob) {
        let formData = new FormData()
        formData.append('myfile', blob, 'person.jpg')
        Toast.loading('上传中…', 0)
        this.props.actions.uploadAvatar(formData)
            .then(res => Toast.hide())
            .catch(err => Toast.fail(err))
    }

    render() {
        let {history, userInfo, account} = this.props
        let qrText = 'http://qm.qq.com/cgi-bin/qm/qr?k=QRCIGbntTn84m7O3c6qzgNjJV5kytp7i'

        return (
            <Tabars selectedKey={2} history={history}>
                <Toolbar title="我的" history={history}>
                    <div>
                        <WhiteSpace size="lg"/>

                        <List>
                            <List.Item className="userPanel" onClick={e => history.push('/mine/profile')}>
                                <div className="avatar" onClick={this.showActionSheet.bind(this)}>
                                    {!window.cordova && <input onClick={e => e.stopPropagation()} ref={ref => this._refs.ipt = ref} type="file"
                                                               accept="image/png,image/jpeg,image/jpg,image/webp"/>}
                                    <UserAvatar type="bgi" gender={userInfo.gender} src={util.patchUrl(userInfo.headPhotoUrl)}/>
                                </div>
                                <h3 className="nickname">{account.authUid}</h3>
                                <p className="email">{account.email}</p>
                                <Icon type={require('src/images/QRcode.svg')} onClick={e => {
                                    e.stopPropagation()
                                    this.setState({qrshow: true})
                                }} className="qr"/>
                            </List.Item>
                        </List>

                        <WhiteSpace size="lg"/>

                        <List>
                            <List.Item arrow="horizontal" onClick={e => history.push('/todo')}>
                                我的消息
                                {
                                    this.props.msgCount && <span className="mineMessageCount">{this.props.msgCount}</span>
                                }
                            </List.Item>
                        </List>

                        <WhiteSpace size="lg"/>

                        <List>
                            <List.Item arrow="horizontal" onClick={e => history.push('/mine/setting')}>设置</List.Item>
                        </List>

                        <WhiteSpace size="lg"/>

                        <List>
                            <List.Item arrow="horizontal"
                                       onClick={e => history.push(`/iframe/${encodeURIComponent('https://demo.entdiy.com/entdiy/admin')}?title=${encodeURIComponent('移动管理端')}`)}>移动管理端</List.Item>
                        </List>

                        {/* 二维码 */}
                        <Modal
                            className="myQrCode"
                            closable={true}
                            transparent
                            title="EntDIY技术QQ交流群"
                            visible={this.state.qrshow}
                            maskClosable={true}
                            onClose={e => this.setState({qrshow: false})}
                        >
                            <QrCode size={500} text={qrText}/>
                            <p>扫描二维码关注</p>
                        </Modal>

                        {/* 裁剪头像 */}
                        <Modal
                            className="cropAvatarModal"
                            visible={this.state.imgSrc}
                        >
                            <CropAvatar
                                src={this.state.imgSrc}
                                options={{
                                    autoCropArea: .9,
                                    aspectRatio: 1 / 1,
                                    dragMode: 'move',
                                    guides: false,
                                    center: false,
                                    background: false,
                                    cropBoxMovable: false,
                                    cropBoxResizable: false,
                                    zoomOnWheel: false,
                                    toggleDragModeOnDblclick: false,
                                }}
                                onCancel={this.closeAvatarModal.bind(this)}
                                onSelect={this.uploadAvatarByWeb.bind(this)}
                            />
                        </Modal>

                    </div>
                </Toolbar>
            </Tabars>
        )
    }
}


const mapStateToProps = state => ({
    userInfo: state.user.info,
    account: state.user.info.account || {},
    msgCount: state.message.count || null,
    loaded: state.load.mine
})

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(actions, dispatch),
    mineLoaded: () => dispatch(mineLoaded())
})

export default connect(mapStateToProps, mapDispatchToProps)(Mine)