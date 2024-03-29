<%-- 
    Document   : visorevento index
    Created on : Oct 14, 2020, 6:05:30 PM
    Author     : John Castillo @johngnu
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!doctype html>
<html lang="es">
    <head>
        <meta charset="utf-8">
        <meta name="author" content="@INE_Bolivia">
        <link rel="icon" type="image/png" href="img/logo.png" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <title>Visor de Eventos</title>

        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
        <meta name="viewport" content="width=device-width" />

        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

        <link rel="stylesheet" href="css/module.css">
        <link rel="stylesheet" href="css/ol2-sidebar.css" />

        <style type="text/css">
            body {
                padding: 0;
                margin: 0;
                overflow: hidden;
            }

            html, body, #map {
                height: 100%;
                font: 10pt "Helvetica Neue", Arial, Helvetica, sans-serif;
            }

            .lorem {
                font-style: italic;
                color: #AAA;
            }
            
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
		padding-left: 5px;
                bottom: 60px; 
                left: 10px; 
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
        
        <div id="sidebar" class="sidebar collapsed">
            <!-- Nav tabs -->
            <div class="sidebar-tabs">
                <ul role="tablist">
                    <li><a href="#home" role="tab"><i class="fa fa-bars"></i></a></li>
                    <!--<li><a href="#profile" role="tab"><i class="fa fa-edit"></i></a></li>-->
                    <!--<li><a href="#ncapas" role="tab"><i class="fa fa-tasks"></i></a></li>-->
                </ul>

                <ul role="tablist">
                    <li><a href="#settings" role="tab"><i class="fa fa-gear"></i></a></li>
                </ul>
            </div>
            
            <!-- Tab panes -->
            <div class="sidebar-content">
                <div class="sidebar-pane" id="home">
                    <h1 class="sidebar-header">
                        Datos
                        <span class="sidebar-close"><i class="fa fa-caret-left"></i></span>
                    </h1>
                    
                    <div class="modal-body">
                        <h5>Capas <strong>Base</strong></h5>
                        <select name="_base_layers" class="form-control"></select>

                        <hr>                    

                        <div class="form-group">
                            <label>Gesti�n:</label>
                            <select id="gestion" class="form-control">
                                <option value="0">-- Seleccione --</option>
                                <option value="2019">2019</option>
                                <option value="2020">2020</option>
                                <option value="2021">2021</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Mes de la Gesti�n:</label>
                            <select id="mes" class="form-control">
                                <option value="0">-- Seleccione --</option>
                                <option value="1">Enero</option>
                                <option value="2">Febrero</option>
                                <option value="3">Marzo</option>
                                <option value="4">Abril</option>
                                <option value="5">Mayo</option>
                                <option value="6">Junio</option>
                                <option value="7">Julio</option>
                                <option value="8">Agosto</option>
                                <option value="9">Septiembre</option>
                                <option value="10">Octubre</option>
                                <option value="11">Noviembre</option>
                                <option value="12">Diciembre</option>
                            </select>
                        </div>

                        <hr>                    

                        <div class="form-group">
                            <label>Eventos Adversos IPP:</label>
                            <select id="cpasado" class="form-control">
                                <option value="0">-- Seleccione --</option>
                                <option value="c1_graniz">Granizo</option>
                                <option value="c2_inunda">Inundaci�n</option>
                                <option value="c3_contam">Contaminaci�n</option>
                                <option value="c4_sequia">Sequ�a</option>
                                <option value="c5_helada">Helada</option>
                            </select>
                        </div>

                        <label> Eventos Adversos SINAGER:</label>
                        <div style="height: 135px; overflow: auto">
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Contaminaci�n"> Contaminaci�n </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Granizadas"> Granizadas </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Heladas"> Heladas </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Incendios"> Incendios </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Inundaci�n"> Inundaci�n </label></div>

                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Movimiento de Masas"> Movimiento de Masas </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Nevadas"> Nevadas </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Plagas"> Plagas </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Sequ�a"> Sequ�a </label></div>
                            <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Tormenta"> Tormenta </label></div>
                        </div>
                        <hr>    
                        <div>
                            <div class="checkbox"><label><input type="checkbox" name="_t_riego" value="Riego"> Proyectos <strong>Mi Riego</strong> </label></div>
                        </div>
                        
                        <br>
                        <div class="legend">
                            <img id="legend_image" src="img/leyenda.png"/>
                        </div>
                    </div>
                    
                </div>

                <!--
                <div class="sidebar-pane" id="profile">
                    <h1 class="sidebar-header">Definici�n<span class="sidebar-close"><i class="fa fa-caret-left"></i></span></h1>
                    
                    <div class="modal-body">
                        <h4></h4>
                        <div id="_radio_option"></div>    
                        <hr>
                        <div id="_check_option"></div>    
                    </div>
                    
                </div>
                -->

                <div class="sidebar-pane" id="settings">
                    <h1 class="sidebar-header">Opciones<span class="sidebar-close"><i class="fa fa-caret-left"></i></span></h1>
                </div>
            </div>
                        
        </div>

        <div id="map" class="sidebar-map"></div>
        
        <!--<img class="inelogo" src="img/logo-ine.png" alt=""/>-->
    
    </body>

    <!-- Core JS Files   -->
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/jquery-sidebar.js"></script>

    <!-- GIS UI libraries 2015 --> 
    <script src="openlayers/OpenLayers.js"></script>

    <!-- Google Maps API_KEY -->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdSJNaTQvzzlSZIAUsHEfCci2cAIRukP8" type="text/javascript"></script>

    <script src="js/openlayers_cfg.js" type="text/javascript"></script>
    <script type="text/javascript">

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
                    "Google Sat�lite",
                    {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
            );
            map.addLayer(ghyb);
            
            // Departamentos
            var deps = new OpenLayers.Layer.WMS('L�mite departamental', 'http://sigedv2.ine.gob.bo/geoserver/geonode/wms', {
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
            
            this.eventos = new OpenLayers.Layer.Vector("eventos", {
                displayInLayerSwitcher: false,
                styleMap: new OpenLayers.StyleMap({
                    "default": domain.objects.eventStyle,
                    "select": new OpenLayers.Style({
                        fillColor: "fuchsia"
                    })
                })  //style
            });
            map.addLayer(this.eventos);
            
            var layerListeners = {
                featureclick: function(e) {
                    //log(e.object.name + " says: " + e.feature.id + " clicked.");
                    //console.log(e.feature.data);
                    return false;
                },
                nofeatureclick: function(e) {
                    //log(e.object.name + " says: No feature clicked.");
                }
            };

            // Object selected layer
            this.objectselected = new OpenLayers.Layer.Vector("objectselected", {
                displayInLayerSwitcher: false,
                styleMap: domain.objects.styles,
                eventListeners: layerListeners
            });
            map.addLayer(this.objectselected);
            
            this.riego = new OpenLayers.Layer.Vector("riego", {
                displayInLayerSwitcher: true,
                styleMap: new OpenLayers.StyleMap({
                    "default": new OpenLayers.Style(null, {
                        rules: [
                            new OpenLayers.Rule({
                                symbolizer: {
                                    "Point": {
                                        pointRadius: 7,
                                        fillColor: "#ff0000",
                                        fillOpacity: 0.25,
                                        strokeWidth: 2,
                                        strokeOpacity: 1,
                                        strokeColor: "#ff0000"
                                    }
                                }
                            })
                        ]
                    })
                })
            });
            map.addLayer(this.riego);
            
            var selectControl = new OpenLayers.Control.SelectFeature (
                [this.objectselected, this.eventos, this.riego], {
                    clickout: true, toggle: false,
                    multiple: false, hover: false,
                    
                    //toggleKey: "ctrlKey", // ctrl key removes from selection
                    //multipleKey: "shiftKey" // shift key adds to selection
                    //onSelect: function (e) { ... process feature hover ...  }, 
                    clickFeature: function (e) {
                        if(e.layer.name === 'objectselected') {
                            domain.objects.popupPuntos(e, map); 
                        } else {
                            domain.objects.popupEventos(e, map);
                        }
                    } 
                }
            );
            
            map.addControl(selectControl);
            selectControl.activate();

            // this.control = control;
            map.events.register("changebaselayer", this, function (obj) {

            });

            map.events.register("zoomend", map, function (obj) {

            });

            map.events.register("loadend", map, function (obj) {

            });

            if (!map.getCenter()) {
                map.zoomToMaxExtent();
            }
            map.zoomToExtent(Ext.geoBounds);

            return map;
        };
        
        // Glove Info
        domain.objects.popupPuntos = function (feature, map) {
            var predioDetails = '<div class="card-content">';
            var keys = ['dpto', 'prov', 'mun', 'comu', 'prod'];
            keys.forEach(function (key, index) {
                if(feature.data[key])
                    predioDetails += '<strong>' + key + ': ' + feature.data[key] + '</strong><br>';        
            });

            predioDetails += '</div>';
            // info popup
            var popup = new OpenLayers.Popup.FramedCloud("PoputDetails",
                    feature.geometry.bounds.getCenterLonLat(),
                    new OpenLayers.Size(100, 100),
                    predioDetails,
                    null,
                    true // <-- true if we want a close (X) button, false otherwise
                    );
            popup.autoSize = true;
            map.addPopup(popup);
        };
        
        domain.objects.popupEventos = function (feature, map) {
            //console.log(feature)
            var predioDetails = '<div class="card-content">';
            var keys = ['departamento', 'municipio', 'fam_afec', 'has_afec', 'gan_afec', 'viv_afec', 'muertos', 'desaparecido'];
            keys.forEach(function (key, index) {
                //console.log(feature.data[key]);
                if(feature.data[key])
                    predioDetails += '<strong>' + key + ': ' + feature.data[key] + '</strong><br>'; 
            });
            
            predioDetails += '</div>';
            // info popup
            var popup = new OpenLayers.Popup.FramedCloud("PoputDetails",
                    feature.geometry.bounds.getCenterLonLat(),
                    new OpenLayers.Size(100, 100),
                    predioDetails,
                    null,
                    true // <-- true if we want a close (X) button, false otherwise
                    );
            popup.autoSize = true;
            map.addPopup(popup);
        };

        $(document).ready(function () {
            
            // Create Map
            var map = domain.objects.mapa();
            domain.objects.imap = map;

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

            var _gestion = $('#gestion');
            var _mes = $('#mes');
            var _cpasado = $('#cpasado');

            function loadFeatureIn(feature) {
                var geographic = new OpenLayers.Projection("EPSG:4326");
                var mercator = new OpenLayers.Projection("EPSG:900913");
                feature.geometry.transform(geographic, mercator);                     
                domain.objects.eventos.addFeatures([feature]);
            }
            
            function updatePuntos() {
                var s = _cpasado.val();
                var g = _gestion.val();
                var m = _mes.val();
                domain.objects.objectselected.removeAllFeatures();
                if(g !== 0 && m !== 0 && s !== 0) {
                    $.ajax({
                            url: '<c:url value="/datos/selected"/>',
                            type: "GET",
                            data: {gestion: g, mes: m, evento: s},
                            success: function (data) {
                                if (data.success) {
                                    // deploy
                                    data.data.forEach(function (item, index) {
                                        var f = domain.objects.featureFromText(item.geom, item);
                                        domain.objects.selectFeature(map, f, false);
                                    });
                                }
                            }
                        });
                }    
            }
            
            _mes.change(function () {
                updatePuntos();
                setvideciall(); 
            });
            _gestion.change(function () {
                updatePuntos();
                setvideciall(); 
            });
            _cpasado.change(function () {
                updatePuntos();
            });
            
            function setvideciall() {
                $.each($("input:checkbox[name='_data_master']:checked"), function(){
                    setvideci($(this).val(), false);
                    setvideci($(this).val(), true);
                });
            }    
            
            function setvideci(val, checked) {
                var g = _gestion.val();
                var m = _mes.val();
                var s = val;
                if(checked) {
                    if(g !== 0 && m !== 0) {
                        $.ajax({
                                url: '<c:url value="/datos/videci"/>',
                                type: "GET",
                                data: {gestion: g, mes: m, evento: s},
                                success: function (data) {
                                    if (data.success) {
                                        // console.log(data.data);
                                        var ids = new Array();
                                        data.data.forEach(function (item, index) {
                                            //var f = domain.objects.featureFromText(item.geom, {evento: item.evento});
                                            var f = domain.objects.featureFromText(item.geom, item);
                                            loadFeatureIn(f);
                                            //ids.push(item.idmanzana);
                                        });
                                        loadFeatureIn(ids);
                                    }
                                }
                            });
                    }    
                } else {
                    var af = new Array();
                    domain.objects.eventos.features.forEach(function (item, index) {
                        if (item.data.evento === s) {
                            af.push(item);
                        }
                    });
                    domain.objects.eventos.removeFeatures(af);
                }
            }    

            $('input:checkbox[name="_data_master"]').change(function () {
                var s = $(this).val();
                setvideci(s, $(this).prop("checked"));
            });
            
            $('input:checkbox[name="_t_riego"]').prop('checked', false);            
            $('input:checkbox[name="_t_riego"]').change(function () {
                var s = $(this).val();
                if($(this).prop("checked")) {
                    $.ajax({
                        url: '<c:url value="/datos/riego"/>',
                        type: "GET",
                        //data: {gestion: g, mes: m, evento: s},
                        success: function (data) {
                            if (data.success) {
                                // console.log(data.data);
                                var fets = new Array();
                                var geographic = new OpenLayers.Projection("EPSG:4326");
                                var mercator = new OpenLayers.Projection("EPSG:900913");                                                                                 
                                
                                data.data.forEach(function (item, index) {
                                    //var f = domain.objects.featureFromText(item.geom, {evento: item.evento});
                                    var f = domain.objects.featureFromText(item.geom, item);
                                    //loadFeatureIn(f);
                                    f.geometry.transform(geographic, mercator);    
                                    fets.push(f);
                                });
                                //loadFeatureIn(ids);
                                domain.objects.riego.addFeatures(fets);
                            }
                        }
                    });
                } else {
                    domain.objects.riego.removeAllFeatures();
                }
            });
                        
            var sidebar = $('#sidebar').sidebar();

        });

    </script>    
</html>



