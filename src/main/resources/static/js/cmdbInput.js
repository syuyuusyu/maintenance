var mainPanle=Ext.create('Ext.tab.Panel',{
    region:'center',
    layout: 'fit'
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
    title: 'CMDB资源录入',
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

	    },
	    beforeitemcollapse: function (node, opt){
	    	
	    },
        'select': function(node, record,item) {
        	console.log(record.raw);
	        if(record.raw.type=='7' ||record.raw.type=='8'  ){
	            return;
            }
	        if(record.raw.hierarchy==2){
	        	Ext.Ajax.request({
            		method:'get',        		
            		url:'./../cmdbConf/tree',
            		params:{parentId:record.raw.id},
            		failure:function(r,data){

            		},
            		success:function(r,data){
            			var result = Ext.JSON.decode(r.responseText);
            			console.log(result.children);
            			var grid=createGrid(result.children);
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
            	});
	        	
	        }

        }		
	    ,'itemcontextmenu': function(_this, record, item, index, e, eOpts) {

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

function createGrid(records){
	console.log(records);
	var columns=[],
		fields=[],
		parentId=records[0].parentId;
	for(var i=0;i<records.length;i++){
		var record=records[i];
		var cloum={dataIndex:record.entityCode,text:record.entityName,width:100};
		columns.push(cloum);
		fields.push(record.entityCode);
	}
	fields.push('groupId');
	columns.push({dataIndex:'groupId',text:'groupId',width:100,hidden:true});
	var store=Ext.create('Ext.data.Store',{
	      fields:fields,
	      proxy : {
	          type : 'ajax',
	          url : './../cmdbConf/records',
	          extraParams:{parentId:parentId},
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
      ,entityId:parentId
      ,records:records
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
                          console.log(items);
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
                                      url:'./../cmdbConf/deleteRecord',
                                      params:{
                                          id:data.groupId
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
	var records=grid.records;
    var items=[{columnWidth:.5,type:'form',padding:'10 0 0 5',id:'column1',items:[]},
                   {columnWidth:.5,type:'form',padding:'10 0 0 5',id:'column2',items:[]},
    			   {columnWidth:.5,type:'form',padding:'10 0 0 5',id:'column3',items:[]}];
    for(var i=0;i<grid.records.length;i++){
    	switch(i%3){
   		case 0:
   			items[0].items.push(createInput(records[i]));
   			break;
   		case 1:
   			items[1].items.push(createInput(records[i]));
   			break;
   		case 2:
   			items[2].items.push(createInput(records[i]));
   			break;
    	}
    }
    items.push({xtype:'hiddenfield',name:'groupId',value:null});
    return items;
}

function createInput(record){
	return {xtype:'textfield',fieldLabel: record.entityName,name:record.entityCode,allowBlank:false}
}

function gridFromButton(grid){
    
    var url='./../cmdbConf/saveOrupdateGroup';
    var buttons=[
        {
            text: '重置',
            handler: function() {
                this.up('form').getForm().reset();
            }
        },{
            text:'提交',
            handler:function(){
            	console.log(grid.entityId);
                var form = this.up('form');
                var baseForm=form.getForm();
                var data = baseForm.getValues();
                if(!form.isValid()){
                    Ext.Msg.alert('!','填入合法内容');
                    return;
                }
                data.entityId=grid.entityId;
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


Ext.onReady(function(){
    Ext.getDoc().on("contextmenu", function(e){
	    e.stopEvent();
	});
    var view=new Ext.Viewport({
        layout:'border',
        items: [tree,mainPanle]
    });
});