

var mainPanle = Ext.create('Ext.Panel', {
    region : 'center',
    layout : 'fit'
});

var cloudPanel = Ext.create('Ext.Panel', {
    title : '告警统计',
    id : 'cloudPanel',
    //closable : true,
    layout : 'fit',
    html : 'sdsd'

});

mainPanle.add(cloudPanel);

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


var option = {
    title : {
        text: '告警信息统计',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    toolbox: {
        show : true,
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


function pieChart(){
    Ext.Ajax.request({
        method:'post',
        url:'./../../alarmStatistics',
        params:{},
        failure:function(r,data){

        },
        success:function(r,data){
            var result = Ext.JSON.decode(r.responseText);
            console.log(result);
            var coloudOn={
                name:'云平台已处理告警',
                type:'pie',
                radius : '25%',
                center: ['25%', '25%'],
                data:[]
            };
            var coloudOf={
                name:'云平台未处理告警',
                type:'pie',
                radius : '25%',
                center: ['25%', '75%'],
                data:[]
            };
            var dataOn={
                name:'大数据已处理告警',
                type:'pie',
                radius : '25%',
                center: ['50%', '25%'],
                data:[]
            };
            var dataOf={
                name:'大数据未处理告警',
                type:'pie',
                radius : '25%',
                center: ['50%', '75%'],
                data:[]
            };
            var sON={
                name:'安全平台已处理告警',
                type:'pie',
                radius : '25%',
                center: ['75%', '25%'],
                data:[]
            };
            var soff={
                name:'安全平台未处理告警',
                type:'pie',
                radius : '25%',
                center: ['75%', '75%'],
                data:[]
            };
            for(var i=0;i<result.length;i++){
                var data=result[i];
                if(data.plateId==2){
                    if(data.step==2){
                        coloudOn.data.push({value:data.count,name:data.ruleName});
                    }else{
                        coloudOf.data.push({value:data.count,name:data.ruleName});
                    }
                }else if(data.plateId==3){
                    if(data.step==2){
                        dataOn.data.push({value:data.count,name:data.ruleName});
                    }else{
                        dataOf.data.push({value:data.count,name:data.ruleName});
                    }
                }else if(data.plateId==4){
                    if(data.step==2){
                        sON.data.push({value:data.count,name:data.ruleName});
                    }else{
                        soff.data.push({value:data.count,name:data.ruleName});
                    }
                }

            }
            var op={};
            Ext.apply(op,option);

            op.series=[].concat(coloudOn,coloudOf,dataOn,dataOf,sON,soff);
            console.log(op);
            require.config({
                paths : {
                    echarts : '../js/echart'
                }
            });
            require(
                [ 'echarts', 'echarts/chart/pie' ],

                function(ec) {
                    console.log(op);
                    var myChart = ec.init(document.getElementById('cloud-canv'));
                    console.log(myChart);
                    //console.log(initData());
                    myChart.setOption(op);

                });
        }
    });
}
