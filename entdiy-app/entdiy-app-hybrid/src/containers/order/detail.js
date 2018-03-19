import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as actions from 'src/actions/orderDetail'
import {NavBar,Toast} from 'antd-mobile'
import './detail.less'

class OrderDetail extends React.Component {
    componentDidMount () {
        let { actions} = this.props
        let id = this.props.match.params.id

        actions.getDetail(id).catch(err=>{
            Toast.fail(err.message)

        })
    }

    render() {
        let {history, detail} = this.props

        return (
            <div>
                <NavBar
                    onLeftClick={() => history.goBack()}
                >订单详情
                </NavBar>
                <div> {detail.orderNo} </div>
            </div>
        )
    }
}

const mapStateToProps = state => ({
    detail: state.orderDetail.info
})

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(actions, dispatch)
})

export default connect(mapStateToProps, mapDispatchToProps)(OrderDetail)