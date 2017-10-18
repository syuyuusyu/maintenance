Ext.override(Ext.form.ComboBox,{
	setValue: function(value) {
		var result=this.callParent(arguments);
		console.log(this);
		console.log(value);
		//this.fireEvent('change', this);
        return result;
    }
})

Ext.define('valueCombo',{
	extend:'Ext.form.ComboBox',
	xtype:'valueCombo',
    store:Ext.create('Ext.data.Store',{
    	fields:['value','text'],
        data : [
            {text: '大于等于', value: 2},
            {text: '小于等于', value: 3}
        ]										
    }),
    fieldLabel: '阀值操作',
    name:'equalType',
    displayField: 'text',
    valueField: 'value',
    allowBlank:false,
    triggerAction:'all',
    listeners:{
    	change:function(_this,record){
           
        }
    }

});
Ext.define('statueCombo',{
	extend:'Ext.form.ComboBox',
	xtype:'statueCombo',
    store:Ext.create('Ext.data.Store',{
    	fields:['value','text'],
        data : [
            {text: '等于', value: 0},
            {text: '不等于', value: 1}
        ]										
    }),
    fieldLabel: '阀值操作',
    name:'equalType',
    displayField: 'text',
    valueField: 'value',
    allowBlank:false,
    triggerAction:'all',
    listeners:{
    	change:function(_this,record){
           
        }
    }

});


Ext.define('valveValue',{
	extend:'Ext.form.Number',
	xtype:'valveValue',
	name:'valveValue',
	fieldLabel:'阀值',
	allowBlank:false
});

Ext.define('valveStatue',{
	extend:'Ext.form.ComboBox',
	xtype:'valveStatue',
	store:Ext.create('Ext.data.Store',{
        fields:['dicText','dicValue'],
        proxy: {
            type: 'ajax',
            url:'./../alarmRule/dictionary',
            extraParams:{},
            reader: {
                type: 'json',
                root: 'result'
            },
            scope:this
        },
        autoLoad:false
    }),
    fieldLabel: '告警状态',
    name:'valveValue',
    displayField: 'dicText',
    valueField: 'dicValue',
    allowBlank:false,
    triggerAction:'all'
});

var store=Ext.create('Ext.data.Store',{
    fields:['id','roleId','relevantPlate','relevantGroup','relevantRecord','name','plateName','groupName','recordName','type','roleName','equalType','valveValue','alarmLevel','action'],
    proxy : {
        type : 'ajax',
        url : './../alarmRule/grid',
        extraParams:{},
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
    ,columns:[
        {dataIndex:'id',text:'ID',width:50},
        {dataIndex:'roleId',text:'角色ID',width:100,hidden:true},
        {dataIndex:'relevantPlate',text:'relevantPlate',hidden:true},
        {dataIndex:'relevantGroup',text:'relevantGroup',hidden:true},
        {dataIndex:'relevantRecord',text:'relevantRecord',hidden:true},
        {dataIndex:'name',text:'规则名称',width:100},
        {dataIndex:'plateName',text:'所属平台',width:100},
        {dataIndex:'groupName',text:'组名称',width:200},
        {dataIndex:'recordName',text:'记录名称',width:200},

        {dataIndex:'type',text:'告警类型',width:100,
            renderer:function(v){
                switch(v){
                    case '0':
                        return '状态告警';
                    case '1':
                        return '阀值告警';
                    default:
                        return v;
                }
            }
        },
        {dataIndex:'roleName',text:'角色名称',width:100 },
        {dataIndex:'equalType',text:'阀值操作',width:100,
            renderer:function(v){
                switch(v){
                    case '0':
                        return '等于';
                    case '1':
                        return '不等于';
                    case '2':
                        return '大于等于';
                    case '3':
                        return '小于等于';
                    default:
                        return v;
                }
            }
        },
        {dataIndex:'valveValue',text:'阀值',width:150},
        {dataIndex:'alarmLevel',text:'等级',width:50},
        {dataIndex:'action',text:'动作',width:200,hidden:true}

    ]
    ,viewConfig:{
        enableTextSelection :true
    }
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
        },{
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'button',
                    text: '增加',
                    iconCls:'icon-add',
                    handler:function(){
                        var grid=this.up('grid');
                        var form=createForm();
                        Ext.create('Ext.window.Window', {
                            //id:'win_'+entity.entityName,
                            title: '修改',
                            height: 200,
                            width: 830,
                            layout: 'fit',
                            items: [form]
                        }).show()
                    }
                },{
                    xtype: 'button',
                    text: '修改',
                    iconCls:'icon-edit',
                    handler:function(){
                        var grid=this.up('grid');
                        if(grid.getSelectionModel().getSelection().length==0){
                            Ext.Msg.alert('!','先选中一条');
                            return;
                        }
                        var form=createForm();
                        console.log('----------------');
                        var data=grid.getSelectionModel().getSelection()[0].getData();
                        console.log(data);
                        form.getForm().setValues(data);
                        form.down('combo[name="relevantRecord"]').setValue(data.relevantRecord);

                        Ext.create('Ext.window.Window', {
                            //id:'win_'+entity.entityName,
                            title: '修改',
                            height: 200,
                            width: 830,
                            layout: 'fit',
                            items: [form]
                        }).show()
                    }
                },{
                    xtype: 'button',
                    text: '删除',
                    iconCls:'icon-delete',
                    handler:function(){
                        var grid=this.up('grid');
                        if(grid.getSelectionModel().getSelection().length==0){
                            Ext.Msg.alert('!','先选中一条');
                            return;
                        }
                        var data=grid.getSelectionModel().getSelection()[0].getData();
                        console.log(data);
                        Ext.Msg.confirm('!','确定删除选中记录？',function(btn){
                            if(btn=='no'){
                                return;
                            }else{
                                Ext.Ajax.request({
                                    method:'post',
                                    url:'./../alarmRule/delete',
                                    params:{
                                        id:data.id
                                    },
                                    failure:function(r,data){

                                    },
                                    success:function(r,data){
                                        var result = Ext.JSON.decode(r.responseText);
                                        if(result.success=='true'){
                                            Ext.Msg.alert('!','成功');
                                            grid.getStore().reload();
                                        }else{
                                            Ext.Msg.alert('!','错误');
                                        }

                                    }
                                });
                            }
                        });
                    }
                }
            ]
        }
    ]
});

function createForm(){
    var form=Ext.create('Ext.form.Panel',{
        autoScroll: true,
        spilt:true,
        minHeight :75,
        layout:'column',
        defaults: {
            border: 0
        },
        items:[
            {columnWidth:.33,type:'form',padding:'10 0 0 5',id:'colum1'
                ,items:[
                {xtype:'textfield',fieldLabel:'规则名称',name:'name',allowblank:false},
                {
                    xtype:'combo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['id','entityName'],
                        proxy: {
                            type: 'ajax',
                            url:'./../alarmRule/entity',
                            extraParams:{parentId:1},
                            reader: {
                                type: 'json',
                                root: 'result'
                            },
                            scope:this
                        },
                        autoLoad:true


                    }),
                    fieldLabel: '所属平台',
                    name:'relevantPlate',
                    displayField: 'entityName',
                    valueField: 'id',
                    allowBlank:false,
                    triggerAction:'all',
                    listeners:{
                    	change:function(_this,record){       
                    		
                            var combo=_this.up('form').down('combo[name="relevantGroup"]');
                            combo.clearValue();
                            combo.store.getProxy().setExtraParam('parentId',record);
                            combo.store.reload();
                        }
                    }
                },
                {
				    xtype:'combo',
				    store:Ext.create('Ext.data.Store',{
				    	fields:['value','text'],
                        data : [
                            {text: '阀值告警', value: 1},
                            {text: '状态告警', value: 0}
                        ]									
				    }),
				    fieldLabel: '告警类型',
				    name:'type',
				    displayField: 'text',
				    valueField: 'value',
				    allowBlank:false,
				    triggerAction:'all',
				    editable :false,
				    disabled:true,
				    listeners:{
                    	change:function(_this,record){                    		
                            var column2=this.up('form').down('[id="colum2"]'),
                            	column3=this.up('form').down('[id="colum3"]'),
                            	typeCombo=column2.down('[name="equalType"]'),
                            	valueCombo=column3.down('[name="valveValue"]'),
                            	recordCombo=this.up('form').down('[name="relevantRecord"]');
                            	//valveStatue
                            column2.remove(typeCombo);
                            column3.remove(valueCombo);
                            switch(record){
                            case 0:
                            	column2.add({xtype:'statueCombo'});
                            	column3.add({xtype:'valveStatue'});
                            	column3.down('[name="valveValue"]')
                            		.getStore()
                            		.getProxy()
                            		.setExtraParam('entityId',recordCombo.getValue());
                            	break;
                            case 1:
                            	column2.add({xtype:'valueCombo'});
                            	column3.add({xtype:'valveValue'});
                            	break;
                            }
                        }
                    }
				}
               
            ]
            },
            {columnWidth:.33,type:'form',padding:'10 0 0 5',id:'colum2'
                ,items:[
                    {xtype:'numberfield',fieldLabel:'告警等级',name:'alarmLevel',allowblank:false},
					{
					    xtype:'combo',
					    store:Ext.create('Ext.data.Store',{
					        fields:['id','entityName'],
					        proxy: {
					            type: 'ajax',
					            url:'./../alarmRule/entity',
					            extraParams:{parentId:-1},
					            reader: {
					                type: 'json',
					                root: 'result'
					            },
					            scope:this
					        },
					        autoLoad:false										
					    }),
					    fieldLabel: '组名称',
					    name:'relevantGroup',
					    displayField: 'entityName',
					    valueField: 'id',
					    allowBlank:false,
					    triggerAction:'all',
					    listeners:{
					    	change:function(_this,record){
					    		alert(record);
	                            var combo=_this.up('form').down('combo[name="relevantRecord"]');
	                            combo.clearValue();
	                            combo.store.getProxy().setExtraParam('parentId',record);
	                            combo.store.reload();
					        }
					    }
					},		
					{
					    xtype:'valueCombo'
					}
            ]
            },
            {columnWidth:.33,type:'form',padding:'10 0 0 5',id:'colum3'
                ,items:[
				{
				    xtype:'combo',
				    store:Ext.create('Ext.data.Store',{
				        fields:['roleid','rolename'],
				        proxy: {
				            type: 'ajax',
				            url:'./../alarmRule/roles',
				            reader: {
				                type: 'json',
				                root: 'result'
				            },
				            scope:this
				        },
				        autoLoad:true					
				    }),
				    fieldLabel: '相关角色',
				    name:'roleId',
				    displayField: 'rolename',
				    valueField: 'roleid',
				    allowBlank:false,
				    triggerAction:'all',
				    listeners:{
				    	select:function(_this,record){
				
				        }
				    }
				},
                {
					    xtype:'combo',
					    store:Ext.create('Ext.data.Store',{
					        fields:['id','entityName','type'],
					        proxy: {
					            type: 'ajax',
					            url:'./../alarmRule/entity',
					            extraParams:{parentId:-1},
					            reader: {
					                type: 'json',
					                root: 'result'
					            },
					            scope:this
					        },
					        autoLoad:false,
					        listeners:{
					        	beforeload:function(store){

					        	}
					        }
					    }),
					    fieldLabel: '记录名称',
					    name:'relevantRecord',
					    displayField: 'entityName',
					    valueField: 'id',
					    allowBlank:false,
					    triggerAction:'all',
					    listeners:{
					    	select:function(_this,record){
					            var combo=this.up('form').down('combo[name="type"]');
					            //6:记录类型关联字典 7:记录类型不关联字典
					            switch(record[0].data.type){
					            	case '6':
					            		combo.setValue(0);
					            		break;
					            	case '7':
					            		combo.setValue(1);
					            		break;
					            	default :
					            		combo.setValue(null);	
					            }
					        }
					    }
				 },
				 {xtype:'valveValue'}
            ]
            },
            {xtype:'hiddenfield',name:'id',value:null}
        ],
        buttons:[
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
                         data.type=form.down('combo[name="type"]').getValue();
                         console.log(data)
                         if(!form.isValid()){
                             Ext.Msg.alert('!','填入合法内容');
                             return;
                         }
                         Ext.Ajax.request({
                             method:'post',
                             url:'./../alarmRule/saveOrupdate',
                             params:data,
                             failure:function(r,data){

                             },
                             success:function(r,data){
                                 var result = Ext.JSON.decode(r.responseText);
                                 if(result.success=='true'){
                                     Ext.Msg.alert('!','成功');
                                     this.up('window').close();
                                     grid.getStore().reload();
                                 }else{
                                     Ext.Msg.alert('!','错误');
                                 }

                             },
                             scope:form
                         });
                     }

                 }
             ]
    });
    return form;
}


var panel=Ext.create('Ext.Panel',{
    title:'告警规则配置',
    layout:'fit',
    region:'center',
    items:[grid]
});




Ext.onReady(function(){
    Ext.getDoc().on("contextmenu", function(e){
        e.stopEvent();
    });
    var view=new Ext.Viewport({
        layout:'border',
        items: [panel]
    });
});