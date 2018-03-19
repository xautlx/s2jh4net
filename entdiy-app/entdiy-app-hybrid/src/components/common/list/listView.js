import React,{Component} from 'react'
import {RefreshControl,ListView,Result, Icon } from 'antd-mobile'
import PropTypes from 'prop-types';

/*

@ 向上暴露onRefresh方法，参数为(needRefreshing,promise);
@ 在组件不销毁的前提下，若要重置index、hasnext等数据（类似筛选等“切换列表所属类别”的场景）时，调用此方法
@ 第一参数为是否需要刷新动作，若不需要则传null值
@ 第二参数为请求数据的promise，若不传此参数则调用props.getDefaultData()  
@ 注：若不传第二参数，在父组件中直接调用onRefreh方法时,若refresh前改变了getDefaultData请求的参数，其调用的props.getDefaultData()方法中参数可能不是最新的值

@ props :
{
	dataSource:[]  array...

	pageLength:10,
	
	//第一页的pageIndex，不传则默认为1
	firstPageIndex:0,

	// 所占高度
	height:,

	// 首次/刷新加载 
	getDefaultData:fn... 
	
	// 滚动加载方法 
	getData:fn...
	
	// 单条加载
	renderRow: fn return doms
	
	// 组件渲染时是否需要执行getDefaultData
	needRefresh: blur

	// 搭配needRefresh使用，若needRefresh为false，则可设置默认跳转至指定位置
	defaultScrollTop :num
	
	// 搭配needRefresh使用，若needRefresh为false，则可设置默认当前的pageIndex，下次请求pageIndex不从1或2开始
	historyPageIndex : num
}

	 initialListSize默认为10，暂时写死100  应从props传
*/

let ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
class APP extends Component{
	constructor(props){
		super(props);
		this.pageIndex=this.props.firstPageIndex===undefined?1:this.props.firstPageIndex;
		this.state={
			refreshing:false,
			isLoading:true,
			hasNext:true
		}
		this.isUnmount=false;
	}
	
	componentWillUnmount() {
		this.isUnmount=true;
	}

	componentDidMount(){
		const {getDefaultData,pageLength,needRefresh,historyScrollTop,historyPageIndex}=this.props;

			//若不需要刷新，则则可设置默认跳转至指定位置，可设置默认当前的pageIndex
		if(needRefresh===false){
			this.setState({
				isLoading:false,
			});
			if(historyScrollTop){
				this.refs.lv.refs.listview.scrollTo(0,historyScrollTop)
			}
			if(historyPageIndex!==undefined){
				this.pageIndex=historyPageIndex;
			}
			return;
		}

		//promise.then返回结果被中间键处理过，请求返回数据在res.action.payload.data中，若需修改，建议输出res查看后处理
		getDefaultData()
		.then((res)=>{
			if(!this.isUnmount){
				this.setState({
					isLoading:false,
					hasNext:res.action.payload.data.length >= pageLength
				})
			}
		})
	}
	
	//下拉刷新
	onRefresh(needRefreshing,promise){
		if(needRefreshing){
			this.setState({
				refreshing:true
			})
		}
	
		this.pageIndex=this.props.firstPageIndex===undefined?1:this.props.firstPageIndex;
		this.setState({
			isLoading:true,
			hasNext:true
		});

		const {getDefaultData,pageLength}=this.props;
		if(!promise){
			promise=getDefaultData(this.pageIndex)
		}
		
		promise.then((res)=>{
			if(!this.isUnmount){
				this.setState({
					refreshing:false,
					isLoading:false,
					hasNext:res.action.payload.data.length >= pageLength
				});
			}
		})
	}
	
	//瀑布加载
	onEndReached(event){
		//console.log(event,this.isLoading,this.hasNext);
		const {getData,pageLength}=this.props;
		if(event&&!this.state.isLoading&&this.state.hasNext){
			this.setState({
				isLoading:true
			})
			this.pageIndex++;
			getData(this.pageIndex)
			.then((res)=>{
				if(!this.isUnmount){
					this.setState({
						isLoading:false,
						hasNext:res.action.payload.data.length >= pageLength
					})
				}
			})
		}
	}
	
	render(){
		const {height,renderRow,dataSource} = this.props;
		
		//无数据处理  默认显示Result组件
		if(!dataSource.length&&!this.state.isLoading){
			return (		
				<ListView 
					ref="lv"
					dataSource={ds.cloneWithRows(['没有数据'])}
					renderRow={(data) =>
						<Result
						    img={<Icon type={require('src/images/login_logo.png')} className="icon" />}
						    title="没有数据"
						    message="没有当前数据，下拉可刷新"
					  	/>
					}
					style={{height:document.documentElement.clientHeight-100}}
					refreshControl={<RefreshControl
			          refreshing={this.state.refreshing}
			          onRefresh={()=>{this.onRefresh(true)}}
			        />}
				/>
			)
		}else{
			return (
				<ListView 
					ref='lv'
					initialListSize={100}
					dataSource={ds.cloneWithRows(dataSource)}
					renderRow={(rowData, sectionID, rowID, highlightRow) => renderRow(rowData, sectionID, rowID, highlightRow)}
					style={{height}}
					onEndReached={(event)=>this.onEndReached(event)}
					onEndReachedThreshold={20}
					scrollerOptions={{ scrollbars: true }}
					scrollRenderAheadDistance={500}
					refreshControl={<RefreshControl
			          refreshing={this.state.refreshing}
			          onRefresh={()=>{this.onRefresh(true)}}
			        />}
			        renderFooter={() => <div style={{ height: '0.52rem',lineHeight:'0.52rem', textAlign: 'center' }}>
			          {this.state.isLoading? '加载中...' : null}
			          {this.state.hasNext?null:'加载完毕'}
			        </div>}
				/>
			)
		}
	}
}

APP.propTypes={
	dataSource:PropTypes.array,
	pageLength:PropTypes.number,
	height:PropTypes.number,
	getDefaultData:PropTypes.func,
	getData:PropTypes.func,
	renderRow:PropTypes.func
}

export default APP;