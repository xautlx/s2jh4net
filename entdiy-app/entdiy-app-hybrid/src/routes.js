import React from 'react'
import {HashRouter as Router, Redirect, Route, Switch} from 'react-router-dom'
import CSSTransitionGroup from 'react-transition-group/CSSTransitionGroup'
import store from 'src/store'

import Todo from 'src/containers/todo'
import Login from 'src/containers/auth/login'
import Home from 'src/containers/home'
import OrderList from 'src/containers/order/list'
import OrderDetail from 'src/containers/order/detail'
import Iframe from 'src/components/common/iframe'
import Mine from 'src/containers/mine/mine'
import Profile from 'src/containers/mine/profile'
import Setting from 'src/containers/mine/setting'
import ModifyPassword from 'src/containers/auth/modifyPassword'
import About from 'src/containers/mine/about'

export default class Routes extends React.Component {
    render() {
        return (
            <Router>
                <Route render={({location}) => (
                    <CSSTransitionGroup
                        transitionName="page"
                        transitionEnterTimeout={300}
                        transitionLeaveTimeout={300}
                    >
                        <Switch location={location} key={location.key}>
                            <Route exact path="/login" component={Login}/>
                            <Route exact path="/password/reset" component={ModifyPassword}/>

                            <Route exact path="/todo" component={Todo}/>

                            <PrivateRoute exact path="/iframe/:url" component={Iframe}/>

                            <PrivateRoute exact path="/password/modify" component={ModifyPassword}/>

                            <PrivateRoute exact path="/" component={Home}/>

                            <PrivateRoute exact path="/order" component={OrderList}/>
                            <PrivateRoute exact path="/order/:id" component={OrderDetail}/>

                            <PrivateRoute exact path="/mine" component={Mine}/>
                            <PrivateRoute exact path="/mine/profile" component={Profile}/>
                            <PrivateRoute exact path="/mine/setting" component={Setting}/>

                            <PrivateRoute exact path="/about" component={About}/>

                            <Redirect to="/"/>
                        </Switch>
                    </CSSTransitionGroup>
                )}/>
            </Router>
        )
    }
}

const PrivateRoute = ({component: Component, ...rest}) => (
    <Route {...rest} render={props => (
        JSON.stringify(store.getState().auth) !== '{}' ? (
            <Component {...props}/>
        ) : (
            <Redirect to={{
                pathname: '/login',
                state: {from: props.location}
            }}/>
        )
    )}/>
)