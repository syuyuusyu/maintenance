//制保留2位小数，如：2，会在2后面补上00.即2.00  
function toDecimal2(x) {
	var f = parseFloat(x);
	if (isNaN(f)) {
		return false;
	}
	var f = Math.round(x * 100) / 100;
	var s = f.toString();
	var rs = s.indexOf('.');
	if (rs < 0) {
		rs = s.length;
		s += '.';
	}
	while (s.length <= rs + 1) {
		s += '0';
	}
	return s;
}

var mainPanle = Ext.create('Ext.tab.Panel', {
	region : 'center',
	layout : 'fit'
});

var cloudPanel = Ext.create('Ext.Panel', {
	title : '云平台状态',
	id : 'cloudPanel',
	closable : true,
	layout : 'fit',
	html : 'sdsd'

});

mainPanle.add(cloudPanel);
mainPanle.setActiveTab(cloudPanel);

Ext.onReady(function() {
	Ext.getDoc().on("contextmenu", function(e) {
		e.stopEvent();
	});
	var view = new Ext.Viewport({
		layout : 'border',
		items : [ mainPanle ]
	});
	console.log(cloudPanel.getWidth());
	console.log(cloudPanel.getHeight());
	$('#cloudPanel-body').empty().append(
			'<div id="cloud-canv" style="height:' + cloudPanel.getHeight()
					+ 'px;width:' + cloudPanel.getWidth()
					+ 'px;border:0px solid white;"></div>')
	pieChart();

});
var data = {
	"regionName" : "1dbf691f-8c05-4e7e-bb9c-8570f0e47b29",
	"vcpuUsed" : 313,
	"vcpuTotal" : 360,
	"vcpuAvailable" : 47,
	"vcpuUsedPercentage" : 0.8694444444444445,
	"memoryUsed" : 940539,
	"memoryTotal" : 1178514,
	"memoryAvailable" : 237975,
	"memoryUsedPercentage" : 0.7980719787800569,
	"diskUsed" : 7680,
	"diskTotal" : 239055,
	"diskAvailable" : 231375,
	"diskUsedPercentage" : 0.03212649808621447,
	"instanceUsed" : 41,
	"instanceTotal" : 100,
	"instanceAvailable" : 59,
	"instanceUsedPercentage" : 0.41,
	"elasticIpUsed" : 0,
	"elasticIpTotal" : 10,
	"elasticIpAvailable" : 10,
	"elasticIpUsedPercentage" : 0,
	"securityGroupUsed" : 7,
	"securityGroupTotal" : 10,
	"securityGroupAvailable" : 3,
	"securityGroupUsedPercentage" : 0.7,
	"volumeUsed" : 2600,
	"volumeTotal" : 30000000,
	"volumeAvailable" : 29997400,
	"volumeUsedPercentage" : 0.00008666666666666667
};
var labeluse = {
	normal : {
		label : {
			position : 'top',
			show : true,
			formatter : function(params){
				return '已使用:'+params.value;
			},
			textStyle : {
				baseline : 'middle'
			}
		},
		labelLine : {
			show : true
		}
	}
};
var labelvali = {
		normal : {
			label : {
				position : 'top',
				show : true,
				formatter : function(params){
					return '可用:'+params.value;
				},
				textStyle : {
					baseline : 'middle'
				}
			},
			labelLine : {
				show : true
			}
		}
	};
var labelFromatter = {
	normal : {
		label : {
			position : 'center',
			formatter : function(params) {
				console.log(params);
				var title = params.series.seriesName, 
					vail = params.series.data[0].value,
					use = params.series.data[1].value;
				return title+'\n' + toDecimal2(use / (use + vail) * 100) + '%'
			},
			textStyle : {
				baseline : 'top'
			}
		},
		labelLine : {
			show : false
		}
	},
}

var radius = [ 45, 65 ];
var optionT = {
	legend : {
		x : 'center',
		y : 'center',
		data : []
	},
	title : {
		text : '云平台状态',
		subtext : 'from global web index',
		x : 'center'
	},
	toolbox : {
		show : true,
		feature : {
			dataView : {
				show : true,
				readOnly : false
			},
			magicType : {
				show : true,
				type : [ 'pie' ]
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},
	series : [ 
	    {
		type : 'pie',
		seriesName : 'cpu使用量',
		center : [ '20%', '30%' ],
		radius : radius,
		itemStyle : labelFromatter,
		data : [ 
		         {name : 'cpu可用',value : 47, itemStyle : labelvali}, 
		         {name : 'cpu使用',value : 313,itemStyle : labeluse} 
		       ]
	}, {
		type : 'pie',
		seriesName : '内存使用量',
		name:'内存使用量',
		center : [ '40%', '30%' ],
		radius : radius,
		x : '20%', // for funnel
		itemStyle : labelFromatter,
		data : [ {
			name : '内存可用',
			value : 56
			//,itemStyle : labelvali
		}, {
			name : '内存使用',
			value : 44
			//,itemStyle : labeluse
		} ]
	}, {
		type : 'pie',
		center : [ '60%', '30%' ],
		radius : radius,
		x : '40%', // for funnel
		itemStyle : labelFromatter,
		data : [ {
			name : '硬盘可用',
			value : 65,
			itemStyle : labelvali
		}, {
			name : '硬盘使用',
			value : 35,
			itemStyle : labeluse
		} ]
	}, {
		type : 'pie',
		center : [ '80%', '30%' ],
		radius : radius,
		x : '60%', // for funnel
		itemStyle : labelFromatter,
		data : [ {
			name : '实例可用',
			value : 70,
			itemStyle : labelvali
		}, {
			name : '实例使用',
			value : 30,
			itemStyle : labeluse
		} ]
	}, {
		type : 'pie',
		center : [ '30%', '70%' ],
		radius : radius,
		x : '80%', // for funnel
		itemStyle : labelFromatter,
		data : [ {
			name : 'ip可用',
			value : 73,
			itemStyle : labelvali
		}, {
			name : 'ip使用',
			value : 27,
			itemStyle : labeluse
		} ]
	}, {
		type : 'pie',
		center : [ '50%', '70%' ],
		radius : radius,
		y : '55%', // for funnel
		x : '0%', // for funnel
		itemStyle : labelFromatter,
		data : [ {
			name : '安全组可用',
			value : 78,
			itemStyle : labelvali
		}, {
			name : '安全组使用',
			value : 22,
			itemStyle : labeluse
		} ]
	}, {
		type : 'pie',
		center : [ '70%', '70%' ],
		radius : radius,
		y : '55%', // for funnel
		x : '20%', // for funnel
		itemStyle : labelFromatter,
		data : [ {
			name : '卷可用',
			value : 78,
			itemStyle : labelvali
		}, {
			name : '卷使用',
			value : 22,
			itemStyle : labeluse
		} ]
	}

	]
};

function pieChart(resultData) {

	console.log(11111);
	var width = 1200;
	var height = 600;
	function initData() {
		return optionT;
		var option = {
			title : {
				text : '',
				subtext : '',
				x : 'center'
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				x : 'center',
				y : 'bottom',
				selectedMode : 'single',
				data : [],
				selected : function() {
					var obj = {};
					for (var i = 1; i < resultData.seriesHeader.length; i++) {
						obj[resultData.seriesHeader[i]] = false;
					}
					return obj;
				}()
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'pie' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			series : []
		};
		option.title.text = resultData.caption;
		option.legend.data = resultData.seriesHeader;

		for (var j = 0; j < resultData.seriesDataIndex.length; j++) {
			var seriesTemp = {
				name : '',
				type : 'pie',
				//radius : [20, 110],
				//center : ['25%', 300],
				//roseType : 'radius',
				//width: '40%',       // for funnel
				//max: 40,            // for funnel
				itemStyle : {
					normal : {
						label : {
							show : true,
							formatter : '{b} : {c} ({d}%)'
						},
						labelLine : {
							show : true
						}
					},
					emphasis : {
						label : {
							show : true
						},
						labelLine : {
							show : true
						}
					}
				},
				data : []
			};

			seriesTemp.radius = [ 20, 160 ];
			seriesTemp.center = [ width / 2, height / 2 ];
			seriesTemp.name = resultData.seriesHeader[j];
			seriesTemp.data = [];
			for (var i = 0; i < resultData.data.length; i++) {
				seriesTemp.data.push({
					name : resultData.data[i][resultData.cateDataIndex],
					value : resultData.data[i][resultData.seriesDataIndex[j]]
				});
			}
			option.series.push(seriesTemp);
		}
		return optionT;
	}

	require.config({
		paths : {
			echarts : '../js/echart'
		}
	});
	require(
			[ 'echarts', 'echarts/chart/pie' ],

			function(ec) {
				var myChart = ec.init(document.getElementById('cloud-canv'));
				//console.log(initData());
				myChart.setOption(initData());
				var ecConfig = require('echarts/config');
				if (resultData.seriesDataIndex.length > 1) {
					myChart
							.on(
									ecConfig.EVENT.CLICK,
									function(param) {
										var i = param.dataIndex;
										var panel = Ext
												.create(
														'Ext.Panel',
														{
															title : '子饼图-'
																	+ resultData.data[i][resultData.cateDataIndex],
															layout : 'fit',
															closable : true,
															html : "<div id='subpiechart"
																	+ resultData.reportId
																	+ i
																	+ "' style='height:600px;width:1200px;border:0px solid white;'></div>"
														});
										mainPanle.add(panel);
										mainPanle.setActiveTab(panel);
										subpiechart(resultData, i);
									});
				}
			});
}