Ext.tree.Panel.addMembers({
    selectPathById: function(id){
        var me = this,
            node = me.getStore().getNodeById(id);
        if(node){
            me.selectPath(node.getPath());
        }
    }
});



Ext.define('WdatePickerTime',{
    extend:'Ext.form.TextField',
    xtype:'wdatetime',
    itemCls:'required-field',
    getRawValue: function() {
        var v = this.callParent();
        if(/\d{13}/.test(v)){
            v=Ext.Date.format(new Date(),'Y-m-d H:i:s');
            return v;
        }
        if(this.hidden){
            v=Ext.Date.format(new Date(),'Y-m-d H:i:s');
            return v;
        }
        v=v.replace('年','-');
        v=v.replace('月','-');
        v=v.replace('日','');
        //v=new Date(v).getTime();

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

Ext.define('Syu.Combox',{
    extend:'Ext.form.ComboBox',
    xtype:'syuCombo',
//	setValue:function(){
//		this.callParent(arguments);
//		var value=Array.prototype.slice.apply(arguments, [0])[0];
//		console.log('setvlaie');
//		console.log(this.store.data.items);
//	},
    getValue:function(){
        var value=this.callParent(arguments);
        return value;
    },
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
                var form=this.up('form');
                var input=form.down('hiddenfield[name="columnIndex"]');
                console.log(input);
                if(record.length>1){
                    for(var i=0;i<record.length;i++){
                        if(!record[i].data[valueField]){
                            combo.setValue(null);
                        }
                    }
                }
            });
        }
        this.on('beforequery',function(e){
            console.log('beforequery');
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
        });

    }

});

var treeStore = Ext.create('Ext.data.TreeStore', {
    // 根节点的参数是parentId
    nodeParam : 'parentId',
    // 根节点的参数值是0
    defaultRootId : -1,
    // 属性域
    fields : ['id','text','type'],
    // 数据代理
    proxy : {
        // 请求方式
        type : 'ajax',
        // 请求网址
        url : './../entity/tree'
    },
    root:{
    	text:'实体类型'
    	,iconCls:'icon-home'
    }
});
treeStore.load();

var tree=Ext.create('Ext.tree.Panel', {
    title: '关系字典配置',
    region:'west',
    store: treeStore,
    width:200,
    useArrows: true,
    frame: true,
    autoScroll: true,
    collapsible: true,
    containerScroll: true,
    split: true,
    listeners: {
	   beforeitemexpand: function (node, index, item, eOpts){
	      node.data.iconCls='icon-home'
	    },
	    beforeitemcollapse: function (node, index, item, eOpts){
	    	node.data.iconCls='icon-home'
	    },
        'select': function(node, record,item) {
        	if(record.isLeaf()){
        		if(Ext.getCmp('tab_'+record.raw.entityName)){
	    			var tab=Ext.getCmp('tab_'+record.raw.entityName);
	    			mainPanle.setActiveTab(tab);
	    			return;
	    		}
        		var grid=createGrid(record.raw);
				var panel=Ext.create('Ext.Panel',{
					id:'tab_'+record.raw.entityName,
					title:record.raw.entityName,
					closable:true,
					layout:'fit',
					items:[grid]
				});
				mainPanle.add(panel);
				mainPanle.setActiveTab(panel);
        	}

        }		
	    ,'itemcontextmenu': function(_this, record, item, index, e, eOpts) {
			//e.preventDefault();menu.showAt(e.getPoint());		
	    	var items=[];
            if(record.isLeaf()){
            	items.push({text:'删除当前节点',iconCls: 'report-add',handler:Ext.Function.pass(this.deleteEntity,[record.raw.id])});            	
            	items.push({text:'修改当前节点',iconCls:'report-edit',handler:Ext.Function.pass(this.editEntity,[record.raw])});
            }else{
            	items.push({text:'增加子节点',iconCls: 'report-add',handler:Ext.Function.pass(this.createEntity,[record.raw.id])});
            }
    		Ext.create('Ext.menu.Menu',{
    			plain: true,
    			margin: '0 0 10 0',
    			items:items
    		}).showAt(e.getPoint());

	    	
	    }   
		
    }
	,deleteEntity:function(id){
		Ext.Msg.confirm('!','确定删除选中记录？',function(btn){
	        if(btn=='no'){
	            return;							
	          }else{
            	Ext.Ajax.request({
            		method:'post',        		
            		url:'./../entity/deleteTree',
            		params:{entityId:id},
            		failure:function(r,data){

            		},
            		success:function(r,data){
            			var result = Ext.JSON.decode(r.responseText);
            			if(result.success=='true'){
            				Ext.Msg.alert('!','成功删除');
            				treeStore.load();
            			}

            		}
            	});
	          }
		})			
	}
	,createEntity:function(id){
		createEntityForm(id,'create');
	}
	,editEntity:function(entity){
		createEntityForm(entity,'update');
	}

});


function createEntityForm(entity,action){
	var id,parentId;
	if(action=='update'){
		id=entity.id;
		parentId=entity.parentId;
	}
	if(action=='create'){
		parentId=entity;
		id=null;
	}
	var form=Ext.create('Ext.form.Panel',{
		autoScroll: true,
		spilt:true,
		minHeight :75,
		layout:'column',
	    defaults: {
	        border: 0
	    },
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
                 	if(!form.isValid()){
                		Ext.Msg.alert('!','填入合法内容');
                		return;
                	}
                 	Ext.Ajax.request({
                		method:'post',        		
                		url:'./../entity/saveOrupdate',
                		params:data,
                		failure:function(r,data){

                		},
                		success:function(r,data){
                			var result = Ext.JSON.decode(r.responseText);
                			if(result.success=='true'){
                				Ext.Msg.alert('!','成功');
                				this.up('window').close();
                				treeStore.reload();
                			}else{
                				Ext.Msg.alert('!','错误');
                			}

                		},
                		scope:form
                	});
            	 }
             }	       
	       ],
	    items:[{columnWidth:.5,type:'form',padding:'10 0 0 5'
	    	,items:[
	    	       {
	    	    	   xtype:'combo',
	    	    	   store:Ext.create('Ext.data.Store',{
	    					fields:['value','text'],
	    					data : [
	    					         {text: '服务类型', value: '0'},
	    					         //{text: '字典类型', value: '1'},
	    					         {text: '记录组类型', value: '2'}
	    					     ]

	    				}),
	    			    fieldLabel: '节点类型',
	    			    name:'type',
	    			    displayField: 'text',
	    			    valueField: 'value',
	    			    allowBlank:false,    			    
	    			    triggerAction:'all'
	    	       }
	    	      ]
	    	},
			   {columnWidth:.5,type:'form',padding:'10 0 0 5'
			,items:[
					{
					   xtype:'textfield',
					   fieldLabel: '节点名称',
					   name:'entityName'
					}
			      ]
	    	}
	    	,{xtype:'hiddenfield',name:'entityId',value:id}
	    	,{xtype:'hiddenfield',name:'parentId',value:parentId}]
	});
	if(action=='update'){
		form.getForm().setValues({type:entity.type,entityName:entity.entityName});
	}
	var win=Ext.create('Ext.window.Window', {
		//id:'win_'+entity.entityName,
	    title: entity.entityName,
	    height: 200,
	    width: 800,
	    layout: 'fit',
	    items: [form] 
	});
	win.show();
}

function createGrid(entity){

	var columns=[],
		fields=[];
	if(entity.type=='0' || entity.type=='2'){
		columns=[
		    {dataIndex:'entityId',text:'ID',width:100},   
		    {dataIndex:'parentId',text:'父节点ID',width:100},
		    {dataIndex:'entityName',text:'名称',width:200},
		    {dataIndex:'hierarchy',text:'层级',hidden:true,width:200},
		    {dataIndex:'description',text:'描述',width:200},
		    {dataIndex:'type',text:'类型',hidden:true}
		    
		 ];
		fields=['entityId','parentId','entityName','hierarchy','description','type'];
	}else if(entity.type=='1'){
		columns=[
		    {dataIndex:'id',text:'ID',width:100},   
		    {dataIndex:'entityId',text:'实体ID',width:100},
		    {dataIndex:'name',text:'名称',width:100},
		    {dataIndex:'dicText',text:'字典字段',width:100},
		    {dataIndex:'dicValue',text:'字典值',width:100}
		 ];
		fields=['id','entityId','name','dicText','dicValue'];
	}
	var store=Ext.create('Ext.data.Store',{
		fields:fields,
		proxy : {
            type : 'ajax',
            url : './../entity/grid',
            extraParams:entity,
            reader: {
	            type: 'json',
	            root: 'content',
	            totalProperty:'totalElements'
	            }
         },
        autoLoad :true
		}
		
	);

	var grid=Ext.create('Ext.grid.Panel',{
		store:store
		,columns:columns
		,viewConfig:{
			enableTextSelection :true
		}
		,gridType:entity.type
		,parentEntityId:entity.entityId
		,parentEntityName:entity.entityName
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
					   iconCls:'icon-edit',
			           handler:function(){
							var grid=this.up('grid'),
								items=createGridFormItems(grid,'add'),
								buttons=fromButton(grid.gridType);
							var form=Ext.create('Ext.form.Panel',{
								autoScroll: true,
								spilt:true,
								minHeight :75,
								layout:'column',
							    defaults: {
							        border: 0
							    },
							    items:items,
							    buttons:buttons
							
							});
							Ext.create('Ext.window.Window', {
		        				//id:'win_'+entity.entityName,
		        			    title: '修改',
		        			    height: 200,
		        			    width: 800,
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
							var	items=createGridFormItems(grid,'update'),
								buttons=fromButton(grid.gridType);
							
							
							
							var form=Ext.create('Ext.form.Panel',{
								autoScroll: true,
								spilt:true,
								minHeight :75,
								layout:'column',
							    defaults: {
							        border: 0
							    },
							    items:items,
							    buttons:buttons
							});
							
		        			var data=grid.getSelectionModel().getSelection()[0].getData();
		        			form.getForm().setValues(data);
			        		
							Ext.create('Ext.window.Window', {
		        				//id:'win_'+entity.entityName,
		        			    title: '修改',
		        			    height: 200,
		        			    width: 800,
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
			        	   Ext.Ajax.request({
		                		method:'post',        		
		                		url:'./../entity/delete',
		                		params:{
		                			id:grid.gridType=='1'?data.id:data.entityId,
		                			type:grid.gridType
		                		},
		                		failure:function(r,data){

		                		},
		                		success:function(r,data){
		                			var result = Ext.JSON.decode(r.responseText);
		                			if(result.success=='true'){
		                				Ext.Msg.alert('!','成功');
		                			
		                			}else{
		                				Ext.Msg.alert('!','错误');
		                			}

		                		}
		                	});
			           }
			       }
			    ]
		    }         
		]
	});
	return grid;
}

function createGridFormItems(grid,action){
	console.log(grid.parentEntityName);
	var formItems=[{columnWidth:.5,type:'form',padding:'10 0 0 5',items:[]},
                   {columnWidth:.5,type:'form',padding:'10 0 0 5',items:[]}];
	if(grid.gridType=='1'){
		formItems[0].items.push({
			xtype:'textfield',
			fieldLabel: '字典字段',
			name:'dicText',
			allowBlank:false
		});
		formItems[1].items.push({
			xtype:'textfield',
			fieldLabel: '字典值',
			name:'dicValue',
			allowBlank:false
		});
		formItems.push({xtype:'hiddenfield',name:'entityId',value:grid.parentEntityId});
		formItems.push({xtype:'hiddenfield',name:'name',value:grid.parentEntityName});
	}else{
		formItems[0].items.push({
			xtype:'textfield',
			fieldLabel: '名称',
			name:'entityName',
			allowBlank:false
		});
		formItems[1].items.push({
			xtype:'textfield',
			fieldLabel: '描述',
			name:'description'
		    //allowBlank:false
		});
		formItems.push({xtype:'hiddenfield',name:'parentId',value:grid.parentEntityId});
		if(grid.gridType=='0'){
			formItems.push({xtype:'hiddenfield',name:'type',value:4});
		}
		if(grid.gridType=='2'){
			formItems.push({xtype:'hiddenfield',name:'type',value:3});
		}
	}
	if(action=='update'){
		var data=grid.getSelectionModel().getSelection()[0].getData();
		formItems.push({xtype:'hiddenfield'
				,name:grid.gridType=='1'?'id':'entityId'
				,value:grid.gridType=='1'?data.id:data.entityId
				})
	}
	return formItems;
	
}

function fromButton(type){
	var url='';
	if(type=='1'){
		url='./../entity/saveOrupdateDic'
	}else{
		url='./../entity/saveOrupdate'
	}
	var buttons=[
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
		    	if(!form.isValid()){
		   		Ext.Msg.alert('!','填入合法内容');
		   			return;
		    	}
		    	Ext.Ajax.request({
			   		method:'post',        		
			   		url:url,
			   		params:data,
			   		failure:function(r,data){
			
			   		},
			   		success:function(r,data){
			   			var result = Ext.JSON.decode(r.responseText);
			   			if(result.success=='true'){
			   				Ext.Msg.alert('!','成功');
			   				this.up('window').close();
			   			}else{
			   				Ext.Msg.alert('!','错误');
			   			}
			
			   		},
			   		scope:form
		    	});
			 }
		
		}	   
	   ];
	return buttons;
}

var mainPanle=Ext.create('Ext.tab.Panel',{
    region:'center',
    layout: 'fit'
});

Ext.onReady(function(){
    Ext.getDoc().on("contextmenu", function(e){
	    e.stopEvent();
	});
    var view=new Ext.Viewport({
        layout:'border',
        items: [tree,mainPanle]
    });
});