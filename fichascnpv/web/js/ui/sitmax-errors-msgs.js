
Ext.ns('com.icg.errors');

com.icg.errors.mustSelect = function () {
    Ext.MessageBox.show({
        title: 'Aviso',
        msg: 'Debe seleccionar un <b>Registro</b>.',
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.INFO
    });
};

com.icg.errors.mustSelectFeature = function () {
    Ext.MessageBox.show({
        title: 'Aviso',
        msg: 'Debe seleccionar un <b>Objeto del mapa</b>.',
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.INFO
    });
};

com.icg.errors.formError = function () {
    Ext.MessageBox.show({
        title: 'Aviso',
        msg: 'Debe seleccionar un <b>Registro</b>.',
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.INFO
    });
};

com.icg.errors.error = function (error) {
    Ext.MessageBox.show({
        title: 'Error',
        msg: error,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.ERROR
    });
};

com.icg.errors.warn = function (msg) {
    Ext.MessageBox.show({
        title: 'Aviso',
        msg: msg,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.WARNING
    });
};

com.icg.errors.info = function (msg) {
    Ext.MessageBox.show({
        title: 'Información',
        msg: msg,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.INFO
    });
};

com.icg.errors.submitFailure = function (title, msg) {
    Ext.MessageBox.show({
        title: title,
        msg: msg,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.ERROR
    });
};

com.icg.errors.openLog = function (url) {
    window.open(Ext.SROOT + url, '_blank', 'toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=0,width=700,height=600');
};

com.icg.errors.serverError = function (action) {
    if (action.failureType === 'server' || action.failureType === 'connect') {
        var httpStatus = null;
        if (action) {
            var response = action.response;
            if (response.status !== 200) {
                httpStatus = response.status ? 'HTTP: ' + response.status : '';
                httpStatus += response.statusText ? ' : ' + response.statusText : '';
            } else {
                httpStatus = action.result.errorMessage ? 'Server: ' + action.result.errorMessage : 'No se ha enviado la causa del error.';
                if (action.result.errorUrl) {
                    httpStatus += '<br/>Ver <a href="#" onclick="com.icg.errors.openLog(\'' + action.result.errorUrl + '\')">error</a>';
                }
            }
        }

        Ext.MessageBox.show({
            title: 'Error',
            msg: 'Ha ocurrido un GRAVE error en el Servidor.<br> ' + httpStatus,
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.ERROR
        });
    }
    if (action.failureType === 'client') {
        Ext.MessageBox.show({
            title: 'Error',
            msg: 'Hay errores en el formulario que pretende enviar.',
            buttons: Ext.MessageBox.OK,
            icon: Ext.Msg.ERROR
        });
    }
};

com.icg.errors.serverErrorAjax = function (action) {
    var httpStatus = null;
    if (action) {
        httpStatus = action.errorMessage ? 'Server: ' + action.errorMessage : 'No se ha enviado la causa del error.';
    }
    if (action.errorUrl) {
        httpStatus += '<br/>Ver <a href="#" onclick="com.icg.errors.openLog(\'' + action.errorUrl + '\')">error</a>';
    }
    Ext.MessageBox.show({
        title: 'Error',
        msg: 'Ha ocurrido un GRAVE error en el Servidor.<br> ' + httpStatus,
        buttons: Ext.MessageBox.OK,
        icon: Ext.Msg.ERROR
    });
};

Ext.ns('com.icg.message');

com.icg.message.deleteConfirm = function (m) {
    var msg = '¿Confirma eliminar el registro? Se perderan Datos.';
    if (m) {
        msg = msg + '\n' + m;
    }
    Ext.MessageBox.confirm('Confirmar', msg, function (r) {
        if (r === 'yes') {

        }
    });
};