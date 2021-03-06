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

        <title>Visor SIN</title>

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

        <img class="inelogo" src="img/logo-ine.png" alt=""/>        


        <div class="legend">
            <img id="legend_image" src="img/leyenda.png"/>
        </div> 


        <div id="map"></div>  

        <div class="mcontainer">
            <div class="modal-content">

                <div class="modal-body">
                    <h5>Capas <strong>Base</strong></h5>
                    <select name="_base_layers" class="form-control"></select>

                    <hr>                    
                    <h5> <strong>_DATA_</strong></h5>
                    <!--
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

                    <div class="form-group">
                        <label>Evento Adverso:</label>
                        <select id="cpasado" class="form-control">
                            <option value="0">-- Seleccione --</option>
                            <option value="c1_graniz">Granizo</option>
                            <option value="c2_inunda">Inundaci�n</option>
                            <option value="c3_contam">Contaminaci�n</option>
                            <option value="c4_sequia">Sequ�a</option>
                            <option value="c5_helada">Helada</option>
                        </select>
                    </div>

                    <hr>                    
                    <h5> <strong>Eventos Adversos (Fuente VIDECI):</strong></h5>

                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Desborde"> Desborde </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Deslizamiento"> Deslizamiento </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Granizada"> Granizada </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Inundaci�n"> Inundaci�n </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="Riada"> Riada </label></div>        
                    -->
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
                displayInLayerSwitcher: true,
                styleMap: domain.objects.styles,
                eventListeners: layerListeners
            });

            map.addLayer(this.objectselected);

            var selectControl = new OpenLayers.Control.SelectFeature (
                [this.objectselected, this.eventos], {
                    clickout: true, toggle: false,
                    multiple: false, hover: false,
                    
                    //toggleKey: "ctrlKey", // ctrl key removes from selection
                    //multipleKey: "shiftKey" // shift key adds to selection
                    //onSelect: function (e) { ... process feature hover ...  }, 
                    clickFeature: function (e) {
                        if(e.layer.name === 'objectselected') {
                            domain.objects.popupPuntos(e, map); 
                        } else {
                            domain.objects.popup(e, map); 
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
                                            var f = domain.objects.featureFromText(item.geom, {evento: item.evento});
                                            loadFeatureIn(f);
                                            //ids.push(item.idmanzana);
                                        });
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
            
            $.ajax({
                url: '<c:url value="/geodata"/>',
                type: "GET",
                data: {view: 'cartografia.vw_demo_dep_act', field: 'graco'},
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
            
        });

    </script>
</html>



