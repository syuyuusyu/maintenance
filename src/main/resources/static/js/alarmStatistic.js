

// var mainPanle = Ext.create('Ext.Panel', {
//     region : 'center',
//     layout : 'fit'
// });
var mainPanle=Ext.create('Ext.tab.Panel',{
    region:'center',
    layout: 'fit'
});


var cloudPanel = Ext.create('Ext.Panel', {
    title : '云平台',
    id : 'cloudPanel',
    //closable : true,
    layout : 'fit',
    html : 'sdsd'

});

var dataPanel = Ext.create('Ext.Panel', {
    title : '大数据平台',
    id : 'dataPanel',
    //closable : true,
    layout : 'fit',
    html : 'sdsd'

});

var secPanel = Ext.create('Ext.Panel', {
    title : '安全平台',
    id : 'secPanel',
    //closable : true,
    layout : 'fit',
    html : 'sdsd'

});



mainPanle.add(cloudPanel);
mainPanle.add(dataPanel);
mainPanle.add(secPanel);


Ext.onReady(function() {

    Ext.getDoc().on("contextmenu", function(e) {
        e.stopEvent();
    });
    var view = new Ext.Viewport({
        layout : 'border',
        items : [ mainPanle ]
    });
    console.log(cloudPanel.getHeight());
    $('#cloudPanel-body').empty().append(
        '<div id="cloud-canv" style="height:' + cloudPanel.getHeight()
        + 'px;width:' + cloudPanel.getWidth()
        + 'px;border:0px solid white;"></div>');
    mainPanle.setActiveTab(dataPanel);
    $('#dataPanel-body').empty().append(
        '<div id="data-canv" style="height:' + dataPanel.getHeight()
        + 'px;width:' + dataPanel.getWidth()
        + 'px;border:0px solid white;"></div>');
    mainPanle.setActiveTab(secPanel);
    $('#secPanel-body').empty().append(
        '<div id="sec-canv" style="height:' + secPanel.getHeight()
        + 'px;width:' + secPanel.getWidth()
        + 'px;border:0px solid white;"></div>');
    mainPanle.setActiveTab(cloudPanel);
    pieChart();
});


var option = {
    title : {
        text: '告警信息统计',
        x:'center'
    },
    legend: {
        y:600
        //,data:['云平台已处理告警','云平台未处理告警','大数据已处理告警','大数据未处理告警','安全平台已处理告警','安全平台未处理告警']
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"

    },
    toolbox: {
        show : false,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {
                show: true,
                type: ['pie', 'funnel'],
                option: {
                    funnel: {
                        x: '25%',
                        width: '50%',
                        funnelAlign: 'left',
                        max: 1548
                    }
                }
            },
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    series : []
};


function pieChart(plateId){
    Ext.Ajax.request({
        method:'post',
        url:'./../alarm/alarmStatistics',
        params:{},
        failure:function(r,data){

        },
        success:function(r,data){
            var result = Ext.JSON.decode(r.responseText);
            console.log(result);
            var coloudOn={
                name:'云平台已处理告警',
                type:'pie',
                radius : '35%',
                center: ['33%', '50%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false,
                            formatter: '{b}'
                        },
                        labelLine : {
                            show : false
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
                data:[]
            };
            var coloudOf={
                name:'云平台未处理告警',
                type:'pie',
                radius : '35%',
                center: ['66%', '50%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false,
                            formatter: '{b}'
                        },
                        labelLine : {
                            show : false
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
                data:[]
            };
            var dataOn={
                name:'大数据已处理告警',
                type:'pie',
                radius : '25%',
                center: ['25%', '50%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false,
                            formatter: '{b}'
                        },
                        labelLine : {
                            show : false
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
                data:[]
            };
            var dataOf={
                name:'大数据未处理告警',
                type:'pie',
                radius : '25%',
                center: ['75%', '50%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false,
                            formatter: '{b}'
                        },
                        labelLine : {
                            show : false
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
                data:[]
            };
            var sON={
                name:'安全平台已处理告警',
                type:'pie',
                radius : '35%',
                center: ['33%', '50%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false,
                            formatter: '{b} : {c} ({d}%)'
                        },
                        labelLine : {
                            show : false
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
                data:[]
            };
            var soff={
                name:'安全平台未处理告警',
                type:'pie',
                radius : '35%',
                center: ['66%', '50%'],
                itemStyle : {
                    normal : {
                        label : {
                            show : false,
                            formatter: '{b} : {c} ({d}%)'
                        },
                        labelLine : {
                            show : false
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
                data:[]
            };
            for(var i=0;i<result.length;i++){
                var data=result[i];
                if(data.plateId==2){
                    if(data.step==2){
                        coloudOn.data.push({value:data.count,name:data.ruleName,plateId:data.plateId,ruleId:data.ruleId,step:data.step});
                    }else{
                        coloudOf.data.push({value:data.count,name:data.ruleName,plateId:data.plateId,ruleId:data.ruleId,step:data.step});
                    }
                }else if(data.plateId==3){
                    if(data.step==2){
                        dataOn.data.push({value:data.count,name:data.ruleName,plateId:data.plateId,ruleId:data.ruleId,step:data.step});
                    }else{
                        dataOf.data.push({value:data.count,name:data.ruleName,plateId:data.plateId,ruleId:data.ruleId,step:data.step});
                    }
                }else if(data.plateId==4){
                    if(data.step==2){
                        sON.data.push({value:data.count,name:data.ruleName,plateId:data.plateId,ruleId:data.ruleId,step:data.step});
                    }else{
                        soff.data.push({value:data.count,name:data.ruleName,plateId:data.plateId,ruleId:data.ruleId,step:data.step});
                    }
                }

            }
            var op={};
            Ext.apply(op,option);

            //op.series=[].concat(coloudOn,coloudOf,dataOn,dataOf,sON,soff);
            console.log(op);
            require.config({
                paths : {
                    echarts : '../js/echart'
                }
            });
            //yun
            require(
                [ 'echarts', 'echarts/chart/pie' ],

                function(ec) {
                    console.log(op);
                    var myChart = ec.init(document.getElementById('cloud-canv'));
                    var op1={};
                    Ext.apply(op1,op);
                    op1.series=[].concat(coloudOn,coloudOf);
                    op1.legend.data=['云平台已处理告警','云平台未处理告警'];
                    myChart.setOption(op1);
                    var ecConfig = require('echarts/config');
                    myChart.on(ecConfig.EVENT.CLICK,function(param){
                        console.log(param.data);
                        if(typeof param.data !='object'){
                            var i=param.dataIndex;
                            console.log(param);
                        }
                        var grid=createGrid(param.data.plateId,param.data.step,param.data.ruleId)
                        Ext.create('Ext.window.Window', {
                            //id:'win_'+entity.entityName,
                            title: '告警处理信息',
                            height: 600,
                            width: 1280,
                            layout: 'border',
                            items: [{
                                xtype:'panel',
                                region : 'center',
                                layout: 'fit',
                                items:[grid]
                            }]
                            ,listeners:{
                                close:function(){
                                    pieChart(2);
                                }
                            }
                        }).show();
                    });


                });
            //bigdata
            require(
                [ 'echarts', 'echarts/chart/pie' ],

                function(ec) {
                    var myChart = ec.init(document.getElementById('data-canv'));
                    var op1={};
                    Ext.apply(op1,op);
                    op1.series=[].concat(dataOn,dataOf);
                    op1.legend.data=['大数据已处理告警','大数据未处理告警'];
                    myChart.setOption(op1);
                    var ecConfig = require('echarts/config');
                    myChart.on(ecConfig.EVENT.CLICK,function(param){
                        console.log(param.data);
                        if(typeof param.data !='object'){
                            var i=param.dataIndex;
                            console.log(param);
                        }
                        var grid=createGrid(param.data.plateId,param.data.step,param.data.ruleId)
                        Ext.create('Ext.window.Window', {
                            //id:'win_'+entity.entityName,
                            title: '告警处理信息',
                            height: 600,
                            width: 1280,
                            layout: 'border',
                            items: [{
                                xtype:'panel',
                                region : 'center',
                                layout: 'fit',
                                items:[grid]
                            }]
                            ,listeners:{
                                close:function(){
                                    pieChart(3);
                                }
                            }
                        }).show();
                    });


                });
            //sec
            require(
                [ 'echarts', 'echarts/chart/pie' ],

                function(ec) {
                    var myChart = ec.init(document.getElementById('sec-canv'));
                    var op1={};
                    Ext.apply(op1,op);
                    op1.series=[].concat(sON,soff);
                    op1.legend.data=['安全平台已处理告警','安全平台未处理告警'];
                    myChart.setOption(op1);
                    var ecConfig = require('echarts/config');
                    myChart.on(ecConfig.EVENT.CLICK,function(param){
                        console.log(param.data);
                        if(typeof param.data !='object'){
                            var i=param.dataIndex;
                            console.log(param);
                        }
                        var grid=createGrid(param.data.plateId,param.data.step,param.data.ruleId)
                        Ext.create('Ext.window.Window', {
                            //id:'win_'+entity.entityName,
                            title: '告警处理信息',
                            height: 600,
                            width: 1280,
                            layout: 'border',
                            items: [{
                                xtype:'panel',
                                region : 'center',
                                layout: 'fit',
                                items:[grid]
                            }]
                            ,listeners:{
                                close:function(){
                                    pieChart(4);
                                }
                            }
                        }).show();
                    });


                });
        }
    });
    if(plateId){
        switch (plateId){
            case 2:
                mainPanle.setActiveTab(cloudPanel);
                break;
            case 3:
                mainPanle.setActiveTab(dataPanel);
                break;
            case 4:
                mainPanle.setActiveTab(secPanel);
                break;
        }
    }
}
