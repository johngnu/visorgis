<%-- 
    Document   : index
    Created on : Apr 14, 2021, 6:05:30 PM
    Author     : John Castillo @johngnu
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!doctype html>
<html lang="es">
    <head>
        <meta charset="utf-8">        
        <link rel="icon" type="image/png" href="img/logo.png" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="author" content="@johngnu">

        <title>Visor Dircembol</title>

        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
        <meta name="viewport" content="width=device-width" />

        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

        <link rel="stylesheet" href="css/module.css">

        <style type="text/css">
            @media screen and (max-width: 768px) {
                .modal-dialog {
                    width: 100%;
                    height: 100%;
                    margin: 0;
                    padding: 0;
                }

                .modal-content {
                    height: auto;
                    min-height: 100%;
                    border-radius: 0;
                }
            }

            .topbutton {
                font-weight: bold;
                position: absolute;
                top: 12px;
                left: 100px;
                z-index: 800;
            }

            .topmessage {
                font-weight: bold;
                position: absolute;
                top: 70px;
                left: 5px;
                z-index: 800;
            }

            .modal-content {
                /*background-color: #337ab721;*/
                background-color: #fff;
            }

            .notes_in {
                position: absolute;
                top: 20px;
                left: 300px;
                z-index: 850;
            }

            .bubble-content {
                position: absolute;
                top: 20px;
                left: 300px;
                z-index: 850;
            }

            .speech-bubble {
                position: relative;
                background: #800040;
                border-radius: .4em;

                padding: 2px 5px;
                margin: 1em 0;
                text-align: center;
                color: white;
                font-weight: bold;
                font-size: 100%;
                text-shadow: 0 -.05em .1em rgba(0,0,0,.3);
            }

            .speech-bubble:after {
                content: '';
                position: absolute;
                bottom: 0;
                left: 50%;
                width: 0;
                height: 0;
                border: 31px solid transparent;
                border-top-color: #800040;
                border-bottom: 0;
                border-left: 0;
                margin-left: -15.5px;
                margin-bottom: -31px;
            }

            .legend {
                background-color: #fff;
                position: absolute; 
                padding-left: 5px;
                bottom: 60px; 
                right: 10px; 
                z-index: 1000;
            }
            .inelogo {
                position: absolute; 		
                top: 20px; 
                right: 10px; 
                z-index: 1000;
            }
            .definiciones {
                background-color: #fff;
                width: 350px;
                max-height: 300px;
                overflow: auto;
                position: absolute; 
                padding-left: 5px;
                top: 160px; 
                right: 10px; 
                z-index: 1000;
                line-height : 18px;
                text-align: justify;
            }
            .form-control {
                font-size: 12px;
            }
        </style>
    </head>

    <body>
        <!-- INE LOGO -->
        <img class="inelogo" src="img/logo-ine.png" alt=""/>        

        <!--
        <div class="legend">
            <img id="legend_image" src="img/leyenda.png"/>
        </div>--> 

        <div id="map"></div>  

        <div class="mcontainer">
            <div class="modal-content">

                <div class="modal-body">
                    <h5>Capas <strong>Base</strong></h5>
                    <select name="_base_layers" class="form-control"></select>

                    <hr>                    

                    <div class="form-group">
                        <label>Indicador:</label>
                        <select id="_indicador" class="form-control">
                            <option value="0">-- Seleccione --</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Categoria:</label>
                        <select id="viewdata_detail" class="form-control"></select>
                    </div>
                    
                    <div class="form-group">
                        <label>Región:</label>
                        <select id="_region" class="form-control">
                            <option value="dep">Departamento</option>
                            <option value="pro">Provincia</option>
                            <option value="mun">Municipio</option>
                        </select>
                    </div>
                    
                    <h5> <strong>Capas</strong></h5>
                    <div class="radio"><label><input type="radio" checked name="_activo" value="act">Activo</label></div>
                    <div class="radio"><label><input type="radio" name="_activo" value="inac">Inactivo</label></div>
                                        
                </div>
            </div>
        </div>

    </body>

    <!-- Core JS Files   -->
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <!-- GIS UI libraries 2015 --> 
    <script src="openlayers/OpenLayers.js"></script>

    <!-- Google Maps API_KEY -->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdSJNaTQvzzlSZIAUsHEfCci2cAIRukP8" type="text/javascript"></script>

    <script src="js/openlayers_cfg.js" type="text/javascript"></script>
    <script type="text/javascript">
        // Create Style
        domain.objects.createStyle = function (max, min, color) {
            // Gradient Color in 5 flags
            var colors = updateSpitter(color);
            var style = new OpenLayers.Style();
            var n = max - min;
            var c = n / 5;
            var rules = new Array();
            var index = min;
            for (var i = 0; i < 5; i++) {
                rules.push(new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.BETWEEN,
                        property: "value",
                        lowerBoundary: index,
                        upperBoundary: (index + c)
                    }),
                    symbolizer: {pointRadius: 10, fillColor: colors[4 - i],
                        fillOpacity: 0.6, strokeColor: colors[4 - i]}
                }));
                index = (index + c) + 1;
            }
            style.addRules(rules);
            return style;
        };

        // Principal Map
        domain.objects.mapa = function (options) {
            OpenLayers.ProxyHost = domain.objects.proxy;
            var map = new OpenLayers.Map("map", Ext.geoOptions);
            map.addControl(new OpenLayers.Control.LayerSwitcher());
            // Add OpenStreetMaps
            map.addLayer(new OpenLayers.Layer.OSM());
            // Add Google Maps
            var gmap = new OpenLayers.Layer.Google(
                    "Google Maps", // the default
                    {numZoomLevels: 20}
            );
            map.addLayer(gmap);
            var ghyb = new OpenLayers.Layer.Google(
                    "Google Satélite",
                    {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
            );
            map.addLayer(ghyb);

            // Departamentos
            var deps = new OpenLayers.Layer.WMS('Límite departamental', 'http://sigedv2.ine.gob.bo/geoserver/geonode/wms', {
                layers: 'geonode:departamentos_bolivia',
                transparent: true,
                format: 'image/png'
            }, {
                buffer: 0,
                displayOutsideMaxExtent: true,
                isBaseLayer: false,
                visibility: true,
                singleTile: true,
                yx: {'EPSG:4326': true}
            });
            map.addLayer(deps);

            // Municipios
            var muns = new OpenLayers.Layer.WMS('Municipios', 'http://sigedv2.ine.gob.bo/geoserver/geonode/wms', {
                layers: 'geonode:municipios_trans',
                transparent: true,
                format: 'image/png'
            }, {
                buffer: 0,
                displayOutsideMaxExtent: true,
                isBaseLayer: false,
                visibility: true,
                singleTile: true,
                yx: {'EPSG:4326': true}
            });
            map.addLayer(muns);

            // style rules (reglas)
            var style = new OpenLayers.Style();
            var rules = [new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.BETWEEN,
                        property: "value",
                        lowerBoundary: 0,
                        upperBoundary: 400
                    }),
                    symbolizer: {pointRadius: 10, fillColor: "#ff9964",
                        fillOpacity: 0.6, strokeColor: "ff9964"}
                }), new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.BETWEEN,
                        property: "value",
                        lowerBoundary: 401,
                        upperBoundary: 800
                    }),
                    symbolizer: {pointRadius: 10, fillColor: "#ff7128",
                        fillOpacity: 0.6, strokeColor: "ff7128"}
                }), new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.BETWEEN,
                        property: "value",
                        lowerBoundary: 801,
                        upperBoundary: 1200
                    }),
                    symbolizer: {pointRadius: 10, fillColor: "#ff7128",
                        fillOpacity: 0.6, strokeColor: "ff7128"}
                }), new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.BETWEEN,
                        property: "value",
                        lowerBoundary: 1201,
                        upperBoundary: 1600
                    }),
                    symbolizer: {pointRadius: 10, fillColor: "#ff6414",
                        fillOpacity: 0.6, strokeColor: "ff6414"}
                }), new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.BETWEEN,
                        property: "value",
                        lowerBoundary: 1601,
                        upperBoundary: 2000
                    }),
                    symbolizer: {pointRadius: 10, fillColor: "#ff5500",
                        fillOpacity: 0.6, strokeColor: "ff5500"}
                })];
            style.addRules(rules);
            this.sdefaltStyle = style;
            // Object selected layer
            this.objectselected = new OpenLayers.Layer.Vector("objectselected", {
                displayInLayerSwitcher: false//,
                        //styleMap: new OpenLayers.StyleMap(style)
                        //style: style
                        /*eventListeners: layerListeners*/
            });
            map.addLayer(this.objectselected);

            if (!map.getCenter()) {
                map.zoomToMaxExtent();
            }
            map.zoomToExtent(Ext.geoBounds);

            return map;
        };

        // On Ready Document (MainApp)
        $(document).ready(function () {

            // Static data
            var myData = [{
                    label: 'Demo',
                    schema: 'cartografia',
                    tview: 'vw_demo',
                    fields: {graco: 'Graco', prico: 'Prico', resto: 'Resto', total: 'Total'},
                    color: '#ff4a4a'
                }, {
                    label: 'Cat contribuyente',
                    schema: 'cartografia',
                    tview: 'vw_cat_contribuyente',
                    fields: {graco: 'Graco', prico: 'Prico', resto: 'Resto', total: 'Total'},
                    color: '#ff4a4a'
                }, {
                    label: 'For jurídica',
                    schema: 'cartografia',
                    tview: 'cw_for_juridica',
                    fields: {graco: 'Graco', prico: 'Prico', resto: 'Resto', total: 'Total'},
                    color: '#000000'
                }, {
                    label: 'Nro. empresas',
                    schema: 'cartografia',
                    tview: 'vw_nro_empresas',
                    fields: {graco: 'Graco', prico: 'Prico', resto: 'Resto', total: 'Total'},
                    color: '#000000'
                }, {
                    label: 'Reg. tributario',
                    schema: 'cartografia',
                    tview: 'vw_nro_empresas',
                    fields: {graco: 'Graco', prico: 'Prico', resto: 'Resto', total: 'Total'},
                    color: '#000000'
                }, {
                    label: 'Tipo contribuyente',
                    schema: 'cartografia',
                    tview: 'vw_tipo_contribuyente',
                    fields: {graco: 'Graco', prico: 'Prico', resto: 'Resto', total: 'Total'},
                    color: '#000000'
                }];

            // Create Map
            var map = domain.objects.mapa();
            domain.objects.imap = map;

            // Custom Layer Swithcer
            var controls = map.getControlsByClass('OpenLayers.Control.LayerSwitcher');
            var baseLayers = $('select[name="_base_layers"]');
            if (controls.length === 1) {
                var swcontrol = controls[0];
                var shtml = '';
                swcontrol.baseLayers.forEach(function (item, index) {
                    var opt = document.createElement('option');
                    opt.value = item.layer.id;
                    opt.innerHTML = item.layer.name;
                    if (index === 0) {
                        opt.selected = true;
                        //shtml += '<div class="checkbox"><label><input checked type="checkbox" name="_base_layers" value="' + item.layer.id + '">' + item.layer.name + '</label></div>';
                    } else {
                        //shtml += '<div class="checkbox"><label><input type="checkbox" name="_base_layers" value="' + item.layer.id + '">' + item.layer.name + '</label></div>';
                    }
                    baseLayers.append(opt);
                });
                //$('#_base_layers').html(shtml);                
                baseLayers.change(function () {
                    var layer = map.getLayersBy('id', $(this).val())[0];
                    map.setBaseLayer(layer);
                });
            }

            var itemSel = null;

            // get data 
            function loadMap(view, field) {
                $.ajax({
                    url: '<c:url value="/geodata"/>',
                    type: "GET",
                    data: {view: view, field: field},
                    success: function (data) {
                        if (data.success) {
                            // deploy
                            var max = 0;
                            var min = 0;
                            data.data.forEach(function (item, index) {
                                if (index === 0) {
                                    max = min = item.value;
                                } else {
                                    if (item.value > max) {
                                        max = item.value;
                                    }
                                    if (item.value < min) {
                                        min = item.value;
                                    }
                                }

                                var f = domain.objects.featureFromText(item.geom, item);
                                domain.objects.selectFeature(map, f, false);
                            });
                            console.log(itemSel);
                            var style = domain.objects.createStyle(max, min, itemSel.color);
                            domain.objects.objectselected.styleMap = new OpenLayers.StyleMap(style);
                            domain.objects.objectselected.redraw();
                        }
                    }
                });
            }

            // load data
            var _indicador = $('#_indicador');
            var _viewdata_detail = $('#viewdata_detail');
            var _region = $('#_region');
            var _activo = $('input:radio[name="_activo"]:checked').val();
            $('input:radio[name="_activo"]').change(function () {
                _activo = $(this).val();
                loadMainData();
            });

            myData.forEach(function (item, index) {
                var opt = document.createElement('option');
                opt.value = item.schema + '.' + item.tview;
                opt.innerHTML = item.label;
                _indicador.append(opt);
            });
            
            function defaultSelector() {
                var opt = document.createElement('option');       
                opt.value = 0;
                opt.innerHTML = '-- Seleccione --';
                _viewdata_detail.append(opt);
            }
            _indicador.change(function () {
                var s = $(this).val();
                _viewdata_detail.empty();
                myData.forEach(function (item, index) {
                    if (s === (item.schema + '.' + item.tview)) {
                        itemSel = item;
                        defaultSelector();
                        for (const pr in item.fields) {
                            var opt = document.createElement('option');
                            opt.value = pr;
                            opt.innerHTML = item.fields[pr];
                            _viewdata_detail.append(opt);
                        }
                    }
                });
                loadMainData();
            });

            _viewdata_detail.change(function () {                
                loadMainData();
            });
            
            _region.change(function () {                
                loadMainData();
            });
            
            function loadMainData() {
                domain.objects.objectselected.removeAllFeatures();
                var i = _indicador.val();
                var v = _viewdata_detail.val();
                var r = _region.val();                
                var ly_data = i + '_' + r + '_' + _activo;
                alert(ly_data); // borra esto
                loadMap(ly_data, v);
            }

        });

    </script>
</html>



