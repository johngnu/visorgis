/**
 * Sitmax ExtJS UI
 * Copyright(c) 2011-2012 ICG Inc.
 * @author Johns Castillo Valencia
 * @class Modules, Modulos del sistema
 */

com.icg.scripts = null;

com.icg.utils = {
    entityInfo: {
        xtype: 'fieldset',
        title: 'Información de la consulta',
        autoHeight: true,
        defaultType: 'textfield',
        defaults: {
            width: 210,
            allowBlank: false,
            msgTarget: 'side'
        },
        items: [{
                fieldLabel: 'Documentación',
                xtype: 'textarea',
                name: 'description',
                allowBlank: true
            }]
    },
    createEntity: function (options) {
        var form = new Ext.FormPanel({
            autoHeight: true,
            border: false,
            labelWidth: 130,
            url: Ext.SROOT + 'command/createcommand',
            bodyStyle: 'padding: 10px',
            layout: 'form',
            autoScroll: true,
            defaults: {
                anchor: '95%',
                msgTarget: 'side'
            },
            items: [{
                    xtype: 'fieldset',
                    title: 'SQL command',
                    autoHeight: true,
                    defaultType: 'textfield',
                    defaults: {
                        width: 210,
                        allowBlank: false,
                        msgTarget: 'side'
                    },
                    items: [{
                            fieldLabel: 'Nombre URI',
                            name: 'name',
                            maxLength: 64,
                            vtype: 'alpha',
                            regex: /^[a-z,_]{0,}$/
                        }, {
                            xtype: 'displayfield',
                            value: '<img src="/ext-3.3.1/resources/images/silk/icons/information.png"/>&nbsp;<i>Ingrese el "Nombre" en minúsculas sin espacios ni números.</i>',
                            width: 300
                        }]
                }, this.entityInfo],
            buttonAlign: 'center',
            buttons: [{
                    text: 'Guardar',
                    formBind: true,
                    handler: function () {
                        if (form.getForm().isValid()) {
                            form.getForm().submit({
                                success: function (form, action) {
                                    options.grid.getStore().reload();
                                    win.close();
                                },
                                failure: function (form, action) {
                                    com.icg.errors.serverError(action);
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: 'Error',
                                msg: 'Hay errores en el formulario.',
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.Msg.ERROR
                            });
                        }
                    }
                }, {
                    text: 'Limpiar',
                    handler: function () {
                        form.getForm().reset();
                    }
                }]
        });
        var win = new Ext.Window({
            iconCls: 'create_entity',
            title: 'Nuevo SQL',
            layout: 'fit',
            width: 550,
            autoHeight: true,
            resizable: false,
            modal: true,
            items: [form]
        });
        win.show();
    },
    editEntity: function (options) {
        var form = new Ext.FormPanel({
            autoHeight: true,
            border: false,
            labelWidth: 130,
            url: Ext.SROOT + 'command/createcommand',
            bodyStyle: 'padding: 10px',
            layout: 'form',
            autoScroll: true,
            defaults: {
                anchor: '95%',
                msgTarget: 'side'
            },
            items: [{
                    xtype: 'fieldset',
                    title: 'SQL command',
                    autoHeight: true,
                    defaultType: 'textfield',
                    defaults: {
                        width: 210,
                        allowBlank: false,
                        msgTarget: 'side'
                    },
                    items: [{
                            fieldLabel: 'Nombre URI',
                            name: 'name',
                            maxLength: 64,
                            vtype: 'alpha',
                            regex: /^[a-z,_]{0,}$/
                        }, {
                            xtype: 'displayfield',
                            value: '<img src="/ext-3.3.1/resources/images/silk/icons/information.png"/>&nbsp;<i>Ingrese el "Nombre" en minúsculas sin espacios ni números.</i>',
                            width: 300
                        }, {
                            xtype: 'hidden',
                            name: 'id'
                        }]
                }, this.entityInfo],
            buttonAlign: 'center',
            buttons: [{
                    text: 'Guardar',
                    formBind: true,
                    handler: function () {
                        if (form.getForm().isValid()) {
                            form.getForm().submit({
                                success: function (form, action) {
                                    options.grid.getStore().reload();
                                    win.close();
                                },
                                failure: function (form, action) {
                                    com.icg.errors.serverError(action);
                                }
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: 'Error',
                                msg: 'Hay errores en el formulario.',
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.Msg.ERROR
                            });
                        }
                    }
                }, {
                    text: 'Limpiar',
                    handler: function () {
                        form.getForm().reset();
                    }
                }]
        });
//        var form = new Ext.FormPanel({
//            autoHeight: true,
//            border: false,
//            labelWidth: 130,
//            url: Ext.SROOT + 'core/editentity',
//            bodyStyle: 'padding: 10px',
//            layout: 'form',
//            autoScroll: true,
//            items: [{
//                    xtype: 'fieldset',
//                    title: 'Entidad',
//                    autoHeight: true,
//                    defaultType: 'textfield',
//                    defaults: {
//                        width: 210,
//                        allowBlank: false,
//                        msgTarget: 'side'
//                    },
//                    items: [{
//                            fieldLabel: 'Nombre',
//                            //fieldLabel: 'Nombre (Solo lectura)',
//                            name: 'name',
//                            maxLength: 64,
//                            //readOnly: true,
//                            vtype: 'alpha',
//                            regex: /^[a-z,_]{0,}$/
//                        }, {
//                            xtype: 'displayfield',
//                            value: '<img src="/ext-3.3.1/resources/images/silk/icons/information.png"/>&nbsp;<i>Ingrese el "Nombre" en minúsculas sin espacios ni números.</i>',
//                            width: 300
//                        }, {
//                            xtype: 'hidden',
//                            name: 'id'
//                        }]
//                }, this.entityInfo],
//            buttonAlign: 'center',
//            buttons: [{
//                    text: 'Guardar',
//                    formBind: true,
//                    handler: function () {
//                        if (form.getForm().isValid()) {
//                            form.getForm().submit({
//                                success: function (form, action) {
//                                    options.grid.getStore().reload();
//                                    win.close();
//                                },
//                                failure: function (form, action) {
//                                    com.icg.errors.serverError(action);
//                                }
//                            });
//                        } else {
//                            Ext.MessageBox.show({
//                                title: 'Error',
//                                msg: 'Hay errores en el formulario.',
//                                buttons: Ext.MessageBox.OK,
//                                icon: Ext.Msg.ERROR
//                            });
//                        }
//                    }
//                }, {
//                    text: 'Limpiar',
//                    handler: function () {
//                        form.getForm().reset();
//                    }
//                }]
//        });
        var win = new Ext.Window({
            iconCls: 'create_entity',
            title: 'Editar comando',
            layout: 'fit',
            width: 550,
            autoHeight: true,
            resizable: false,
            modal: true,
            items: [form]
        });
        win.show();
        if (options.record) {
            form.getForm().loadRecord(options.record);
        }
    },
    deleteEntity: function (options) {
        Ext.MessageBox.confirm('Confirmar', '¿Confirma eliminar SQL Store?', function (r) {
            if (r === 'yes') {
                var id = options.record.data.id;
                Ext.Ajax.request({
                    url: Ext.SROOT + 'command/deletecommand',
                    method: 'POST',
                    params: {
                        id: id
                    },
                    success: function (result, request) {
                        var data = Ext.util.JSON.decode(result.responseText);
                        if (data.success) {
                            options.grid.store.reload();
                            var tab = options.tabs.findById(options.record.data.name);
                            if (tab) {
                                options.tabs.remove(tab);
                            }
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
    lockEntity: function (options) {
        Ext.MessageBox.confirm('Confirmar', '¿Confirma cambiar el estado de bloqueo de la entidad?', function (r) {
            if (r === 'yes') {
                var id = options.record.data.id;
                Ext.Ajax.request({
                    url: Ext.SROOT + 'command/lockentity',
                    method: 'POST',
                    params: {
                        id: id
                    },
                    success: function (result, request) {
                        options.grid.store.reload();
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
    gridResults: function (data) {
        if (data.length > 0) {
            var cols = new Array();
            cols.push(new Ext.grid.RowNumberer({
                width: 27
            }));
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
                title: 'Resultados',
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
    },
    paramsForm: function (options) {
        var form = new Ext.FormPanel({
            border: false,
            autoHeight: true,
            bodyStyle: 'padding:10px',
            labelWidth: 100,
            waitTitle: 'Procesando...',
            defaults: {
                msgTarget: 'side',
                anchor: '90%'
            },
            items: [options.fields]
        });

        var win = new Ext.Window({
            title: 'Parámetros de entrada',
            autoScroll: true,
            width: 500,
            autoHeight: true,
            minWidth: 300,
            items: form,
            modal: true,
            buttons: [{
                    text: 'Ejecutar',
                    iconCls: 'play',
                    handler: function () {
                        if (form.getForm().isValid()) {
                            Ext.Ajax.request({
                                url: options.url,
                                method: 'POST',
                                params: form.getForm().getValues(),
                                success: function (result, request) {
                                    win.close();
                                    var data = Ext.util.JSON.decode(result.responseText);
                                    if (data.success && data.data) {
                                        if (data.data.length > 0) {
                                            var grid = com.icg.utils.gridResults(data.data);
                                            options.presults.removeAll();
                                            options.presults.add(grid);
                                            options.presults.doLayout();
                                        } else {
                                            com.icg.errors.info('Ejecutado correctamente [sin resultados]');
                                        }
                                    } else {
                                        com.icg.errors.serverErrorAjax(data);
                                    }
                                },
                                failure: function (result, request) {
                                    com.icg.errors.error('Error en la petición.');
                                }
                            });
                        } else {
                            com.icg.errors.error('Error en los datos del formularo.');
                        }
                    }
                }]
        });
        win.show();
    }
};

com.icg.EntityManager = {
    renderIcon: function (val) {
        if (!val) {
            return '<img src="' + Ext.IMAGES_SILK + 'accept.png">';
        } else {
            return '<img src="' + Ext.IMAGES_SILK + 'lock.png">';
        }
    },
    entityName: function (val) {
        return ' <span style="font-weight:bold">' + val + '</span>';
    },
    openEntityManager: function (tabs, record) {
        var tab = tabs.findById(record.data.name);
        if (!tab) {
            var grid = com.icg.EntityGrid({record: record});
            var presults = new Ext.Panel({
                border: false,
                height: 100,
                split: true,
                region: 'south',
                layout: 'fit',
                items: []
            });
            var form = new Ext.FormPanel({
                border: false,
                frame: true,
                labelAlign: 'top',
                region: 'center',
                url: Ext.SROOT + 'command/savesql',
                bodyStyle: 'padding: 10px',
                layout: 'form',
                defaults: {
                    anchor: '100%, 100%',
                    msgTarget: 'side'
                },
                items: [{
                        fieldLabel: 'SQL Query',
                        xtype: 'textarea',
                        name: 'sql',
                        allowBlank: true,
                        value: record.data.sqlCommand
                    }, {
                        xtype: 'hidden',
                        name: 'id',
                        value: record.data.id
                    }],
                tbar: [{
                        text: 'Guardar',
                        iconCls: 'save',
                        handler: function () {
                            form.getForm().submit({
                                success: function (form, action) {
                                    com.icg.errors.info('Guardado correctamente.');
                                },
                                failure: function (form, action) {
                                    com.icg.errors.serverError(action);
                                }
                            });
                        }
                    }, '-', {
                        text: 'Ejecutar',
                        iconCls: 'play',
                        handler: function () {

                            if (grid.store.getCount() > 0) {
                                var fields = new Array();
                                grid.store.each(function (record) {
                                    fields.push({
                                        xtype: 'textfield',
                                        fieldLabel: record.data.name,
                                        allowBlank: false,
                                        name: record.data.name
                                    });
                                });
                                com.icg.utils.paramsForm({
                                    fields: fields,
                                    url: Ext.SROOT + 'command/select/query/' + record.data.name,
                                    presults: presults
                                });
                            } else {
                                Ext.Ajax.request({
                                    url: Ext.SROOT + 'command/select/query/' + record.data.name,
                                    method: 'POST',
                                    params: {
                                        //___test_param_sw : true
                                    },
                                    success: function (result, request) {
                                        var data = Ext.util.JSON.decode(result.responseText);
                                        if (data.success && data.data) {
                                            if (data.data.length > 0) {
                                                var grid = com.icg.utils.gridResults(data.data);
                                                presults.removeAll();
                                                presults.add(grid);
                                                presults.doLayout();
                                            } else {
                                                com.icg.errors.info('Ejecutado correctamente [sin resultados]');
                                            }
                                        } else {
                                            com.icg.errors.serverErrorAjax(data);
                                        }
                                    },
                                    failure: function (result, request) {

                                    }
                                });
                            }
                        }
                    }]
            });
            tab = new Ext.Panel({
                id: record.data.id,
                iconCls: 'module-app',
                layout: 'border',
                border: false,
                frame: false,
                closable: true,
                title: record.data.name,
                items: [grid, form, presults]
            });
            tabs.add(tab);
        }
        tabs.add(tab);
    },
    /**
     * Maim Entity Grid 
     * Version 2012, update 2014
     * @author: Johns Castillo Valencia
     **/
    init: function () {
        var store = new Ext.data.JsonStore({
            url: Ext.SROOT + 'command/listcommands',
            idProperty: 'id',
            root: 'data',
            fields: ['id', 'name', 'description', 'sqlCommand', {name: 'locked', type: 'boolean'}],
            autoLoad: true
        });

        var expander = new Ext.ux.grid.RowExpander({
            tpl: new Ext.Template('<p><b>Documentación:</b>&nbsp;{description}</p>'),
            enableCaching: false
        });

        var grid = new Ext.grid.GridPanel({
            title: 'SQL Store',
            region: 'west',
            width: 370,
            maxWidth: 500,
            minWidth: 300,
            collapsible: true,
            split: true,
            store: store,
            loadMask: true,
            selModel: new Ext.grid.RowSelectionModel({
                singleSelect: true
            }),
            plugins: expander,
            columns: [expander, {
                    header: "Nombre",
                    sortable: true,
                    dataIndex: 'name',
                    width: 150
                }, {
                    header: "Bloqueado",
                    sortable: false,
                    align: 'center',
                    width: 100,
                    dataIndex: 'locked',
                    renderer: this.renderIcon
                }],
            tbar: [{
                    iconCls: 'refresh',
                    tooltip: 'Actualizar',
                    handler: function () {
                        grid.store.reload();
                    }
                }, '-', {
                    iconCls: 'create',
                    tooltip: 'Nuevo SQL Command',
                    text: 'Nuevo',
                    handler: function () {
                        com.icg.utils.createEntity({
                            grid: grid
                        });
                    }
                }, {
                    text: 'Editar',
                    iconCls: 'update',
                    tooltip: 'Editar SQL Store',
                    handler: function () {
                        var record = grid.getSelectionModel().getSelected();
                        if (record) {
                            com.icg.utils.editEntity({
                                grid: grid,
                                record: record
                            });
                        } else {
                            com.icg.errors.mustSelect();
                        }
                    }
                }, {
                    text: 'Eliminar',
                    iconCls: 'delete',
                    tooltip: 'Eliminar SQL Store',
                    handler: function () {
                        var record = grid.getSelectionModel().getSelected();
                        if (record) {
                            com.icg.utils.deleteEntity({
                                grid: grid,
                                record: record,
                                tabs: tabs
                            });
                        } else {
                            com.icg.errors.mustSelect();
                        }
                    }
                }, '-', {
                    iconCls: 'lock_edit',
                    tooltip: 'Bloquear/desbloquear entidad.',
                    handler: function () {
                        var record = grid.getSelectionModel().getSelected();
                        if (record) {
                            com.icg.utils.lockEntity({
                                grid: grid,
                                record: record,
                                tabs: tabs
                            });
                        } else {
                            com.icg.errors.mustSelect();
                        }
                    }
                }]
        });

        grid.getSelectionModel().on('rowselect', function rowselected(sm, rowindex, record) {
            com.icg.EntityManager.openEntityManager(tabs, record);
        });

        // Tab principal. con informacion o documentacion
        var start = new Ext.Panel({
            title: "Inicio",
            bodyStyle: 'padding:25px',
            contentEl: 'start-div'
        });

        var tabs = new Ext.TabPanel({
            resizeTabs: true,
            minTabWidth: 115,
            tabWidth: 135,
            frame: true,
            border: false,
            activeTab: 0,
            enableTabScroll: true,
            region: 'center',
            defaults: {autoScroll: true},
            items: [start],
            plugins: new Ext.ux.TabCloseMenu(),
            listeners: {
                tabchange: function (tabs, tab) {
                    if (tab) {
                        grid.getSelectionModel().selectRow(store.indexOf(store.getById(tab.id)));
                    }
                }
            }
        });

        new Ext.Viewport({
            border: false,
            layout: 'border',
            split: true,
            items: [grid, tabs]
        });
    }
};

Ext.onReady(com.icg.EntityManager.init, com.icg.EntityManager);