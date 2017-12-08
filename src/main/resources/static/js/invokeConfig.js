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

function changeRenderer(value, meta, record) {
    meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';
    return value.replace(/\n/g,'</br>');
}

Ext.define('methodCombo',{
    extend:'Ext.form.ComboBox',
    xtype:'statueCombo',
    store:Ext.create('Ext.data.Store',{
        fields:['value','text'],
        data : [
            {text: 'POST', value: 'post'},
            {text: 'GET', value: 'get'}
        ]
    }),
    fieldLabel: '请求方法',
    name:'method',
    displayField: 'text',
    valueField: 'value',
    allowBlank:false,
    triggerAction:'all',
    listeners:{
        change:function(_this,record){

        }
    }

});



var store=Ext.create('Ext.data.Store',{
    fields:['id','name','descrption','method','url','head','body','parseFun','next','isSave','saveEntityId'],
    proxy : {
        type : 'ajax',
        url : './../invoke/invokes',
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
        {dataIndex:'id',text:'ID',hidden:true},
        {dataIndex:'name',text:'名称',width:150},
        {dataIndex:'descrption',text:'描述',width:200},
        {dataIndex:'method',text:'请求方法',width:70},
        {dataIndex:'url',text:'url',width:150,renderer:changeRenderer},
        {dataIndex:'head',text:'请求头',width:200,renderer:changeRenderer},
        {dataIndex:'body',text:'请求体',width:200,renderer:changeRenderer},
        {dataIndex:'parseFun',text:'解析函数',width:300,renderer:changeRenderer},
        {dataIndex:'next',text:'下次关联调用',width:200},
        {dataIndex:'isSave',text:'是否保存',width:100 },
        {dataIndex:'saveEntityId',text:'保存实体ID',width:100,
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
        }

    ]
    ,viewConfig:{
        enableTextSelection :true
    }
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
                            if(btn=='yes'){
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

                            }else{
                                return;
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
                {xtype:'textfield',fieldLabel:'调用名称',name:'name',allowBlank:false}


            ]
            },
            {columnWidth:.33,type:'form',padding:'10 0 0 5',id:'colum2'
                ,items:[
                {
                    xtype:'methodCombo'
                }
            ]
            },
            {columnWidth:.33,type:'form',padding:'10 0 0 5',id:'colum3'
                ,items:[
                {
                    xtype:'syuCombo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['id','name'],
                        proxy: {
                            type: 'ajax',
                            url:'./../invoke/allInvoke',
                            reader: {
                                type: 'json',
                                root: 'result'
                            },
                            scope:this
                        },
                        autoLoad:true
                    }),
                    fieldLabel: '下次关联调用',
                    name:'roleId',
                    displayField: 'name',
                    valueField: 'id',
                    allowBlank:false,
                    triggerAction:'all',
                    multiSelect:true,
                    listeners:{
                        select:function(_this,record){

                        }
                    }
                }

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