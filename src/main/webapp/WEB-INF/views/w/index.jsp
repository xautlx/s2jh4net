<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>首页</title>
</head>
<body>

	<!-- BEGIN REVOLUTION SLIDER -->
	<div class="fullwidthbanner-container slider-main">
		<div class="fullwidthabnner">
			<ul id="revolutionul" style="display: none;">
				<!-- THE FIRST SLIDE -->
				<li data-transition="fade" data-slotamount="8" data-masterspeed="700" data-delay="9400"
					data-thumb="${ctx}/assets/w/img/sliders/revolution/thumbs/thumb2.jpg">
					<!-- THE MAIN IMAGE IN THE FIRST SLIDE --> <img src="${ctx}/assets/w/img/sliders/revolution/bg1.jpg" alt="">

					<div class="caption lft slide_title slide_item_left" data-x="0" data-y="105" data-speed="400" data-start="1500"
						data-easing="easeOutExpo">Need a website design ?</div>
					<div class="caption lft slide_subtitle slide_item_left" data-x="0" data-y="180" data-speed="400" data-start="2000"
						data-easing="easeOutExpo">This is what you were looking for</div>
					<div class="caption lft slide_desc slide_item_left" data-x="0" data-y="220" data-speed="400" data-start="2500"
						data-easing="easeOutExpo">
						Lorem ipsum dolor sit amet, dolore eiusmod<br> quis tempor incididunt. Sed unde omnis iste.
					</div> <a class="caption lft btn green slide_btn slide_item_left"
					href="http://themeforest.net/item/metronic-responsive-admin-dashboard-template/4021469?ref=keenthemes" data-x="0"
					data-y="290" data-speed="400" data-start="3000" data-easing="easeOutExpo"> Purchase Now! </a>
					<div class="caption lfb" data-x="640" data-y="55" data-speed="700" data-start="1000" data-easing="easeOutExpo">
						<img src="${ctx}/assets/w/img/sliders/revolution/man-winner.png" alt="Image 1">
					</div>
				</li>

				<!-- THE SECOND SLIDE -->
				<li data-transition="fade" data-slotamount="7" data-masterspeed="300" data-delay="9400"
					data-thumb="${ctx}/assets/w/img/sliders/revolution/thumbs/thumb2.jpg"><img
					src="${ctx}/assets/w/img/sliders/revolution/bg2.jpg" alt="">
					<div class="caption lfl slide_title slide_item_left" data-x="0" data-y="125" data-speed="400" data-start="3500"
						data-easing="easeOutExpo">Powerfull & Clean</div>
					<div class="caption lfl slide_subtitle slide_item_left" data-x="0" data-y="200" data-speed="400" data-start="4000"
						data-easing="easeOutExpo">Responsive Admin & Website Theme</div>
					<div class="caption lfl slide_desc slide_item_left" data-x="0" data-y="245" data-speed="400" data-start="4500"
						data-easing="easeOutExpo">
						Lorem ipsum dolor sit amet, consectetuer elit sed diam<br> nonummy amet euismod dolore.
					</div>
					<div class="caption lfr slide_item_right" data-x="635" data-y="105" data-speed="1200" data-start="1500"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/mac.png" alt="Image 1">
					</div>
					<div class="caption lfr slide_item_right" data-x="580" data-y="245" data-speed="1200" data-start="2000"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/ipad.png" alt="Image 1">
					</div>
					<div class="caption lfr slide_item_right" data-x="735" data-y="290" data-speed="1200" data-start="2500"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/iphone.png" alt="Image 1">
					</div>
					<div class="caption lfr slide_item_right" data-x="835" data-y="230" data-speed="1200" data-start="3000"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/macbook.png" alt="Image 1">
					</div>
					<div class="caption lft slide_item_right" data-x="865" data-y="45" data-speed="500" data-start="5000"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/hint1-blue.png" id="rev-hint1" alt="Image 1">
					</div>
					<div class="caption lfb slide_item_right" data-x="355" data-y="355" data-speed="500" data-start="5500"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/hint2-blue.png" id="rev-hint2" alt="Image 1">
					</div></li>

				<!-- THE THIRD SLIDE -->
				<li data-transition="fade" data-slotamount="8" data-masterspeed="700" data-delay="9400"
					data-thumb="${ctx}/assets/w/img/sliders/revolution/thumbs/thumb2.jpg"><img
					src="${ctx}/assets/w/img/sliders/revolution/bg3.jpg" alt="">
					<div class="caption lfl slide_item_left" data-x="20" data-y="95" data-speed="400" data-start="1500"
						data-easing="easeOutBack">
						<iframe src="http://player.vimeo.com/video/56974716?portrait=0" width="420" height="240" style="border: 0"
							allowFullScreen></iframe>
					</div>
					<div class="caption lfr slide_title" data-x="470" data-y="100" data-speed="400" data-start="2000"
						data-easing="easeOutExpo">Responsive Video Support</div>
					<div class="caption lfr slide_subtitle" data-x="470" data-y="170" data-speed="400" data-start="2500"
						data-easing="easeOutExpo">Youtube, Vimeo and others.</div>
					<div class="caption lfr slide_desc" data-x="470" data-y="220" data-speed="400" data-start="3000"
						data-easing="easeOutExpo">
						Lorem ipsum dolor sit amet, consectetuer elit sed diam<br> nonummy amet euismod dolore.
					</div> <a class="caption lfr btn yellow slide_btn" href="" data-x="470" data-y="280" data-speed="400" data-start="3500"
					data-easing="easeOutExpo"> Watch more Videos! </a></li>

				<!-- THE FORTH SLIDE -->
				<li data-transition="fade" data-slotamount="8" data-masterspeed="700" data-delay="9400"
					data-thumb="${ctx}/assets/w/img/sliders/revolution/thumbs/thumb2.jpg">
					<!-- THE MAIN IMAGE IN THE FIRST SLIDE --> <img src="${ctx}/assets/w/img/sliders/revolution/bg4.jpg" alt="">
					<div class="caption lft slide_title" data-x="0" data-y="105" data-speed="400" data-start="1500"
						data-easing="easeOutExpo">What else included ?</div>
					<div class="caption lft slide_subtitle" data-x="0" data-y="180" data-speed="400" data-start="2000"
						data-easing="easeOutExpo">The Most Complete Admin Theme</div>
					<div class="caption lft slide_desc" data-x="0" data-y="225" data-speed="400" data-start="2500"
						data-easing="easeOutExpo">
						Lorem ipsum dolor sit amet, consectetuer elit sed diam<br> nonummy amet euismod dolore.
					</div> <a class="caption lft slide_btn btn red slide_item_left"
					href="http://www.keenthemes.com/preview/index.php?theme=metronic_admin" target="_blank" data-x="0" data-y="300"
					data-speed="400" data-start="3000" data-easing="easeOutExpo"> Learn More! </a>
					<div class="caption lft start" data-x="670" data-y="55" data-speed="400" data-start="2000"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/iphone_left.png" alt="Image 2">
					</div>

					<div class="caption lft start" data-x="850" data-y="55" data-speed="400" data-start="2400"
						data-easing="easeOutBack">
						<img src="${ctx}/assets/w/img/sliders/revolution/iphone_right.png" alt="Image 3">
					</div>
				</li>
			</ul>
			<div class="tp-bannertimer tp-bottom"></div>
		</div>
	</div>
	<!-- END REVOLUTION SLIDER -->

	<!-- BEGIN CONTAINER -->
	<div class="container">
		<div class="note note-danger">
			<h2 class="block">访问说明：</h2>
			<p>
				目前 <a href="${ctx}/w" target="_blank"><i class="fa fa-windows"></i> 前端Web站点</a> | <a href="${ctx}/m" target="_blank"><i
					class="fa fa-html5"></i> HTML5移动站点</a> 仅作站点布局展示，暂无太多功能实现
			<h4>
				主要原型演示请访问：<a href="${ctx}/admin" target="_blank">管理后台系统</a>
			</h4>
			</p>
		</div>

		<!-- BEGIN SERVICE BOX -->
		<div class="row service-box">
			<div class="col-md-4 col-sm-4">
				<div class="service-box-heading">
					<em><i class="fa fa-location-arrow blue"></i></em> <span>Multipurpose Template</span>
				</div>
				<p>Lorem ipsum dolor sit amet, dolore eiusmod quis tempor incididunt ut et dolore Ut veniam unde nostrudlaboris.
					Sed unde omnis iste natus error sit voluptatem.</p>
			</div>
			<div class="col-md-4 col-sm-4">
				<div class="service-box-heading">
					<em><i class="fa fa-check red"></i></em> <span>Well Documented</span>
				</div>
				<p>Lorem ipsum dolor sit amet, dolore eiusmod quis tempor incididunt ut et dolore Ut veniam unde nostrudlaboris.
					Sed unde omnis iste natus error sit voluptatem.</p>
			</div>
			<div class="col-md-4 col-sm-4">
				<div class="service-box-heading">
					<em><i class="fa fa-resize-small green"></i></em> <span>Responsive Design</span>
				</div>
				<p>Lorem ipsum dolor sit amet, dolore eiusmod quis tempor incididunt ut et dolore Ut veniam unde nostrudlaboris.
					Sed unde omnis iste natus error sit voluptatem.</p>
			</div>
		</div>
		<!-- END SERVICE BOX -->

		<!-- BEGIN BLOCKQUOTE BLOCK -->
		<div class="row quote-v1">
			<div class="col-md-9 quote-v1-inner">
				<span>Metronic - The Most Complete & Popular Admin & Frontend Theme</span>
			</div>
			<div class="col-md-3 quote-v1-inner text-right">
				
			</div>
		</div>
		<!-- END BLOCKQUOTE BLOCK -->

		<div class="clearfix"></div>

		<!-- BEGIN RECENT WORKS -->
		<div class="row recent-work margin-bottom-40">
			<div class="col-md-3">
				<h2>
					<a href="portfolio.html">Recent Works</a>
				</h2>
				<p>Lorem ipsum dolor sit amet, dolore eiusmod quis tempor incididunt ut et dolore Ut veniam unde voluptatem. Sed
					unde omnis iste natus error sit voluptatem.</p>
			</div>
			<div class="col-md-9">
				<ul class="bxslider">
					<li><em> <img src="${ctx}/assets/w/img/works/img1.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img1.jpg"
							class="fancybox-button" title="Project Name #1" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img2.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img2.jpg"
							class="fancybox-button" title="Project Name #2" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img3.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img3.jpg"
							class="fancybox-button" title="Project Name #3" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img4.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img4.jpg"
							class="fancybox-button" title="Project Name #4" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img5.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img5.jpg"
							class="fancybox-button" title="Project Name #5" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img6.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img6.jpg"
							class="fancybox-button" title="Project Name #6" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img3.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img3.jpg"
							class="fancybox-button" title="Project Name #3" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
					<li><em> <img src="${ctx}/assets/w/img/works/img4.jpg" alt="" /> <a href="portfolio_item.html"><i
								class="fa fa-link icon-hover icon-hover-1"></i></a> <a href="${ctx}/assets/w/img/works/img4.jpg"
							class="fancybox-button" title="Project Name #4" data-rel="fancybox-button"><i
								class="fa fa-search icon-hover icon-hover-2"></i></a>
					</em> <a class="bxslider-block" href="#"> <strong>Amazing Project</strong> <b>Agenda corp.</b>
					</a></li>
				</ul>
			</div>
		</div>
		<!-- END RECENT WORKS -->

		<div class="clearfix"></div>

		<!-- BEGIN TABS AND TESTIMONIALS -->
		<div class="row mix-block">
			<!-- TABS -->
			<div class="col-md-7 tab-style-1 margin-bottom-20">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#tab-1" data-toggle="tab">Multipurpose</a></li>
					<li><a href="#tab-2" data-toggle="tab">Documented</a></li>
					<li><a href="#tab-3" data-toggle="tab">Responsive</a></li>
					<li><a href="#tab-4" data-toggle="tab">Clean & Fresh</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane row fade in active" id="tab-1">
						<div class="col-md-3"></div>
						<div class="col-md-9">
							<p class="margin-bottom-10">Raw denim you probably haven't heard of them jean shorts Austin. Nesciunt tofu
								stumptown aliqua, retro synth master cleanse. Mustache cliche tempor, williamsburg carles vegan helvetica. Cosby
								sweater eu banh mi, qui irure terry richardson ex squid Aliquip placeat salvia cillum iphone.</p>
							<p>
								<a class="more" href="#">Read more <i class="icon-angle-right"></i></a>
							</p>
						</div>
					</div>
					<div class="tab-pane row fade" id="tab-2">
						<div class="col-md-9">
							<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. Exercitation +1
								labore velit, blog sartorial PBR leggings next level wes anderson artisan four loko farm-to-table craft beer
								twee. Qui photo booth letterpress, commodo enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum
								PBR. Homo nostrud organic, assumenda labore aesthetic magna delectus mollit. Keytar helvetica VHS salvia..</p>
						</div>
						<div class="col-md-3"></div>
					</div>
					<div class="tab-pane fade" id="tab-3">
						<p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's organic lomo retro fanny
							pack lo-fi farm-to-table readymade. Messenger bag gentrify pitchfork tattooed craft beer, iphone skateboard
							locavore carles etsy salvia banksy hoodie helvetica. DIY synth PBR banksy irony. Leggings gentrify squid 8-bit
							cred pitchfork. Williamsburg banh mi whatever gluten-free, carles pitchfork biodiesel fixie etsy retro mlkshk
							vice blog. Scenester cred you probably haven't heard of them, vinyl craft beer blog stumptown. Pitchfork
							sustainable tofu synth chambray yr.</p>
					</div>
					<div class="tab-pane fade" id="tab-4">
						<p>Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they sold out master cleanse
							gluten-free squid scenester freegan cosby sweater. Fanny pack portland seitan DIY, art party locavore wolf cliche
							high life echo park Austin. Cred vinyl keffiyeh DIY salvia PBR, banh mi before they sold out farm-to-table VHS
							viral locavore cosby sweater. Lomo wolf viral, mustache readymade thundercats keffiyeh craft beer marfa ethical.
							Wolf salvia freegan, sartorial keffiyeh echo park vegan.</p>
					</div>
				</div>
			</div>
			<!-- END TABS -->

			<!-- TESTIMONIALS -->
			<div class="col-md-5 testimonials-v1">
				<div id="myCarousel" class="carousel slide">
					<!-- Carousel items -->
					<div class="carousel-inner">
						<div class="active item">
							<span class="testimonials-slide">Denim you probably haven't heard of. Lorem ipsum dolor met consectetur
								adipisicing sit amet, consectetur adipisicing elit, of them jean shorts sed magna aliqua. Lorem ipsum dolor met
								consectetur adipisicing sit amet do eiusmod dolore.</span>
							<div class="carousel-info">
								<div class="pull-left">
									<span class="testimonials-name">Lina Mars</span> <span class="testimonials-post">Commercial Director</span>
								</div>
							</div>
						</div>
						<div class="item">
							<span class="testimonials-slide">Raw denim you Mustache cliche tempor, williamsburg carles vegan helvetica
								probably haven't heard of them jean shorts austin. Nesciunt tofu stumptown aliqua, retro synth master cleanse.
								Mustache cliche tempor, williamsburg carles vegan helvetica.</span>
							<div class="carousel-info">
								<div class="pull-left">
									<span class="testimonials-name">Kate Ford</span> <span class="testimonials-post">Commercial Director</span>
								</div>
							</div>
						</div>
						<div class="item">
							<span class="testimonials-slide">Reprehenderit butcher stache cliche tempor, williamsburg carles vegan
								helvetica.retro keffiyeh dreamcatcher synth. Cosby sweater eu banh mi, qui irure terry richardson ex squid
								Aliquip placeat salvia cillum iphone.</span>
							<div class="carousel-info">
								<div class="pull-left">
									<span class="testimonials-name">Jake Witson</span> <span class="testimonials-post">Commercial Director</span>
								</div>
							</div>
						</div>
					</div>

					<!-- Carousel nav -->
					<a class="left-btn" href="#myCarousel" data-slide="prev"></a> <a class="right-btn" href="#myCarousel"
						data-slide="next"></a>
				</div>
			</div>
			<!-- END TESTIMONIALS -->
		</div>
		<!-- END TABS AND TESTIMONIALS -->

		<!-- BEGIN STEPS -->
		<div class="row no-space-steps margin-bottom-40">
			<div class="col-md-4 col-sm-4">
				<div class="front-steps front-step-one">
					<h2>Goal definition</h2>
					<p>Lorem ipsum dolor sit amet sit consectetur adipisicing eiusmod tempor.</p>
				</div>
			</div>
			<div class="col-md-4 col-sm-4">
				<div class="front-steps front-step-two">
					<h2>Analyse</h2>
					<p>Lorem ipsum dolor sit amet sit consectetur adipisicing eiusmod tempor.</p>
				</div>
			</div>
			<div class="col-md-4 col-sm-4">
				<div class="front-steps front-step-three">
					<h2>Implementation</h2>
					<p>Lorem ipsum dolor sit amet sit consectetur adipisicing eiusmod tempor.</p>
				</div>
			</div>
		</div>
		<!-- END STEPS -->
	</div>
</body>
</html>