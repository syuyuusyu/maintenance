var store=Ext.create('Ext.data.Store',{
    fields:['id','roleId','relevantGroup','relevantRecord','groupName','recordName','type','roleName','equalType','valveValue','alarmLevel','action'],
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
        {dataIndex:'relevantGroup',text:'relevantGroup',hidden:true},
        {dataIndex:'relevantRecord',text:'relevantRecord',hidden:true},
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
            console.log(1111);
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
                                    url:'./../recordConf/delete',
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
            {columnWidth:.33,type:'form',padding:'10 0 0 5'
                ,items:[
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
                    name:'type',
                    displayField: 'entityName',
                    valueField: 'id',
                    allowBlank:false,
                    triggerAction:'all',
                    listeners:{
                        selected:function(a,b){
                            console.log(a);
                            console.log(b);
                        }
                    }
                },
                {
                    xtype:'combo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['value','text'],
                        proxy: {
                            type: 'ajax',
                            url:'./../entityConf/grid',
                            extraParams:{type:'',entityId:''},
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


                    })
                },
                {
                    xtype:'combo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['value','text'],
                        proxy: {
                            type: 'ajax',
                            url:'./../entityConf/grid',
                            extraParams:{type:'',entityId:''},
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


                    })
                }
            ]
            },
            {columnWidth:.33,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel: '字典值',name:'dicValue',allowBlank:false}
            ]
            },
            {columnWidth:.33,type:'form',padding:'10 0 0 5'
                ,items:[
                {xtype:'textfield',fieldLabel: '字典值',name:'dicValue',allowBlank:false}
            ]
            }

        ],
        buttons:[]
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