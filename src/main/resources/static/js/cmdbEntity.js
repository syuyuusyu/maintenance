var mainPanle=Ext.create('Ext.tab.Panel',{
    region:'center',
    layout: 'fit'
});

Ext.define('entityCombo',{
		extend:'Ext.form.ComboBox',
        xtype:'entityCombo',
        
        displayField: 'entityName',
        valueField: 'id',
        allowBlank:false,
        triggerAction:'all'   
});

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
        url : './../cmdbConf/tree'
    },
    root:{
    	text:'CMDB资源'
    	,iconCls:'icon-home'
    }
});
treeStore.load();

var tree=Ext.create('Ext.tree.Panel', {
    title: 'CMDB资源关系配置',
    region:'west',
    store: treeStore,
    width:200,
    useArrows: false,
    frame: true,
    autoScroll: true,
    collapsible: true,
    containerScroll: true,
    //rootVisible: false,//隐藏根节点
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
        	console.log(record.raw);
	        if(record.raw.type=='7' ||record.raw.type=='8'  ){
	            return;
            }
	        if(record.raw.hierarchy>=1){
	        	//各平台告警类型组
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


function createGrid(entity,grid){
	console.log(entity);
  var entityCode,entityName;
  if(entity.hierarchy==1){
	  entityCode='资源编码';
	  entityName='资源名称';
  }
  if(entity.hierarchy==2){
	  entityCode='资源属性编码';
      entityName='资源属性名称';
  }
  var columns=[
          {dataIndex:'id',text:'ID',width:100},
          {dataIndex:'parentId',text:'父节点ID',width:100},
          {dataIndex:'entityCode',text:entityCode,width:100},
          {dataIndex:'entityName',text:entityName,width:200},
          {dataIndex:'hierarchy',text:'层级',hidden:true,width:200},
          {dataIndex:'type',text:'类型',renderer:typeRender,width:100},
          {dataIndex:'relevantId',text:'关联实体ID',renderer:typeRender,width:100}

          ];
  var fields=['entityId','parentId','entityCode','entityName','hierarchy','type','relevantId'];
  var store=Ext.create('Ext.data.Store',{
      fields:fields,
      proxy : {
          type : 'ajax',
          url : './../cmdbConf/grid',
          extraParams:entity,
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
      ,gridType:entity.type
      ,gridHierarchy:entity.hierarchy
      ,entityId:entity.id
      ,parentEntityId:entity.parentId
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
                          console.log(data);
                          Ext.Msg.confirm('!','确定删除选中记录？',function(btn){
                              if(btn=='no'){
                                  return;
                              }else{
                                  Ext.Ajax.request({
                                      method:'post',
                                      url:'./../cmdbConf/delete',
                                      params:{
                                          id:data.id,
                                          type:grid.gridType,
                                          hierarchy:grid.gridHierarchy
                                      },
                                      failure:function(r,data){

                                      },
                                      success:function(r,data){
                                          var result = Ext.JSON.decode(r.responseText);
                                          if(result.success=='true'){
                                              Ext.Msg.alert('!','成功');
                                              grid.getStore().reload();
                                              treeStore.reload();
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
	console.log(grid.entityId+" "+grid.parentEntityId);
    if(grid.gridType==5 && grid.gridHierarchy==2){
        var formItems=[
            {columnWidth:.5,type:'form',padding:'10 0 0 5',id:'column1'
                ,items:[
                {xtype:'textfield',fieldLabel: grid.entityCodeText,name:'entityCode',allowBlank:false},
                {
                    xtype:'combo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['value','text'],
                        data : [
                            {text: 'ID标识', value: '8'},
                            {text: '普通字段', value: '7'},
                            {text: '关联字段', value: '6'}
                        ]
                    }),
                    fieldLabel: '类型',
                    name:'type',
                    displayField: 'text',
                    valueField: 'value',
                    allowBlank:false,
                    triggerAction:'all',
                    listeners:{
                    	'select':function(combo,v){
                    		var form=this.up('form'),
                    			gCombo=this.up('form').down('combo[name="relevantGroup"]'),
                    			rCombo=this.up('form').down('combo[name="relevantId"]');
                    		if(combo.getValue()==6){
                    			gCombo.setDisabled(false);
                    			rCombo.setDisabled(false);
                    			gCombo.store.getProxy().setExtraParam('parentId',grid.parentEntityId);
                    			gCombo.store.load();
                    			
                    		}else{
                    			gCombo.clearValue();
                    			rCombo.clearValue();
                    			gCombo.setDisabled(true);
                    			rCombo.setDisabled(true);
                    		}
                    	}
                    }
                },
                {
                	xtype:'entityCombo',
                	store:Ext.create('Ext.data.Store',{
                        fields:['id','entityName'],
                        proxy: {
                            type: 'ajax',
                            url:'./../cmdbConf/entity',
                            extraParams:{parentId:-1},
                            reader: {
                                type: 'json',
                                root: 'result'
                            }
                            ,scope:this
                        },
                        autoLoad:false
                    }),
                	fieldLabel: '关联资源属性',
                	name:'relevantId',
                	disabled:true
                }
            ]
            },
            {columnWidth:.5,type:'form',padding:'10 0 0 5',id:'column2'
                ,items:[
                {xtype:'textfield',fieldLabel: grid.entityNameText,name:'entityName',allowBlank:false}
                ,{
                	xtype:'entityCombo',
                	store:Ext.create('Ext.data.Store',{
                        fields:['id','entityName'],
                        proxy: {
                            type: 'ajax',
                            url:'./../cmdbConf/entity',
                            extraParams:{parentId:-1},
                            reader: {
                                type: 'json',
                                root: 'result'
                            }
                            ,scope:this
                        },
                        autoLoad:false
                    }),
                	fieldLabel: '关联资源',
                	name:'relevantGroup',
                	disabled:true,
                	listeners:{
    		        	change:function(_this,value){
    		        		console.log('关联资源'+value);
    		        		var rCombo=this.up('form').down('combo[name="relevantId"]');
    		        		rCombo.clearValue();
    		        		rCombo.store.getProxy().setExtraParam('parentId',value);
    		        		rCombo.store.load();
    		        	}
    		        }
                }
                
            ]
            }

        ];
        formItems.push({xtype:'hiddenfield',name:'parentId',value:grid.entityId});
        formItems.push({xtype:'hiddenfield',name:'hierarchy',value:grid.gridHierarchy+1});
        if(action=='update'){
            formItems.push({xtype:'hiddenfield',name:'id',value:null});
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
        formItems.push({xtype:'hiddenfield',name:'parentId',value:grid.entityId});
        formItems.push({xtype:'hiddenfield',name:'type',value:5
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
    var url='./../cmdbConf/saveOrupdate';
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

Ext.onReady(function(){
    Ext.getDoc().on("contextmenu", function(e){
	    e.stopEvent();
	});
    var view=new Ext.Viewport({
        layout:'border',
        items: [tree,mainPanle]
    });
});