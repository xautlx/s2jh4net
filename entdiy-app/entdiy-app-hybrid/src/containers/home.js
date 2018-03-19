import React from 'react'
import {connect} from 'react-redux'
import {WhiteSpace} from 'antd-mobile'
import HomeCarousel from 'src/components/home/homeCarousel'
import Toolbar from 'src/components/home/toolbar'
import {getMessageCount} from 'src/actions/message'
import Tabars from 'src/components/tabs'
import {homeLoaded} from 'src/actions/load'
import Update from 'src/components/mine/update'

class Home extends React.Component {
    componentDidMount() {
        //如果页面未初始化加载，则执行请求加载数据，完成后更新首页load状态
        //若已初始化加载，则不执行任何操作
        if (!this.props.loaded) {
            this.props.getMessageCount();
            this.props.homeLoaded();
        }
    }

    render() {
        let {history} = this.props
        return (
            <Tabars selectedKey={0} history={history}>
                <Toolbar history={history} title="首页">
                    <div>
                        <HomeCarousel/>
                        <WhiteSpace/>
                        <Update/>
                    </div>
                </Toolbar>
            </Tabars>
        )
    }
}

const mapStateToProps = state => {
    return {
        loaded: state.load.home
    }
}

const mapDispatchToProps = dispatch => ({
    getMessageCount: () => dispatch(getMessageCount()),
    homeLoaded: () => dispatch(homeLoaded())
})

export default connect(mapStateToProps, mapDispatchToProps)(Home)