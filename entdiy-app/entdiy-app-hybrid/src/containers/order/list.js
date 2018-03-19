import React from 'react'
import {Tabs} from 'antd-mobile'
import {connect} from 'react-redux'
import { Link } from 'react-router-dom'
import Tabars from 'src/components/tabs'
import Toolbar from 'src/components/home/toolbar'
import utils from 'src/utils/util'
import ListView from 'src/components/common/list/listView'

import './list.less'
import * as actions from 'src/actions/orderList'

function handleTabClick(key) {
    console.log('onTabClick', key);
}

/*
	在组件销毁时缓存了当前activeIndex的listView的scrollTop和pageIndex进store中
	所有tab切换都讲 needRefresh 重置为true
*/

class APP extends React.Component {
    changeActiveTab(key) {
        const {dispatch, data} = this.props;
        dispatch(actions.changeActiveTab(key))
        if (!data.needRefresh) {
            dispatch(actions.changeNeedRefresh())
        }
    }


    //组件销毁时，缓存当前离开时的scrollTop及当前listView的pageIndex，并设置needRefresh为false，下次进入时默认不加载，并保持离开状态，若需刷新，进入前请dispatch为true
    componentWillUnmount() {
        const {dispatch, data} = this.props;
        if (data.needRefresh) {
            dispatch(actions.changeNeedRefresh())
        }

        dispatch(actions.changeHistoryScrollTop(
            this.refs['lv' + data.activeIndex].refs.lv.refs.listview.scrollProperties.offset,	//取组件下listView的scrollTop值
            this.refs['lv' + data.activeIndex].pageIndex   //取组件下listView（自定义）当前的pageIndex
        ))
    }

    //渲染每一条数据
    renderRow(data) {
        const nodes=(
            <div className="order-item" onClick={data.onclick}>
                {
                    data.tips&&(
                        <span className="type">{data.tips}</span>
                    )
                }
                <h3 className="title">{data.title}</h3>
                <p className="date">{data.subtitle}</p>
                <p className="intro">{data.content}</p>
            </div>
        )
        return data.linkData?<Link to={data.linkData}>{nodes}</Link>:nodes;
    }

    render() {
        const {dispatch, data, history} = this.props;

        const listViewHeight = document.documentElement.clientHeight - utils.navbarHeight * 2 - utils.rootFontSize;  //listView高度
        const pageLength = 10;	//每页列表条数

        return (
            <Tabars history={history} selectedKey={1}>
                <Toolbar history={history} title={'订单列表'}>
                    <Tabs
                        defaultActiveKey={data.activeIndex}
                        onChange={(index) => this.changeActiveTab(index)}
                        pageSize={2}
                        onTabClick={handleTabClick}
                        swipeable={false}
                    >
                        <Tabs.TabPane tab={"待支付"} key={0}>
                            <ListView
                                ref="lv0"
                                dataSource={data.topay}
                                renderRow={(data) => this.renderRow(data)}
                                height={listViewHeight}
                                pageLength={pageLength}
                                getDefaultData={() => dispatch(actions.getMyOrder('topay', 'refresh', 1))}
                                getData={(pageIndex) => dispatch(actions.getMyOrder('topay', 'add', pageIndex))}
                                needRefresh={!(data.activeIndex === "0" && data.needRefresh === false)}
                                historyScrollTop={data.historyScrollTop}
                                historyPageIndex={data.historyPageIndex}
                            />
                        </Tabs.TabPane>

                        <Tabs.TabPane tab={"所有订单"} key={1}>
                            <ListView
                                ref="lv1"
                                dataSource={data.all}
                                renderRow={(data) => this.renderRow(data)}
                                height={listViewHeight}
                                pageLength={pageLength}
                                getDefaultData={() => dispatch(actions.getMyOrder('all', 'refresh', 1))}
                                getData={(pageIndex) => dispatch(actions.getMyOrder('all', 'add', pageIndex))}
                                needRefresh={!(data.activeIndex === "1" && data.needRefresh === false)}
                                historyScrollTop={data.historyScrollTop}
                                historyPageIndex={data.historyPageIndex}
                            />
                        </Tabs.TabPane>
                    </Tabs>
                </Toolbar>
            </Tabars>
        )
    }
}


//数据转coreseItem公用组件接收的props
function dataToProps(data) {
    return data.map((child) => {
        return {
            title: child.orderNo,
            subtitle: child.submitTime,
            content: child.orderTitle,
            tips: child.payTime ? '已支付' : '待支付',
            linkData: {
                pathname: `/order/${child.id}`,
                state: {
                    orderNo: child.orderNo
                }
            }
        }
    })
}

const select = (state) => {
    const {orderList} = state;
    return {
        data: {
            ...orderList,
            topay: dataToProps(orderList.topay || []),
            all: dataToProps(orderList.all || [])
        }
    }
}

export default connect(select)(APP);