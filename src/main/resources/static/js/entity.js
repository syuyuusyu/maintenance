var treeStore = Ext.create('Ext.data.TreeStore', {
    // 根节点的参数是parentId
    nodeParam : 'parentId',
    // 根节点的参数值是0
    defaultRootId : 1,
    // 属性域
    fields : ['id','text','type'],
    // 数据代理
    proxy : {
        // 请求方式
        type : 'ajax',
        // 请求网址
        url : './../entityConf/tree'
    },
    root:{
    	text:'实体'
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
	   beforeitemexpand: function (node, opt){
	      //node.data.iconCls='icon-home';
	      console.log("beforeitemexpand");
	      console.log(node);
	      console.log(opt);

	    },
	    beforeitemcollapse: function (node, opt){
            //node.data.iconCls='icon-home';
            console.log("beforeitemcollapse");
            console.log(node);
            console.log(opt);
	    },
        'select': function(node, record,item) {
	        if(record.raw.type=='7'){
	            return;
            }
        	if(record.isLeaf() || record.raw.type=='5'){
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
            // var items=[];
            // if(record.isLeaf()){
            	// items.push({text:'删除当前节点',iconCls: 'report-add',handler:Ext.Function.pass(this.deleteEntity,[record.raw.id])});
            	// items.push({text:'修改当前节点',iconCls:'report-edit',handler:Ext.Function.pass(this.editEntity,[record.raw])});
            // }else{
            	// items.push({text:'增加子节点',iconCls: 'report-add',handler:Ext.Function.pass(this.createEntity,[record.raw.id])});
            // }
            // Ext.create('Ext.menu.Menu',{
    			// plain: true,
    			// margin: '0 0 10 0',
    			// items:items
            // }).showAt(e.getPoint());

	    	
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

//1:主机类型 2:云平台应用程序 3:大数据平台应用程序 4:安全平台应用程序 5:记录组类型 6:记录类型关联字典 7:记录类型不关联字典
function typeRender(v){
    switch(v){
        case '1':
            return '主机类型';
        case '2':
            return '云平台应用程序';
        case '3':
            return '大数据平台应用程序';
        case '4':
            return '安全平台应用程序';
        case '5':
            return '记录组类型';
        case '6':
            return '记录类型关联字典';
        case '7':
            return '记录类型不关联字典';
        case '8':
        	return '记录类型ID标识';
        default :
            return v;
    }
}

function createGrid(entity){
	var entityCode,entityName;
	switch (entity.type){
		case '1':
			entityCode='主机编码';
			entityName='主机名称';
			break;
        case '2':
            entityCode='云平台应用编码';
            entityName='云平台应用名称';
            break;
        case '3':
            entityCode='大数据平台应用编码';
            entityName='大数据平台应用名称';
            break;
        case '4':
            entityCode='安全平台应用编码';
            entityName='安全平台应用名称';
            break;
        case '5':
            if(entity.hierarchy==1){
                entityCode='记录组类型编码';
                entityName='记录组类型名称';
            }else{
                entityCode='记录类型编码';
                entityName='记录类型名称';
            }
            break;
        // case '6':

        break;

	}

    var  columns=[
            {dataIndex:'entityId',text:'ID',width:100},
            {dataIndex:'parentId',text:'父节点ID',width:100},
            {dataIndex:'entityCode',text:entityCode,width:100},
            {dataIndex:'entityName',text:entityName,width:200},
            {dataIndex:'hierarchy',text:'层级',hidden:true,width:200},
            {dataIndex:'type',text:'类型',renderer:typeRender,width:100}

        ];
    var fields=['entityId','parentId','entityCode','entityName','hierarchy','type'];
    if(entity.type=='6'){
        columns=[
            {dataIndex:'id',text:'ID',width:100},
            {dataIndex:'entityId',text:'父节点ID',width:100},
            {dataIndex:'name',text:'字典名称',width:100},
            {dataIndex:'dicText',text:'字典字段',width:200},
            {dataIndex:'dicValue',text:'字典值',width:200}

        ];
        fields=['id','entityId','name','dicText','dicValue'];
    }


    var store=Ext.create('Ext.data.Store',{
            fields:fields,
            proxy : {
                type : 'ajax',
                url : './../entityConf/grid',
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
        ,gridHierarchy:entity.hierarchy
        ,parentEntityId:entity.entityId
        ,parentEntityName:entity.entityName
        ,entityCodeText:entityCode
        ,entityNameText:entityName
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
                            var grid=this.up('grid'),
                                items=createGridFormItems(grid,'add'),
                                buttons=gridFromButton(grid);
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
                                buttons=gridFromButton(grid);



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

                            Ext.Msg.confirm('!','确定删除选中记录？',function(btn){
                                if(btn=='no'){
                                    return;
                                }else{
                                    Ext.Ajax.request({
                                        method:'post',
                                        url:'./../entityConf/delete',
                                        params:{
                                            id:data.entityId,
                                            type:grid.gridType
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
    return grid;
}


function createGridFormItems(grid,action){
    console.log(grid.gridHierarchy+1);
    if(grid.gridType==6){
        //字典类型
        var formItems=[
            {columnWidth:.5,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel:'字典字段',name:'dicText',allowBlank:false}
            ]
            },
            {columnWidth:.5,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel: '字典值',name:'dicValue',allowBlank:false}
            ]
            }

        ];
        formItems.push({xtype:'hiddenfield',name:'entityId',value:grid.parentEntityId});
        formItems.push({xtype:'hiddenfield',name:'name',value:grid.parentEntityName});
        if(action=='update'){
            formItems.push({xtype:'hiddenfield',name:'id',value:null});
        }
        return formItems;
    }else if(grid.gridType==5 && grid.gridHierarchy==2){
        var formItems=[
            {columnWidth:.5,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel: grid.entityCodeText,name:'entityCode',allowBlank:false},
                {
                    xtype:'combo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['value','text'],
                        data : [
                            {text: '是', value: '6'},
                            {text: '否', value: '7'}
                        ]

                    }),
                    fieldLabel: '是否应对字典值',
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
                {xtype:'textfield',fieldLabel: grid.entityNameText,name:'entityName',allowBlank:false}
                ,{
                	xtype:'checkboxfield',
                	boxLabel:'是否ID标识',
                	name:'isID'
                }
            ]
            }

        ];
        formItems.push({xtype:'hiddenfield',name:'parentId',value:grid.parentEntityId});
        formItems.push({xtype:'hiddenfield',name:'hierarchy',value:grid.gridHierarchy+1});
        if(action=='update'){
            formItems.push({xtype:'hiddenfield',name:'entityId',value:null});
        }
        return formItems;
    }else{
        var formItems=[
            {columnWidth:.5,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel: grid.entityCodeText,name:'entityCode',allowBlank:false}
            ]
            },
            {columnWidth:.5,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel: grid.entityNameText,name:'entityName',allowBlank:false}
            ]
            }

        ];
        formItems.push({xtype:'hiddenfield',name:'parentId',value:grid.parentEntityId});
        formItems.push({xtype:'hiddenfield',name:'type',value:grid.gridType
        });
        formItems.push({xtype:'hiddenfield',name:'hierarchy',value:grid.gridHierarchy+1});
        if(action=='update'){
            formItems.push({xtype:'hiddenfield',name:'entityId',value:null});
        }
        return formItems;
    }


}


function gridFromButton(grid){
    console.log(grid);
    var url='./../entityConf/saveOrupdate';
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
                console.log(data)
                if(data.isID || data.isID=='on'){
                	data.type=8;
                }
                data.objType=grid.gridType;
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
                            grid.getStore().reload();
                            treeStore.reload();
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