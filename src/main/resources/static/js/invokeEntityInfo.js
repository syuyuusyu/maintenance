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
        if(this.name=='next'){
            var s="";
            for(var i=0;i<v.length;i++){
                if(i==v.length-1){
                    s+=v[i];
                }else{
                    s+=v[i]+",";
                }

            }
            return s;
        }
        return v;
    }
    , getRawValue: function() {
        var v = this.callParent();
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

function format(txt,compress/*是否为压缩模式*/){/* 格式化JSON源码(对象转换为JSON文本) */
    var indentChar = '    ';
    if(/^\s*$/.test(txt)){
        alert('数据为空,无法格式化! ');
        return;
    }
    try{var data=eval('('+txt+')');}
    catch(e){
        alert('数据源语法错误,格式化失败! 错误信息: '+e.description,'err');
        return;
    };
    var draw=[],last=false,This=this,line=compress?'':'\n',nodeCount=0,maxDepth=0;

    var notify=function(name,value,isLast,indent/*缩进*/,formObj){
        nodeCount++;/*节点计数*/
        for (var i=0,tab='';i<indent;i++ )tab+=indentChar;/* 缩进HTML */
        tab=compress?'':tab;/*压缩模式忽略缩进*/
        maxDepth=++indent;/*缩进递增并记录*/
        if(value&&value.constructor==Array){/*处理数组*/
            draw.push(tab+(formObj?('"'+name+'":'):'')+'['+line);/*缩进'[' 然后换行*/
            for (var i=0;i<value.length;i++)
                notify(i,value[i],i==value.length-1,indent,false);
            draw.push(tab+']'+(isLast?line:(','+line)));/*缩进']'换行,若非尾元素则添加逗号*/
        }else   if(value&&typeof value=='object'){/*处理对象*/
            draw.push(tab+(formObj?('"'+name+'":'):'')+'{'+line);/*缩进'{' 然后换行*/
            var len=0,i=0;
            for(var key in value)len++;
            for(var key in value)notify(key,value[key],++i==len,indent,true);
            draw.push(tab+'}'+(isLast?line:(','+line)));/*缩进'}'换行,若非尾元素则添加逗号*/
        }else{
            if(typeof value=='string')value='"'+value+'"';
            draw.push(tab+(formObj?('"'+name+'":'):'')+value+(isLast?'':',')+line);
        };
    };
    var isLast=true,indent=0;
    notify('',data,isLast,indent,false);
    return draw.join('');
}


var store=Ext.create('Ext.data.Store',{
        fields:['id','name','descrption','method','url','head','body','parseFun','next','isSave','saveEntityId'],
        proxy : {
            type : 'ajax',
            url : './../invokeInfo/infos',
            method:'POST',
            extraParams:{},
            actionMethods : {
                read : 'POST' // Store设置请求的方法，与Ajax请求有区别
            },
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
    ,columns:[
        {dataIndex:'id',text:'ID',width:30},
        {dataIndex:'name',text:'名称',width:100},
        {dataIndex:'descrption',text:'描述',width:200},
        {dataIndex:'method',text:'请求方法',width:100},
        {dataIndex:'url',text:'URL',width:200},
        {dataIndex:'head',text:'请求头',width:200},
        {dataIndex:'body',text:'请求体',width:200},
        {dataIndex:'parseFun',text:'解析函数',width:200},
        {dataIndex:'isSave',text:'是否储存',width:100},
        {dataIndex:'saveEntityId',text:'储存实体',width:100}
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
            items: [{
                    xtype: 'button',
                    text: '增加',
                    iconCls:'icon-add',
                    handler:function(){
                        var grid=this.up('grid');
                        var form=createForm();
                        Ext.create('Ext.window.Window', {
                            //id:'win_'+entity.entityName,
                            title: '增加',
                            height: 780,
                            width: 1200,
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
                    var data=grid.getSelectionModel().getSelection()[0].getData();
                    data.head=format(data.head);
                    data.body=format(data.body);
                    form.getForm().setValues(data);

                    Ext.create('Ext.window.Window', {
                        //id:'win_'+entity.entityName,
                        title: '修改',
                        height: 780,
                        width: 1200,
                        layout: 'fit',
                        modal:true,
                        items: [form]
                    }).show()
                }
            }
               ]
        }
    ]
});

function createForm(){
    var required = '<span style="color:#ff2432;font-weight:bold" data-qtip="Required">*</span>';
    var top = Ext.widget({
        xtype: 'form',
        id: 'multiColumnForm',
        //collapsible: true,
        frame: true,
        bodyPadding: '5 5 0',
        width: 600,
        fieldDefaults: {
            labelAlign: 'top',
            msgTarget: 'side'
        },

        items: [{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '调用名称',
                    allowBlank: false,
                    name: 'name',
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '描述',
                    name: 'descrption',
                    anchor:'95%'
                }]
            }]
        },{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
                xtype: 'container',
                flex: .5,
                layout: 'anchor',
                items: [{
                    xtype:'combo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['value','text'],
                        data : [
                            {text: 'post', value: 'post'},
                            {text: 'get', value: 'get'},
                            {text: 'put', value: 'put'},
                            {text: 'delete', value: 'delete'}
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

                }]
            },{
                xtype: 'container',
                flex: .5,
                layout: 'anchor',
                items: [{
                    xtype:'syuCombo',
                    store:Ext.create('Ext.data.Store',{
                        fields:['id','name'],
                        proxy: {
                            actionMethods : {
                                read : 'POST' // Store设置请求的方法，与Ajax请求有区别
                            },
                            type: 'ajax',
                            url:'./../invokeInfo/invokes',
                            extraParams:{},
                            reader: {
                                type: 'json',
                                root: 'result'
                            },
                            scope:this
                        },
                        autoLoad:true
                    }),
                    fieldLabel: '关联请求',
                    name:'next',
                    displayField: 'name',
                    valueField: 'id',
                    allowBlank:true,
                    triggerAction:'all',
                    multiSelect:true
                    // ,getValue:function () {
                    //     var v = this.callParent();
                    //     console.log('getValue');alert(v);
                    //     console.log(v);
                    //     return v;
                    // }
                    ,listeners:{
                        change:function(_this,record){

                        }
                    }

                }]
            },{
                xtype: 'container',
                flex: 1.5,
                layout: 'anchor',
                items: [{
                    xtype:'textfield',
                    fieldLabel: 'URL',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'url',
                    anchor:'97%'
                }]
            }]
        },{
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    fieldLabel: '请求头',
                    allowBlank: false,
                    afterLabelTextTpl: required,
                    name: 'head',
                    minHeight:300,
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    fieldLabel: '请求体',
                    afterLabelTextTpl: required,
                    allowBlank: false,
                    name: 'body',
                    minHeight:300,
                    anchor:'95%'
                }]
            }]
        },{
            xtype:'textareafield',
            fieldLabel: '解析函数',
            name: 'parseFun',
            minHeight:300,
            anchor:'98%'
        },{
            xtype:'hiddenfield',
            name:'id'
        }

        ],

        buttons: [{
            text: '测试',
            handler: function() {
                if(this.up('form').getForm().isValid()) {
                    var data = this.up('form').getForm().getValues();
                    var queryStr = [];
                    (data.url + data.head + data.body).replace(/@(\w+)/g, function (w, p1) {
                        queryStr.push(p1);
                    });
                    var fromId=this.up('form').getId();
                    var from = createSubForm(queryStr, fromId);
                    Ext.create('Ext.window.Window', {
                        //id:'win_'+entity.entityName,
                        title: '请填写调用参数',
                        //height: 600,
                        width: 400,
                        layout: 'fit',
                        modal: true,
                        items: [from]
                    }).show()
                }
            }
        },{
            text: 'Save',
            handler: function() {
                if(this.up('form').getForm().isValid()) {
                    var queryData = this.up('form').getForm().getValues();
                    var m = Ext.MessageBox.wait("查询正在进行中...", "查询");
                    Ext.Ajax.request({
                        method:'post',
                        url:'./../invokeInfo/save',
                        jsonData:queryData,
                        failure:function(r,data){

                        },
                        success:function(r,data){
                            var result = Ext.JSON.decode(r.responseText);
                            m.hide();
                            if(result.success){
                                alert("success");
                            }

                        }
                    });
                }
            }
        },{
            text: 'Cancel',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }]
    });
    return top;
}

function createSubForm(queryStr,fromId){
    var items=[];
    for(var i=0;i<queryStr.length;i++){
        items.push({
            xtype:'textfield',
            fieldLabel: queryStr[i],
            allowBlank: false,
            name: queryStr[i],
            anchor:'97%'
        });
    }
    items.push(
    {
        xtype:'checkbox',
        boxLabel: '是否进行关联调用',
        name: 'isNext',
        inputValue: true,
        listeners:{
            change:function(_this,newValue, oldValue, eOpts){
                if(newValue){
                    this.up('form').down('checkbox[name="isFun"]').setValue(true);
                    //this.up('form').down('checkbox[name="isFun"]').hide();
                }else{
                    //this.up('form').down('checkbox[name="isFun"]').show();
                }
            }
        }
    }
    );
    items.push(
        {
            xtype:'checkbox',
            boxLabel: '是否运行解析函数',
            name: 'isFun',
            inputValue: true,
            hidden:true
        }
    );
    var from = Ext.widget({
        xtype: 'form',
        //collapsible: true,
        frame: true,
        bodyPadding: '5 5 0',
        height:'auto',
        items:items,
        buttons: [{
            text: '提交',
            handler: function() {
                var from =this.up('form').getForm();
                var upFrom=Ext.getCmp(fromId);
                if(from.isValid()){
                    var queryData=upFrom.getForm().getValues();
                    //Ext.apply(queryData,oqueryData);
                    var queryMap=from.getValues(),
                        isNext=queryMap.isNext,
                        isFun=queryMap.isFun,
                        parseFun=queryData.parseFun;
                    delete queryMap.isNext;
                    delete queryMap.isFun;
                    if(!isNext){
                        delete queryData.next;
                    }
                    if(!isFun){
                        delete queryData.parseFun;
                    }
                    queryData.queryMap=queryMap;
                    var m = Ext.MessageBox.wait("查询正在进行中...", "查询");
                    Ext.Ajax.request({
                        method:'post',
                        url:'./../invokeInfo/test',
                        jsonData:queryData,
                        timeout: 100000000,
                        failure:function(r,data){

                        },
                        success:Ext.Function.bind(function(r,data,parseFun,isNext){
                            var result = Ext.JSON.decode(r.responseText);
                            m.hide();
                            if(result.success){
                                var form=resultForm(result,isNext,parseFun);

                                Ext.create('Ext.window.Window', {
                                    //id:'win_'+entity.entityName,
                                    title: '调用结果',
                                    height: 780,
                                    width: 1200,
                                    layout: 'fit',
                                    modal:true,

                                    items: [form]
                                }).show()
                            }

                        },this,[parseFun,isFun],true)
                    });
                }
            }
        },{
            text: '清空',
            handler: function() {
                this.up('form').getForm().reset();
            }
        }]
    });
    return from;

}

function resultForm(result,isNext,parseFun){
    var items=[];

    for(p in result){
        if(p=='success' || p=='msg') continue;

        items.push(
            {
                xtype:'displayfield',
                fieldLabel:'url',
                value:result[p].url
            },
            {
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
                xtype: 'container',
                flex: .5,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    labelAlign: 'top',
                    fieldLabel: '请求头',
                    value:format(Ext.JSON.encode(result[p].head)),
                    allowBlank: false,
                    minHeight:300,
                    name: 'head',
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: .5,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    labelAlign: 'top',
                    fieldLabel: '请求体',
                    value:format(Ext.JSON.encode(result[p].body)),
                    name: 'body',
                    minHeight:300,
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: 1,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    labelAlign: 'top',
                    fieldLabel: '调用结果',
                    minHeight:300,
                    name: 'orginResult',
                    value:format(Ext.JSON.encode(result[p].result)),
                    anchor:'95%'
                }]
            }]
        });
    }

    if (!isNext) {

        items.push(            {
            xtype: 'container',
            anchor: '100%',
            layout: 'hbox',
            items:[{
                xtype: 'container',
                flex: .5,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    labelAlign: 'top',
                    fieldLabel: '解析函数',
                    value:parseFun,
                    allowBlank: false,
                    minHeight:350,
                    name: 'parseFun',
                    anchor:'95%'
                }]
            },{
                xtype: 'container',
                flex: .5,
                layout: 'anchor',
                items: [{
                    xtype:'textareafield',
                    labelAlign: 'top',
                    fieldLabel: '解析函数结果',
                    value: (function (parseFun) {
                        if(!parseFun) return;
                        var orginResult;
                        for (p in result) {
                            if (p == 'success' || p == 'msg') continue;
                            orginResult = result[p].result;
                        }
                        var fnBody = parseFun.replace(/function\s+\w+\(\w+\)([\s|\S]*)/, '$1');
                        var fnObj=parseFun.replace(/function\s+\w+\((\w+)\)([\s|\S]*)/, '$1');
                        try {
                            eval('var sdsd=function('+fnObj+')'+fnBody);
                            var s=sdsd(orginResult);
                            return format(Ext.JSON.encode(s));
                        }catch (e){
                            return e.toString();
                        }

                    })(parseFun),
                    name: 'result',
                    minHeight:350,
                    anchor:'95%'
                }]
            }]
        });
    }
    var from = Ext.widget({
        xtype: 'form',
        //collapsible: true,
        frame: true,
        bodyPadding: '5 5 0',
        height: 'auto',
        autoScroll:true,
        items: items,
        buttons: (function(isNext){
            if(!isNext){
                return [
                    {
                        text: '测试解析函数',
                        handler: function() {
                            var fun = this.up('form').down('textareafield[name="parseFun"]').getValue();
                            if(!fun) return;
                            var rst=this.up('form').down('textareafield[name="orginResult"]').getValue();
                            var orginResult = eval('['+rst+']');
                            orginResult=orginResult[0];
                            var fnBody = fun.replace(/function\s+\w+\(\w+\)([\s|\S]*)/, '$1');
                            var fnObj=fun.replace(/function\s+\w+\((\w+)\)([\s|\S]*)/, function(w,p1,p2){
                                return p1;
                            });
                            try {
                                eval('var sdsd=function('+fnObj+')'+fnBody);
                                var s=sdsd(orginResult);
                                this.up('form').down('textareafield[name="result"]').setValue(format(Ext.JSON.encode(s)));
                            }catch (e){
                                this.up('form').down('textareafield[name="result"]').setValue(e.toString());
                            }

                        }
                    }
                ]
            }
        })(isNext)
    });
    return from;

}


var panel=Ext.create('Ext.Panel',{
    title:'接口调用配置',
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
