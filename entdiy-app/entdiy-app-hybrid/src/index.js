import React from 'react'
import ReactDOM from 'react-dom'
import Root from './containers/root'
import 'animate.css'
import './styles/core.less'
// antd-mobile的依赖项 #576

require('fastclick').attach(document.body)

const startApp = () => {
  ReactDOM.render(<Root />, document.getElementById('root'))
}

//为document添加一个数组，存放'on'方法绑定的事件及其执行函数
document.onEventsArray=[];

//document.on方法定义，类似jquery的on, 将事件方法存放至document.onEventsArray中，并用addEventListener绑定事件
document.on=function(event,fn){
	let eventArr=document.onEventsArray;
	const boundEvent=eventArr.filter((child)=>event===child.event);
	if(boundEvent.length){
		boundEvent[0].fns.push(fn)
	}else{
		eventArr.push({
			event,
			fns:[fn]
		})
	}
	document.addEventListener(event,fn,false);
}

//类似jquery off方法，取消该事件下的全部绑定函数
document.off=function(event){
	let eventArr=document.onEventsArray;
	eventArr.forEach((child,i)=>{
		if(event!==child.event)return;
		child.fns.forEach((chi)=>{
			document.removeEventListener(event,chi,false)
		})
		eventArr.splice(i,1)
	})
}

if (!window.cordova) {
  startApp();
} else {
  document.addEventListener('deviceready', startApp, false);
}

