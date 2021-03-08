/*!
 * ICG - International Consulting Group, 2011
 * 
 */

com.icg.entity = {
    buttons: function (options, esaveupdate, edelete) {
        var tbar = [{
                iconCls: 'refresh',
                tooltip: 'Actualizar',
                handler: function () {
                    options.target.store.reload();
                }
            }, '-', {
                iconCls: 'create',
                tooltip: 'Nuevo',
                disabled: !esaveupdate,
                handler: function () {
                    options.entity_id = undefined;
                    options.record = undefined;
                    com.icg.entity.viewform(options);
                }
            }, {
                iconCls: 'update',
                tooltip: 'Modificar',
                disabled: !esaveupdate,
                handler: function () {
                    var record = options.target.getSelectionModel().getSelected();
                    if (!record) {
                        Ext.MessageBox.show({
                            title: 'Aviso',
                            msg: 'Debe seleccionar un <b>Registro</b>.',
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.Msg.INFO
                        });
                    } else {
                        options.entity_id = record.get(options.target.store.idProperty);
                        com.icg.entity.viewform(options);
                    }
                }
            }, {
                iconCls: 'delete',
                tooltip: 'Eliminar',
                disabled: !edelete,
                handler: function () {
                    var record = options.target.getSelectionModel().getSelected();
                    if (!record) {
                        Ext.MessageBox.show({
                            title: 'Aviso',
                            msg: 'Debe seleccionar un <b>Registro</b>.',
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.Msg.INFO
                        });
                    } else {
                        Ext.MessageBox.confirm('Confirmar', '¿Confirma eliminar el registro? Se perderan Datos.', function (r) {
                            if (r == 'yes') {
                                var id = record.get(options.target.store.idProperty);
                                var box = Ext.MessageBox.wait('Por favor espere.', 'Eliminando el <b>registro</b>');
                                Ext.Ajax.request({
                                    url: 'entity/delete/' + options.id,
                                    method: 'POST',
                                    params: {
                                        entity_id: id
                                    },
                                    success: function (result, request) {
                                        //    console.log(request);
                                        //                                        var r = Ext.util.JSON.decode(result.responseText);
                                        //                                        if(!r.success) {
                                        //                                            com.icg.sitmax.security.Login();
                                        //                                        } else {                                        
                                        options.target.getStore().reload();
                                        //                                        }                                        
                                        box.hide();
                                    },
                                    failure: function (result, request) {
                                        if (result.status === '403') {
                                            com.icg.sitmax.security.msg.e403();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }, '-', {
                iconCls: 'history',
                tooltip: 'Historico del registro',
                handler: function () {
                    var record = options.target.getSelectionModel().getSelected();
                    if (record) {
                        var _oid = record.data[options.id + '_id'];
                        var op = {
                            id: options.id + '/' + _oid,
                            iconCls: 'history',
                            url: 'history/' + options.id + '/' + _oid,
                            title: options.name + '/' + _oid
                        };
                        top.com.icg.openModule(op);
                    } else {
                        com.icg.errors.mustSelect();
                    }
                }
            }];
        return tbar;
    },
    formbuttons: function (entity) {
        var tbar = [{
                iconCls: 'silk-add',
                text: 'Nuevo',
                handler: function () {
                    com.icg.entity.viewform({
                        url: 'persona_telefonos',
                        text: 'Telefonos',
                        id: 'persona_telefonos'
                    })
                }
            }, '-', {
                iconCls: 'silk-table-refrosh',
                text: 'Modificar'
            }, '-', {
                iconCls: 'silk-delete',
                text: 'Eliminar'
            }];
        return tbar;
    },
    copy: null,
    attributeTypes: [['character varying', 'Cadena'], ['text', 'Texto'], ['integer', 'Entero'], ['bigint', 'Entero largo (bigint)'], ['double precision', 'Real'], ['boolean', 'Boolean']/*, ['date', 'Fecha'], ['time without time zone', 'Hora'], ['lob', 'Binario'], ['catalogo', 'Catálogo'], ['entity', 'Entidad']/*, ['geometry', 'Geométrico'], ['list', 'Lista'],['entity','Entidad agregada (1/n)'],['centity','Entidad compuesta (n)'],['class','Entidad compuesta (1)']*/],
    newAttribute: function (options, grid, paste) {
        var form = new Ext.FormPanel({
            autoHeight: true,
            border: false,
            labelWidth: 150,
            url: Ext.SROOT + 'command/createparam',
            bodyStyle: 'padding: 10px',
            layout: 'form',
            autoScroll: true,
            items: [{
                    xtype: 'fieldset',
                    title: 'Información del parámetro',
                    autoHeight: true,
                    defaultType: 'textfield',
                    defaults: {
                        width: 210,
                        allowBlank: false,
                        msgTarget: 'side'
                    },
                    items: [{
                            fieldLabel: 'Nombre',
                            name: 'name',
                            maxLength: 64,
                            vtype: 'alpha',
                            regex: /^[a-z,_]{0,}$/
                        }, {
                            xtype: 'displayfield',
                            value: '<img src="/ext-3.3.1/resources/images/silk/icons/information.png"/>&nbsp;<i>El "Nombre" debe ser único en la estructura de parámetros.</i>',
                            width: 300
                        }, {
                            xtype: 'combo',
                            fieldLabel: 'Tipo',
                            hiddenName: 'typeClass',
                            forceSelection: true,
                            store: new Ext.data.ArrayStore({
                                fields: ['typeClass', 'objeto'],
                                data: this.attributeTypes
                            }),
                            valueField: 'typeClass',
                            displayField: 'objeto',
                            typeAhead: true,
                            mode: 'local',
                            triggerAction: 'all',
                            emptyText: 'Selecione el tipo...',
                            selectOnFocus: true
                        }, {
                            xtype: 'textarea',
                            fieldLabel: 'Documentación',
                            name: 'description',
                            allowBlank: true
                        }, {
                            xtype: 'hidden',
                            name: 'id',
                            value: options.record.data.id
                        }]
                }]
        });

        var win = new Ext.Window({
            iconCls: 'entity-form',
            title: 'Nuevo parámetro de ' + options.record.data.name,
            layout: 'fit',
            width: 550,
            autoHeight: true,
            resizable: false,
            plain: true,
            modal: true,
            items: [form],
            buttonAlign: 'center',
            buttons: [{
                    text: 'Guardar',
                    formBind: true,
                    handler: function () {
                        form.getForm().submit({
                            success: function (form, action) {
                                grid.store.reload();
                                win.close();
                            },
                            failure: function (form, action) {
                                com.icg.errors.serverError(action);
                            }
                        });
                    }
                }, {
                    text: 'Limpiar',
                    handler: function () {
                        form.getForm().reset();
                    }
                }]
        });
        win.show();
        if (paste) {
            form.getForm().loadRecord(com.icg.entity.copy);
        }
    },
    deleteAttribute: function (options) {
        Ext.MessageBox.confirm('Confirmar', '¿Confirma eliminar el parámetro?', function (r) {
            if (r === 'yes') {
                var id = options.record.data.id;
                Ext.Ajax.request({
                    url: Ext.SROOT + 'command/removeparam',
                    method: 'POST',
                    params: {
                        id: options.id,
                        key: options.record.data.name // ParamName
                    },
                    success: function (result, request) {
                        var data = Ext.util.JSON.decode(result.responseText);
                        if (data.success) {
                            options.grid.store.reload();
                        } else {
                            com.icg.errors.serverErrorAjax(data);
                        }
                    },
                    failure: function (result, request) {
                        if (result.status === '403') {
                            domain.sitmax.security.msg.e403();
                        }
                    }
                });
            }
        });
    },
    view: function (data) {
        if (data.length > 0) {
            var cols = new Array();
            var fields = new Array();
            for (var prop in data[0]) {
                var col = {
                    header: prop,
                    dataIndex: prop
                };
                cols.push(col);
                var field = {
                    name: prop
                };
                fields.push(field);
            }

            var grid = new Ext.grid.GridPanel({
                title: 'Results',
                width: 400,
                height: 190,
                store: new Ext.data.JsonStore({
                    fields: fields,
                    data: data,
                    autoLoad: true
                }),
                columns: cols
            });
            return grid;
        }
        return undefined;
    }
};

com.icg.EntityManager = {
    init: function () {

        var options = {
            url: __entityId.id,
            name: __entityId.name,
            id: __entityId.id
        };

        var entityGrid = com.icg.EntityGrid(options);

        new Ext.Viewport({
            layout: 'border',
            defaults: {
                split: true,
                frame: false,
                border: false
            },
            items: [entityGrid/*,{
             xtype:'panel',
             height:200,
             border:false,
             region:'south',
             //layout:'border',
             //items:[entityBlocks,entityGroups,groupItems]
             items:[{
             title:'Opciones',
             region:'south'
             }]
             }*/]
        });



    }
};

com.icg.formBuilder = function (type) {
    return com.icg.formBuilderX[type];
};

com.icg.Entityform = function (options) {

    console.log(options.record);

    var attrClass = Ext.data.Record.create([
        {name: 'name', type: 'string'},
        {name: 'label', type: 'string'}
    ]);
    var onSelectType = function (combo, record, index) {
        switch (record.data.type) {
            case 'string':
                form.remove(1);
                form.add(com.icg.formBuilder('string'));
                break;
            case 'integer':
                form.remove(1);
                form.add(com.icg.formBuilder('integer'));
                break;
            case 'float':
                form.remove(1);
                form.add(com.icg.formBuilder('float'));
                break;
            case 'date':
                form.remove(1);
                form.add(com.icg.formBuilder('date'));
                break;
            case 'boolean':
                form.remove(1);
                form.add(com.icg.formBuilder('sino'));
                break;
            case 'entity':
                form.remove(1);
                form.add(com.icg.formBuilderX.entity(form));
                break;
            case 'catalogo':
                form.remove(1);
                form.add(com.icg.formBuilderX.catalogo(form));
                break;
            case 'geometry':
                form.remove(1);
                var input = new attrClass({
                    name: 'the_geom',
                    label: 'Geométrico'
                });
                form.getForm().loadRecord(input);
                break;
            default:
                form.remove(1);
        }
        form.doLayout();
    };

    var form = new Ext.FormPanel({
        autoHeight: true,
        border: false,
        labelWidth: 150,
        //waitMsgTarget:true,
        //url: Ext.SROOT + 'load/add/' + options.id,
        url: Ext.SROOT + 'load/' + (options.record ? 'update/' : 'add/') + options.id,
        bodyStyle: 'padding: 10px',
        layout: 'form',
        autoScroll: true,
        items: [{
                xtype: 'fieldset',
                title: 'Informaci&oacute;n del atributo',
                autoHeight: true,
                defaultType: 'textfield',
                defaults: {
                    width: 210,
                    allowBlank: false,
                    msgTarget: 'side'
                },
                items: [{
                        fieldLabel: 'Nombre',
                        name: 'name',
                        maxLength: 64,
                        vtype: 'alpha',
                        regex: /^[a-z,_]{0,}$/
                    }, {
                        xtype: 'displayfield',
                        value: '<i>El "Nombre" es único en la estructura de la entidad (nombre de columna)</i>',
                        width: 300
                    }, {
                        fieldLabel: 'Etiqueta',
                        name: 'label',
                        maxLength: 128
                    }, {
                        xtype: 'combo',
                        fieldLabel: 'Tipo',
                        hiddenName: 'type',
                        forceSelection: true,
                        store: new Ext.data.ArrayStore({
                            fields: ['type', 'objeto'],
                            data: [/*['blob','Binario'],*/['string', 'Cadena'], ['text', 'Texto'], ['integer', 'Entero'], ['float', 'Real'], ['boolean', 'Si o No'], ['date', 'Fecha'], ['time', 'Hora'], /*['timestamp','Fecha y Hora'],*/['catalogo', 'Catálogo'], ['entity', 'Entidad'], ['geometry', 'Geométrico']/*,['subentity','Entidad propia'],['entity','Entidad agregada (1/n)'],['centity','Entidad compuesta (n)'],['class','Entidad compuesta (1)']*/]
                        }),
                        valueField: 'type',
                        displayField: 'objeto',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText: 'Selecione el tipo...',
                        selectOnFocus: true,
                        listeners: {
                            select: onSelectType
                        }
                    }, {
                        xtype: 'checkbox',
                        fieldLabel: 'Aceptar nulo',
                        name: 'nullable',
                        checked: true
                    }, {
                        xtype: 'textarea',
                        fieldLabel: 'Documentación',
                        name: 'description',
                        allowBlank: true
                    }]
            }]
    });

    if (options.record) {
        onSelectType(null, options.record, null);
        //form.getForm().loadRecord(options.record);
        setTimeout(function () {
            form.getForm().loadRecord(options.record);
        }, 700);

    }
    return form;
};

com.icg.updateEntityform = function (options) {

    var form = new Ext.FormPanel({
        autoHeight: true,
        border: false,
        labelWidth: 150,
        //waitMsgTarget:true,
        url: Ext.SROOT + 'load/update/' + options.id,
        bodyStyle: 'padding: 10px',
        layout: 'form',
        autoScroll: true,
        items: [{
                xtype: 'fieldset',
                title: 'Informaci&oacute;n del atributo',
                autoHeight: true,
                defaultType: 'textfield',
                defaults: {
                    width: 210,
                    allowBlank: false,
                    msgTarget: 'side'
                },
                items: [{
                        xtype: 'hidden',
                        name: 'name'
                    }, {
                        fieldLabel: 'Etiqueta',
                        name: 'label',
                        maxLength: 128
                    }, {
                        xtype: 'textarea',
                        fieldLabel: 'Documentaci&oacute;n',
                        name: 'description',
                        allowBlank: true
                    }]
            }]
    });

//    if(options.record) {        
//        form.getForm().loadRecord(options.record);
//    }
    return form;
};

com.icg.EntityGrid = function (options) {
    var sql_id = options.record.data.id;
    var dataStore = new Ext.data.JsonStore({
        url: Ext.SROOT + 'command/listparams',
        idProperty: 'name',
        root: 'data',
        fields: ['name', 'typeClass', 'label', 'description', 'readOnly', 'hasError'],
        autoLoad: true,
        baseParams: {
            id: sql_id
        }
    });

//    var nameRenderer = function (val, meta, record) {
//        if (record.data.hasError) {
//            return '<span style="font-weight:bold;color:red">(error) <span style="text-decoration:line-through">'
//                    + val + '</span></span>';
//        } else if (record.data.readOnly) {
//            return '<span style="font-weight:bold;color:#CCC;text-decoration:under-line">'
//                    + val + '</span>';
//        } else {
//            return val;
//        }
//    };

    var grid = new Ext.grid.GridPanel({
        store: dataStore,
        region: 'north',
        height: 100,
        minHeight: 70,
        split: true,
        loadMask: true,
        selModel: new Ext.grid.RowSelectionModel({
            singleSelect: true
        }),
        viewConfig: {
            //Return CSS class to apply to rows depending upon data values
            getRowClass: function (record, index) {
                if (record.data.hasError) {
                    return 'has-error';
                }
                if (record.data.readOnly) {
                    return 'read-only';
                }
            }
        },
        border: false,
        frame: false,
        columns: [new Ext.grid.RowNumberer({
                widht: 25
            }), {
                id: 'name',
                header: "Nombre",
                width: 120,
                sortable: true,
                dataIndex: 'name'
            }, {
                header: "Tipo",
                width: 120,
                sortable: true,
                dataIndex: 'typeClass'
            }, {
                header: "Descripción",
                width: 250,
                sortable: true,
                dataIndex: 'description'
            }],
        tbar: [{
                iconCls: 'refresh',
                tooltip: 'Actualizar',
                handler: function () {
                    grid.store.reload();
                }
            }, '-', {
                text: 'Nuevo...',
                iconCls: 'create',
                tooltip: 'Agrega parámetro',
                handler: function () {
                    com.icg.entity.newAttribute(options, grid);
                }
            }, '-', {
                text: 'Eliminar',
                iconCls: 'delete',
                tooltip: 'Eliminar Atributo',
                handler: function () {
                    var record = grid.getSelectionModel().getSelected();
                    if (record) {
                        com.icg.entity.deleteAttribute({
                            id: sql_id,
                            record: record,
                            grid: grid
                        });
                    } else {
                        com.icg.errors.mustSelect();
                    }
                }
            }]
    });

    return grid;
};


