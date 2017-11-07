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
        	console.log(record.raw);
	        if(record.raw.hierarchy=2){
	        	var grid=createGrid(record.raw.id);
    			var panel=Ext.create('Ext.Panel',{
					id:'tab_'+record.raw.text,
					title:record.raw.text,
					closable:true,
					layout:'fit',
					items:[grid]
				});
				mainPanle.add(panel);
				mainPanle.setActiveTab(panel);
	        	
	        }

        }		
	    ,'itemcontextmenu': function(_this, record, item, index, e, eOpts) {

	    }   
		
    }

});


function createGrid(plateId){
  var columns=[
               {dataIndex:'id',text:'ID',hidden:true,width:100},
               {dataIndex:'ruleId',text:'告警规则ID',hidden:true,width:100},
               {dataIndex:'ruleName',text:'告警规则名称',width:100},
               {dataIndex:'roleId',text:'角色ID',hidden:true,width:100},
               {dataIndex:'step',text:'处理步骤',width:80,
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
               },
               {dataIndex:'groupId',text:'记录组ID',hidden:true,width:200},
               {dataIndex:'gcode',text:'记录组编码',hidden:true,width:100},
               {dataIndex:'gname',text:'记录组名称',width:150},
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
       var fields=['id','ruleId','ruleName','roleId','step','groupId','gcode','gname','recordId',
                   'rcode','rname','alarmType','valveValue','equalType','equalType','alarmValue','info'];
       var store=Ext.create('Ext.data.Store',{
           fields:fields,
           proxy : {
               type : 'ajax',
               url : './../alarm/alarms',
               extraParams:{plateId:plateId},
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
      ,forceFit:true
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
                    }]
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
			        alarm.info=data.info
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
                 allowblank: false
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