import React from 'react'
import './index.less'

export default class Rating extends React.Component {

  constructor (props) {
    super(props)
    this.state = {
      min_score: 1,
      max_score: 5,
      default_rating: props.rating || 0,
      rating:  props.rating || 0,
    }
  }

  render () {
    let { max_score, rating } = this.state
    let nodes=[];
    for(let k=0;k<max_score;k++){
      nodes.push(
        <i key={k} className={k < rating ? 'sel' : ''} />
      )
    }
    return (
      <div 
        ref="dom"
        className="m-rating" 
        onTouchStart={this.onTouchStart.bind(this)}
        onTouchMove={this.onTouchMove.bind(this)}
        onTouchEnd={this.onTouchEnd.bind(this)}
        >
        {nodes}
      </div>
    )
  }

  onTouchStart (e) {
    if (this.props.disabled) { return }
    this.onTouchMove(e)
  }

  onTouchMove (e) {
    if (this.props.disabled) { return }

    let dom = this.refs.dom
    let ne = e.nativeEvent
    let touch = ne.touches[0]
    let rect = dom.getBoundingClientRect()
    let x = touch.pageX - rect.left
    let rating = Math.round(x / rect.width * this.state.max_score)
    rating = Math.max(this.state.min_score, rating)
    rating = Math.min(this.state.max_score, rating)

    this.setState({ rating })
  }

  onTouchEnd (e) {
    if (this.props.disabled) { return }
    
    let { onSelect } = this.props
    onSelect && onSelect({
      rating: this.state.rating,
      reset: this.reset.bind(this),
    })
  }

  reset () {
    this.setState({ rating: this.state.default_rating })
  }
}