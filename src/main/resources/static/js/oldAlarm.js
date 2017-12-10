var tree=Ext.create('Ext.tree.Panel', {
    title: '告警处理记录',
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
            if(record.raw.hierarchy=2){
                if(Ext.getCmp('tab_'+record.raw.text)){
                    mainPanle.setActiveTab(Ext.getCmp('tab_'+record.raw.text));
                    return;
                }

                var grid1=createGrid(record.raw.id,"2");
                var panel1=Ext.create('Ext.Panel',{
                    id:'tab_'+record.raw.text,
                    title:record.raw.text+"告警处理记录",
                    closable:true,
                    layout:'fit',
                    items:[grid1]
                });

                mainPanle.add(panel1);
                mainPanle.setActiveTab(panel1);

            }

        }
        ,'itemcontextmenu': function(_this, record, item, index, e, eOpts) {

        }

    }

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
