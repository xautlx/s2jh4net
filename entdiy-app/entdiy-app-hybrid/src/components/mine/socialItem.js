import React,{Component} from 'react'
import './socialItem.less'
import moment from 'moment'

class APP extends Component{
	onRightClick(e){
		e.stopPropagation();
		this.props.onRecieveClick();
	}
	
	handleClick(e){
		e.stopPropagation();
		this.props.onClick();
	}

	render(){
		const {data}=this.props;
		return (
			<div className="socialItem" onClick={(e)=>!data.url?this.handleClick(e):null}>
				<div className="title">{data.title}</div>
				{
					data.url&&(
						<div>相关链接:<span className="url" onClick={(e)=>this.handleClick(e)}>{data.url}</span></div>
					)
				}
				<div>分享日期:{moment.unix(data.created_at).format('YYYY-MM-DD')}</div>
				<div>分享内容:{data.content}</div>
			</div>
		)
	}
}

export default APP;