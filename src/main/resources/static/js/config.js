var HEADERS={
		Authorization:''
}

Ext.apply(Ext,{
	isIE11:(function(){
	var uaAutoScroll = navigator.userAgent.toLowerCase();
	var isAutoScroll = false;
	if ( uaAutoScroll.indexOf("rv:11") > -1) {
	    isAutoScroll = true;
	}
	return isAutoScroll;
	})()
});
Ext.tree.Panel.addMembers({
    selectPathById: function(id){
        var me = this,
            node = me.getStore().getNodeById(id);
        if(node){
            me.selectPath(node.getPath());
        }
    }
});

Ext.form.Basic.override({
	getValues:function(){
		var values=this.callParent(arguments);
		if(values.id && values.id instanceof Array){
			values.id=values.id[0];
		}
		return values;
	}
});


Ext.define('Syu.form',{
	extend:'Ext.form.Panel',
	autoScroll: true,
	config:{
		option:'create',
		entity:{}
	},
	spilt:true,
	minHeight :75,
	layout:'column',
    defaults: {
        border: 0
    },
    buttons: [
    {
        text: '重置',
        handler: function() {
            this.up('form').getForm().reset();
        }
    },{
        text: '提交',
        handler: function() {
        	var form = this.up('form');
        	var baseForm=form.getForm();
        	var data = baseForm.getValues();
        	var url='./../conf/create'
        	var entity=form.getEntity();
        	
        	if(form.getOption()=='update'){
        		url='./../conf/update';
        	}
        	console.log(data);
        	if(!form.isValid()){
        		Ext.Msg.alert('!','填入合法内容');
        		return;
        	}

        	data.entityName=entity.entityName;
        	
        	Ext.Ajax.request({
        		method:'post',        		
        		url:url,
        		headers:HEADERS,
        		params:{jsonStr:JSON.stringify(data)},
        		failure:function(r,data){

        		},
        		success:function(r,data){
        			var result = Ext.JSON.decode(r.responseText);
        			if(result.success==true){
        				Ext.Msg.alert('!','添加成功');
        				this.up('window').close();
        				Ext.getCmp('grid_'+entity.entityName).getStore().reload();
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


Ext.define('EntityGrid',{
	extend:'Ext.grid.Panel'
	,viewConfig:{
		enableTextSelection :true
	},
	config:{
		entity:{},
	    records:{}
	}
	
	,forceFit:true
	,autohight:true
	,autoScroll:true
	,constructor:function(){
		var args=Array.prototype.slice.call(arguments);
		Ext.apply(args[0],{
			selModel: Ext.create("Ext.selection.CheckboxModel", {
			    injectCheckbox: 0,//checkbox位于哪一列，默认值为0
			    mode: "single",//multi,simple,single；默认为多选multi
			    checkOnly: false,//如果值为true，则只用点击checkbox列才能选中此条记录
			    allowDeselect: true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
			    enableKeyNav: true
			})
			,dockedItems:[{
		        xtype: 'pagingtoolbar',
		        store: args[0].store,
		        dock: 'bottom',
		        displayInfo: true
		    	}
			    ,{
				    xtype: 'toolbar',
				    dock: 'top',
				    items: [
				        {
				        	xtype: 'button',
				        	text: '增加',
				        	handler:function(){
				        		    var grid= this.up('grid');
				        		    var entity=Ext.apply({},grid.getEntity());

					        		var curForm=Ext.create('Syu.form',{
					        			items:createFormItems(entity)
					        		});
					        		
					        		curForm.setEntity(entity);
					        		var win=Ext.create('Ext.window.Window', {
				        				//id:'win_'+entity.entityName,
				        			    title: '增加'+entity.title,
				        			    height: 200,
				        			    width: 800,
				        			    layout: 'fit',
				        			    items: [curForm] 
				        			});
					        		win.show();	
					        		
				        	}

				        }
				        ,'-'
				        ,{
				        	xtype: 'button',
				        	text: '修改',
				        	handler:function(){
				        		var grid=this.up('grid');
			        		    var entity=grid.getEntity();
				        		var curForm=Ext.create('Syu.form',{
				        			items:createFormItems(entity)
				        		});
				     
				        		curForm.setOption('update');
				        		curForm.setEntity(entity);
				        		if(grid.getSelectionModel().getSelection().length>0){
				        			var data=grid.getSelectionModel().getSelection()[0].getData();
				        			console.log(data);
				        			curForm.getForm().setValues(data);
				        		}else{
				        			Ext.Msg.alert('!','先选中一条');
				        			return;
				        		}
				        	    //var input=curForm.down('[name="dataType"]');
				        	    //console.log(input);
				        	    //input.setRawValue('ioioioio');
				        		
				        		
			        			Ext.create('Ext.window.Window', {
			        				//id:'win_'+entity.entityName,
			        			    title: '修改'+entity.title,
			        			    height: 200,
			        			    width: 800,
			        			    layout: 'fit',
			        			    items: [curForm] 
			        			}).show()

				        	}
				        }
				        ,'-'
				        ,{
				        	xtype: 'button',
				        	text: '删除',
				        	handler:function(){		        		
				        		var grid=this.up('grid');
			        		    var entity=grid.getEntity();
				        		if(!grid.getSelectionModel().getSelection().length>0){		        		
				        			Ext.Msg.alert('!','先选中一条');
				        			return;
				        		}
				        		Ext.Msg.confirm('!','确定删除选中记录？',function(btn){
				        	        if(btn=='no'){
				        	            return;							
				        	          }else{
				        	       		var data=grid.getSelectionModel().getSelection()[0].getData();
						        		data.entityName=entity.entityName;
						            	Ext.Ajax.request({
						            		method:'post',        		
						            		url:'./../conf/delete',
						            		params:{jsonStr:JSON.stringify(data)},
						            		failure:function(r,data){

						            		},
						            		success:function(r,data){
						            			var result = Ext.JSON.decode(r.responseText);
						            			if(result.success=='true'){
						            				Ext.Msg.alert('!','成功删除');
						        
						            				Ext.getCmp('grid_'+entity.entityName).getStore().reload();
						            			}

						            		}
						            	});
				        	          }
				        		})		        		
				        	}
				        }
				    ]
			    }
			]			
		});
		
		this.callParent(args);
	}
	//,dockedItems: 
		
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

function createInput(field){
	var allowBlank=field.allowBlank==1?false:true,
		hidden=field.hidden==1?true:false;
	var blur=(function(){
		if(!field.checkSql){
			return function(){};
		}
		return function(){
		    var params=this.up('form').getForm().getValues();
		    params.checkSql=field.checkSql;
        	Ext.Ajax.request({
        		method:'post',
        		url:'./../conf/checkUnique',
        		params:params,
        		failure:function(r,data){

        		},
        		success:function(r,data){
        			var result = Ext.JSON.decode(r.responseText);
        			console.log(result);
        			if(!result.success){
        				this.setValue('');
        				Ext.Msg.alert(result.msg);
        			}

        		},
        		scope:this
        	});
		}
	})();
	var select=function(){};
	if(field.renderer){
		eval('var tempFun='+field.renderer);
		//blur=Ext.Function.createSequence(blur,tempFun);
		select=tempFun;
	}
	if(field.dataSql){
		return Ext.create('Syu.Combox',{
			store:Ext.create('Ext.data.Store',{
				fields:['value','text'],
				proxy: {
			        type: 'ajax',
			        url:'./../conf/dataSql',
			        extraParams:{sql:field.dataSql},
			        reader: {
			            type: 'json',
			            root: 'result'
			        },
			        scope:this
			    },
			    autoLoad:true,
			    listeners:{
			    	beforeLoad:function(){
			    		
			    	}
			    }

			}),
			listeners:{
				blur:blur,
				select:select
			    
			},
		    fieldLabel: field.fieldText,
		    //id:field.entityName+'_'+field.fieldName,
		    name:field.fieldName,
		    displayField: 'text',
		    valueField: 'value',
		    allowBlank:allowBlank,
		    hidden:hidden,
		    //disabled:!!inputInfo.disablemsg,
		    //emptyText:inputInfo.multiselect?null:inputInfo.disablemsg,
		    triggerAction:'all'
		    //editable:false,
		    //multiSelect :inputInfo.multiselect
		});
	}
	if(field.fieldType=='string'){
		//console.log(field.fieldTxt);
		return Ext.create('Ext.form.TextField',{
			fieldLabel: field.fieldText,
		    name:field.fieldName,
		    allowBlank:allowBlank,
		    hidden:hidden,
		    //allowBlank:inputInfo.allowblank,
		    //regex:inputInfo.validateexp?new RegExp(inputInfo.validateexp):'',
		    //regexText:inputInfo.validatemsg
		    listeners:{
		    	blur:blur
		    }

		});
	}
	if(field.fieldType=='int'){
		return Ext.create('Ext.form.Number',{
			fieldLabel: field.fieldText,
			//id:field.entityName+'_'+field.fieldName,
		    name:field.fieldName,
		    hidden:hidden,
		    allowBlank:allowBlank,
		    //allowBlank:inputInfo.allowblank,
		    //regex:inputInfo.validateexp?new RegExp(inputInfo.validateexp):'',
		    //regexText:inputInfo.validatemsg
		});
	}
	if(field.fieldType=='date'){
		return Ext.create('WdatePickerTime', {
		    name: field.fieldName,
		   // id:field.entityName+'_'+field.fieldName,
	        fieldLabel: field.fieldText,
	        anchor: '100%',
	        hidden:hidden,
	        allowBlank:allowBlank,
	        //allowBlank:inputInfo.allowblank
		});
	}
	if(field.fieldType=='time'){
		return Ext.create('Ext.form.Time', {
		    name: field.fieldName,
		    //id:field.entityName+'_'+field.fieldName,
	        fieldLabel: field.fieldText,
	        increment: 30,
	        anchor: '100%',
	        hidden:hidden,
	        allowBlank:allowBlank
	        //allowBlank:inputInfo.allowblank
		});
	}
}

function createFormItems(entity){
	var items=[{columnWidth:.33,type:'form',padding:'10 0 0 5',items:[]},
			   {columnWidth:.33,type:'form',padding:'10 0 0 5',items:[]},
			   {columnWidth:.33,type:'form',padding:'10 0 0 5',items:[]}];
	var array=[];
	for(var i=0;i<entity.fields.length;i++){
		array.push(entity.fields[i]);
	}
	for(var i=0;i<array.length;i++){
		if(array[i].hidden=='1'){
			var f=array.splice(i,1);
			items.push(createInput(f[0]));
		}
	}
	for(var i=0;i<array.length;i++){
		var field=array[i];
		
		switch(i%3){
	   		case 0:
	   			items[0].items.push(createInput(field));
	   			break;
	   		case 1:
	   			items[1].items.push(createInput(field));
	   			break;
	   		case 2:
	   			items[2].items.push(createInput(field));
	   			break;
		}		
	}
	//items.push({xtype:'hiddenfield',name:'id',value:null});
	console.log(items);
	return items;
}

function createGrid(entity){

	var fields=[],
		columns=[],
		queryUrl=entity.queryUrl,
		createUrl=entity.createUrl,
		updateUrl=entity.queryUrl,
		deleteUrl=entity.updateUrl;
	for(var i=0;i<entity.fields.length;i++){

		var field=entity.fields[i];
		fields.push(field.fieldName);
		var column={
				dataIndex:field.fieldName,
				text:field.fieldText,
				hidden:field.hidden==1?true:false,
				width:field.width
				//,dataType:field.fieldType,xtype: 'datecolumn',   format:'Y-m-d'
				// xtype:'templatecolumn'
				
		};
		if(field.dataSql){
			(function(i,columns){
	        	Ext.Ajax.request({
	        		method:'post',
	        		url:'./../conf/dataSql',
	        		params:{sql:field.dataSql},
	        		failure:function(r,data){

	        		},
	        		success:function(r,data){
	        			var result = Ext.JSON.decode(r.responseText);
	        			var grid=Ext.getCmp('grid_'+entity.entityName);	        			
	        			this[i].renderer=function(v){
	        			    for(var j=0;j<result.length;j++){
	        			    	if(result[j].value==v){
	        			    		return result[j].text;
	        			    	}
	        			    }
	        				return v;
	        			}	  
	        			grid.reconfigure(grid.getStore(),this);
	        			
	        		},
	        		scope:columns
	        	});					
			})(i,columns);
		
		}
		if(field.fieldType=='date'){
			column.renderer=function(v){
				if(/\d{13}/.test(v)){
					return Ext.Date.format(new Date(v), 'Y-m-d H:i:s');
				}
				return v;
			}
		}
		columns.push(column);
	}

	     
	var store=Ext.create('Ext.data.Store',{
		fields:fields,
		proxy : {
            type : 'ajax',
            url : './../conf/data',
            extraParams:{entityName:entity.entityName},
            reader: {
	            type: 'json',
	            root: 'data',
	            totalProperty:'total'
	            }
            }
		}
	);
	store.load();
	var grid=Ext.create('EntityGrid',{
		id:'grid_'+entity.entityName,
		store:store
		,columns:columns
		});
	grid.setEntity(entity);
	return grid;

}


var treeStore = Ext.create('Ext.data.TreeStore', {
    // 根节点的参数是parentId
    nodeParam : 'parentId',
    // 根节点的参数值是0
    defaultRootId : 0,
    // 属性域
    fields : ['id','text',{name:'inputInfo',type:'object'}],
    // 数据代理
    proxy : {
        // 请求方式
        type : 'ajax',
        // 请求网址
        url : './../conf/tree'
    },
    root:{
    	text:'认证授权'
    }
});
treeStore.load();

var tree=Ext.create('Ext.tree.Panel', {
	title: '系统管理',
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
		'select': function(node, record,item) {	
	    	if(record.isLeaf()){	 
	    		if(Ext.getCmp('tab_'+record.raw.entityName)){
	    			var tab=Ext.getCmp('tab_'+record.raw.entityName);
	    			mainPanle.setActiveTab(tab);
	    			return;
	    		}
	    		Ext.Ajax.request({
	    			method:'post',
	    			url:'./../conf/entity',
	    			params:{entityName:record.raw.entityName},
	    			failure:function(r,data){

	    			},
	    			success:function(r,data){
	    				var result = Ext.JSON.decode(r.responseText);
	    				//console.log(result);
	    				var grid=createGrid(result);
	    				var panel=Ext.create('Ext.Panel',{
	    					id:'tab_'+record.raw.entityName,
	    					title:result.title,
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
	}

});
var mainPanle=Ext.create('Ext.tab.Panel',{
	region:'center',
	layout: 'fit'
});

Ext.onReady(function(){
//	Ext.getDoc().on("contextmenu", function(e){
//	    e.stopEvent();
//	});
	
	if(!HEADERS.Authorization){
		var win=Ext.create('Ext.window.Window', {
		    title: '登陆',
		    closable:false,
		    height: 200,
		    width: 300,
		    layout: 'fit'
		    ,items:[{
		    	xtype:'form',
		    	bodyPadding:5,
		    	defaultType:'textfield',
		    	items:[
		    	       {
		    	    	   fieldLabel:'用户名(手机号码或EMAIL)',
		    	    	   name:'id',
		    	    	   allowBlank:false
		    	       },{
		    	    	   fieldLabel:'密码',
		    	    	   inputType:'password',
		    	    	   name:'passwd',
		    	    	   allowBlank:false
		    	    	   
		    	       }
		    	]
			 	,buttons: [
				           {
				               text: '重置',
				               handler: function() {
				                   this.up('form').getForm().reset();
				               }
				           },{
				        	   text:'提交',
				        	   handler:function(){
				        		   var values=this.up('form').getForm().getValues();
				        		   console.log(values);
				        	        Ext.Ajax.request({
				        	        	url:'./../login',
				        	        	method:'post',
				        	        	params:values,
				        	        	success:function(r,data){
				    	    				var result = Ext.JSON.decode(r.responseText);
				    	    				console.log(result);
				    	    				HEADERS.Authorization=result.ticket
				    	    				var view=new Ext.Viewport({
				    	    				   	layout:'border',
				    	    				    items: [tree,mainPanle]
				    	    				});
				    	    				win.close();
				    	    			},
				    	    			scope:this
				        	        });	        		   
				        	   }
				           }
					 ]	    	
		    }]

		});
		win.show();
	}


});
