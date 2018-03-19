import React, {Component} from 'react'
import {NavBar} from 'antd-mobile'
import {connect} from 'react-redux'

class APP extends Component {

    render() {
        const {history} =this.props;

        return (
            <div>
                <NavBar leftContent="返回" onLeftClick={() => history.goBack()}>TODO页面</NavBar>
                <div>TODO</div>
            </div>
        )
    }
}

export default connect()(APP)