Ext.define('Syu.Combox',{
    extend:'Ext.form.ComboBox',
    xtype:'syuCombo',
    initComponent:function(){
        this.callParent(arguments);
        this.tpl=Ext.create('Ext.XTemplate',
            '<tpl for=".">',
            '<div class="x-boundlist-item" >',
            '{[typeof values === "string" ? values : values["'+this.displayField+'"]?values["'+this.displayField+'"]:"&nbsp"   ]}',
            '</div>',
            '</tpl>');
        var mc=this.store.getProxy().getModel(),
            mod=new mc(),
            displayField=this.displayField,
            valueField=this.valueField;
        mod.set(displayField,'');
        mod.set(valueField,null);
        this.store.on('load',Ext.Function.pass(function(store,combo){
            store.insert(0,mod);
        },[this.store,this]));
        if(this.multiSelect){
            this.on('select',function(combo,record){
                if(record.length>1){
                    for(var i=0;i<record.length;i++){
                        if(!record[i].data[valueField]){
                            combo.setValue(null);
                        }
                    }
                }
            });
        }

    }
    ,getValue:function () {
        var v = this.callParent();
        console.log(v);
        return v;
    }
    , getRawValue: function() {
        var v = this.callParent();
        console.log(v);
        return v;

    }
    ,listeners:{
        beforequery:function(e){
            var combo = e.combo;
            if(!e.forceAll){
                var input = e.query;
                // 检索的正则
                var regExp = new RegExp('.*'+input+'.*','i');
                // 执行检索
                combo.store.filterBy(function(record,id){
                    var text = record.get(combo.displayField);
                    return regExp.test(text);
                });
                combo.expand();
                return false;
            }
        }
    }
});


Ext.define('WdatePickerTime',{
    extend:'Ext.form.TextField',
    itemCls:'required-field',
    xtype:'WdatePickerTime',
    getRawValue: function() {
        var v = this.callParent();
        v=v.replace('年','-');
        v=v.replace('月','-');
        v=v.replace('日','');
        return v;

    },
    listeners : {
        render : function(p) {
            Ext.get(p.getInputId()).on('click', function() {
                WdatePicker({
                    dateFmt : 'yyyy年MM月dd日 HH:mm:ss',
                    realFullFmt : 'yyyy-MM-dd HH:mm:ss',
                    readOnly : true
                    //,vel : 'startTime'
                });
            });
        }
    }
});

var mainPanle=Ext.create('Ext.tab.Panel',{
    region:'center',
    layout: 'fit'
});



var treeStore = Ext.create('Ext.data.TreeStore', {
    root: {
        expanded: true,
        children: [
            { text: "云平台告警", leaf: true,hierarchy:2,iconCls:'icon-cloud',id:2 },
            { text: "大数据平台告警", leaf:true,hierarchy:2,iconCls:'icon-data',id:3 },
            { text: "安全平台告警", leaf: true ,hierarchy:2,iconCls:'icon-security',id:4}
        ]
    }
});


var tree=Ext.create('Ext.tree.Panel', {
    title: '告警处理',
    region:'west',
    store: treeStore,
    width:200,
    useArrows: true,
    frame: true,
    autoScroll: true,
    collapsible: true,
    containerScroll: true,
    split: true,
    rootVisible: false,
    listeners: {
	   beforeitemexpand: function (node, opt){

	    },
	    beforeitemcollapse: function (node, opt){
	    	
	    },
        'select': function(node, record,item) {
	        if(record.raw.hierarchy=2){
	            if(Ext.getCmp('tab_'+record.raw.text+"0")){
                    mainPanle.setActiveTab(Ext.getCmp('tab_'+record.raw.text+"0"));
                    return;
                }
                if(Ext.getCmp('tab_'+record.raw.text+"2")){
                    mainPanle.setActiveTab(Ext.getCmp('tab_'+record.raw.text+"2"));
                    return;
                }
	        	var grid1=createGrid(record.raw.id,"0");
    			var panel1=Ext.create('Ext.Panel',{
					id:'tab_'+record.raw.text+"0",
					title:record.raw.text+"待处理",
					closable:true,
					layout:'fit',
					items:[grid1]
				});
                var grid2=createGrid(record.raw.id,"2");
                var panel2=Ext.create('Ext.Panel',{
                    id:'tab_'+record.raw.text+"2",
                    title:record.raw.text+"已处理",
                    closable:true,
                    layout:'fit',
                    items:[grid2]
                });
				mainPanle.add(panel1);
                mainPanle.add(panel2);
				mainPanle.setActiveTab(panel1);
	        	
	        }

        }		
	    ,'itemcontextmenu': function(_this, record, item, index, e, eOpts) {

	    }   
		
    }

});


function createGrid(plateId,step){
  var columns=[
               {dataIndex:'id',text:'ID',hidden:true,width:100},
               {dataIndex:'ruleId',text:'告警规则ID',hidden:true,width:100},
               {dataIndex:'ruleName',text:'告警规则名称',width:200},
               {dataIndex:'roleId',text:'角色ID',hidden:true,width:100},
                (function(){
                    if(step==0){
                        return {
                            dataIndex:'step', text:'处理步骤',width:110,
                            renderer:function(value){
                                switch(value){
                                    case '0':
                                        return "<input type='button' style='color:red' value='处理告警'>";
                                    case '1':
                                        return "<input type='button' style='color:red' value='处理告警'>'";
                                    case '2':
                                        return "<input type='button' style='color:green' value='查看处理结果'>";;
                                }
                        }
                        }
                    }else{
                        return {dataIndex:'handler',text:'处理人',width:100};
                    }
                })(),
                (function(){
                    if(step==2){
                        return {dataIndex:'info',text:'告警处理记录',width:200};
                    }
                    return {};
                })(),

                {dataIndex:'upId',text:'资源ID',width:200},
                {dataIndex:'createTime',text:'生成时间',width:170,
                    renderer:function(value){
                       return Ext.Date.format(new Date(value), 'Y年m月d日 H:i:s');

                    }
                },
               {dataIndex:'groupId',text:'记录组ID',hidden:true,width:200},
               {dataIndex:'gcode',text:'记录组编码',hidden:true,width:100},
               {dataIndex:'gname',text:'记录组名称',width:250},
               {dataIndex:'recordId',text:'记录ID',hidden:true,width:200},
               {dataIndex:'rcode',text:'记录编码',hidden:true,width:100},
               {dataIndex:'rname',text:'记录名称',width:150},
               {dataIndex:'alarmType',text:'告警类型',width:100,
            	   renderer:function(value){               
		   				switch(value){
						case '0':
							return '状态告警';
						case '1':
							return '阀值告警';
					}				
				}
               },
               {dataIndex:'valveValue',text:'阀值',width:150},
               {dataIndex:'equalType',text:'比较类型',width:100,
            	   renderer:function(value){               
		   				switch(value){
						case '0':
							return '等于';
						case '1':
							return '不等于';	
						case '2':
							return '大于等于';
						case '3':
							return '小于等于';
					}	
            	   }
               },
               {dataIndex:'alarmValue',text:'实际值',width:150},
               {dataIndex:'info',text:'处理信息',hidden:true,width:150}
               ];
       console.log(columns);
       var fields=['id','ruleId','ruleName','roleId','step','upId','createTime','groupId','gcode','gname','recordId',
                   'rcode','rname','alarmType','valveValue','equalType','equalType','alarmValue','handler','info'];
       var store=Ext.create('Ext.data.Store',{
           fields:fields,
           proxy : {
               type : 'ajax',
               url : './../alarm/alarms',
               extraParams:{plateId:plateId,step:step},
               reader: {
                   type: 'json',
                   root: 'content',
                   totalProperty:'totalElements'
               }
           },
           autoLoad :true
       });

	var grid=Ext.create('Ext.grid.Panel',{
      store:store
      ,columns:columns
      ,viewConfig:{
          enableTextSelection :true
      }	     
      ,plateId:plateId
        ,step:step
      //,forceFit:true
      ,autohight:true
      ,autoScroll:true
      ,selModel: Ext.create("Ext.selection.CheckboxModel", {
          injectCheckbox: 0,//checkbox位于哪一列，默认值为0
          mode: "single",//multi,simple,single；默认为多选multi
          checkOnly: false,//如果值为true，则只用点击checkbox列才能选中此条记录
          allowDeselect: true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
          enableKeyNav: true
      })
      ,dockedItems:[
                    {
                        xtype: 'pagingtoolbar',
                        store: store,
                        dock: 'bottom',
                        displayInfo: true
                    },{
                        xtype: 'toolbar',
                        dock: 'top',
                        height :35,
                        items:[
                            {
                                xtype:'syuCombo',
                                store:Ext.create('Ext.data.Store',{
                                    fields:['id','name'],
                                    proxy: {
                                        type: 'ajax',
                                        url:'./../alarm/alarmRule',
                                        reader: {
                                            type: 'json',
                                            root: 'result'
                                        },
                                        scope:this
                                    },
                                    autoLoad:true
                                }),
                                fieldLabel: '告警规则名称',
                                name:'ruleId',
                                displayField: 'name',
                                valueField: 'id',
                                allowBlank:true,
                                triggerAction:'all'
                                //multiSelect:true,
                                //width:200,

                            },'-',{
                                xtype:'WdatePickerTime',
                                name: 'startTime',
                                fieldLabel: '生成开始时间',
                                anchor: '100%',
                                allowBlank:true
                            },'-',{
                                xtype:'WdatePickerTime',
                                name: 'endTime',
                                fieldLabel: '生成结束时间',
                                anchor: '100%',
                                allowBlank:true
                            },'->',
                            {
                                xtype: 'button',
                                text : '查询',
                                iconCls:'icon-search',
                                handler:function(){
                                    var grid=this.up('grid');
                                    var toolbar=this.up('toolbar');
                                    var query={
                                        plateId:grid.plateId,
                                        step:grid.step,
                                        ruleId:toolbar.down('[name="ruleId"]').getValue(),
                                        startTime:toolbar.down('[name="startTime"]').getValue()?toolbar.down('[name="startTime"]').getValue():Ext.Date.format(new Date(2000), 'Y-m-d H:i:s'),
                                        endTime:toolbar.down('[name="endTime"]').getValue()?toolbar.down('[name="endTime"]').getValue():Ext.Date.format(new Date(), 'Y-m-d H:i:s')
                                    }
                                    for(p in query){
                                        grid.store.getProxy().setExtraParam(p,query[p]);
                                    }
                                    grid.store.reload();
                                }
                            }

                        ]
                    }
        ]
      ,listeners:{
     	cellclick:function( _this, td, cellIndex, record, tr, rowIndex, e, eOpts ){
     		console.log(_this);
     		if(cellIndex==5){
     			alarmInfo(record,e,_this);
     		}
     	}
    }
      
	  });
	  return grid;
	  						
	}

function alarmInfo(record,e,grid){
	console.log(record.raw);
	var alarm=record.raw;
	var buttons=[];
	if(record.raw.step==1||record.raw.step==0){
		buttons=[
			{
			    text: '重置',
			    handler: function() {
			        this.up('form').getForm().reset();
			    }
			},{
			    text:'提交',
			    handler:function(){
			        var form = this.up('form');
			        var baseForm=form.getForm();
			        var data = baseForm.getValues();
			        Ext.apply(alarm,data);
                    //alarm.createTime=new Date(alarm.createTime);
                    alarm.createTime=Ext.Date.format(new Date(alarm.createTime), 'Y-m-d H:i:s');
			       // delete alarm.createTime;

			        if(!form.isValid()){
			            Ext.Msg.alert('!','填入合法内容');
			            return;
			        }
			        Ext.Ajax.request({
			            method:'post',
			            url:'./../alarm/update',
			            params:alarm,
			            failure:function(r,data){
			
			            },
			            success:function(r,data){
			                var result = Ext.JSON.decode(r.responseText);
			                if(result.success=='true'){
			                    Ext.Msg.alert('!','成功');
			                    this.up('window').close();
			                    console.log(this.up('grid'));
			                    grid.getStore().reload();
			                }else{
			                    Ext.Msg.alert('!','错误');
			                }
			
			            },
			            scope:form
			        });
			    }
			
			}     		         
		];
	}


	var form=Ext.create('Ext.form.Panel',{
        autoScroll: true,
        spilt:true,
        minHeight :75,
        width      : 400,
        bodyPadding: 10,
        defaults: {
            border: 0
        },
        items:[
             {
                 xtype     : 'textareafield',
                 grow      :  true,
                 name      : 'info',
                 fieldLabel: '',
                 height    : 150,
                 anchor    : '100%',
                 value	   : record.raw.info,
                 allowBlank: false
             },{
                xtype : 'textfield',
                name :'handler',
                fieldLabel: '处理人',
                allowBlank: false
            }
        ],
        buttons:buttons

    });

    Ext.create('Ext.window.Window', {
        //id:'win_'+entity.entityName,
        title: '告警处理信息',
        height: 240,
        width: 400,
        layout: 'fit',
        items: [form]
    }).showAt(e.getPoint());   
}






Ext.onReady(function(){
    Ext.getDoc().on("contextmenu", function(e){
	    e.stopEvent();
	});
    var view=new Ext.Viewport({
        layout:'border',
        items: [tree,mainPanle]
    });
});