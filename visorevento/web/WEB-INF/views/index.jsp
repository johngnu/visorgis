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
        <meta name="author" content="@johngnu">
        <link rel="icon" type="image/png" href="img/logo.png" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <title>Visor de Eventos</title>

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
                    <h5> <strong>Eventos Adversos IPP</strong></h5>

                    <div class="form-group">
                        <label>Gestión:</label>
                        <select id="gestion" class="form-control">
                            <option value="0">-- Seleccione --</option>
                            <option value="2019">2019</option>
                            <option value="2020">2020</option>
                            <option value="2021">2021</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Mes de la Gestión:</label>
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
                            <option>-- Seleccione --</option>
                            <option value="1">Desbordado</option>
                            <option value="2">Deslizamiento</option>
                            <option value="3">Granizada</option>
                            <option value="4">Inundación</option>
                            <option value="5">Riada</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                        </select>
                    </div>

                    <hr>                    
                    <h5> <strong>Eventos Adversos (Fuente VIDECI):</strong></h5>

                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="desborde_ene_20"> Desborde </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="deslizamiento_ene_20"> Deslizamiento </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="granizada_ene_20"> Granizada </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="inundacion_ene_20"> Inundación </label></div>
                    <div class="checkbox"><label><input type="checkbox" name="_data_master" value="riada_ene_20"> Riada </label></div>        

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
                    "Google Satélite",
                    {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
            );
            map.addLayer(ghyb);


            // server features
            /*
             this.datawfs = new OpenLayers.Layer.Vector("Server WFS Data", {
             strategies: [new OpenLayers.Strategy.BBOX()],
             protocol: new OpenLayers.Protocol.WFS({
             url: "http://sigedv2.ine.gob.bo/geoserver/wfs",
             featurePrefix:"censo",
             featureType: "t_ipp_eventos",  //"vw_ipp_eve_granizo_ene_20",                  
             srsName: "EPSG:4326",
             geometryName: "geom"                  
             }),
             displayInLayerSwitcher: true,
             visibility: true
             styleMap: domain.objects.styles
             });
             //map.addLayer(this.datawfs);
             */

            // Work layers
            /*
             this.canvas = new OpenLayers.Layer.Vector("canvas", {
             displayInLayerSwitcher: false,
             styleMap: domain.objects.styles
             });
             map.addLayer(this.canvas);
             domain.objects.canvas = this.canvas;
             */

            // Object selected layer
            this.objectselected = new OpenLayers.Layer.Vector("objectselected", {
                displayInLayerSwitcher: false,
                styleMap: domain.objects.styles  //style
            });

            map.addLayer(this.objectselected);

            // this.control = control;
            map.events.register("changebaselayer", this, function (obj) {
                // console.log(obj.layer.name);
            });

            //map.events.register("zoomend", map, zoomChanged);
            map.events.register("zoomend", map, function (obj) {
                // console.log(obj.layer.name);
                // domain.objects.objectselected.refresh({force:true});
                domain.objects.selectedInfo();
            });

            map.events.register("loadend", map, function (obj) {
                domain.objects.selectedInfo();
            });

            //Departamentos
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

            //Municipios
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

            if (!map.getCenter()) {
                map.zoomToMaxExtent();
            }
            map.zoomToExtent(Ext.geoBounds);

            return map;
        };
        // Selected Info
        domain.objects.selectedInfo = function () {
            domain.objects.objectselected.removeAllFeatures();
            var favorite = [];
            favorite.push(domain.objects.radio);
            var html = '';
            $.each($("input:checkbox[name='_data_master']:checked"), function () {
                favorite.push($(this).val());
            });
            $('#_check_option').html(html);

            /*
             $.each(domain.objects.datawfs.features, function() {
             var f = $(this)['0'];
             var sum = 0;
             favorite.forEach(function (item, index) {
             sum = sum + parseInt(f['data'][item]);                            
             });
             sum = Math.round(sum/favorite.length);
             
             // create some attributes for the feature
             var attributes = {name: "_result", datavalue: sum, municipio: f.data.municipio};
             
             var feature = new OpenLayers.Feature.Vector(f.geometry, attributes);                        
             domain.objects.objectselected.addFeatures(feature);
             });
             */
        };


        $(document).ready(function () {
            // N-Layers Array
            domain.objects.radio = $('select[name="_data_radio"]:selected').val();

            // Precipitación layers
            var geoserver_url = 'http://sigedv2.ine.gob.bo/geoserver/geonode/';
            var layers = new Array();

            // Primer grupo: Eventos IPP
            layers.push({key: 'desborde_ene_20', label: 'Desbordado', url: 'http://sigedv2.ine.gob.bo/geoserver/geonode/', layer: 'geonode:desborde_enero', searchField: 'NombreMuni'});
            layers.push({key: 'deslizamiento_ene_20', label: 'Deslizamiento', url: 'http://sigedv2.ine.gob.bo/geoserver/geonode/', layer: 'geonode:deslizamiento_enero', searchField: 'NombreMuni'});
            layers.push({key: 'granizada_ene_20', label: 'Granizada', url: 'http://sigedv2.ine.gob.bo/geoserver/geonode/', layer: 'geonode:granizada_enero', searchField: 'NombreMuni'});
            layers.push({key: 'inundacion_ene_20', label: 'Inundación', url: 'http://sigedv2.ine.gob.bo/geoserver/geonode/', layer: 'geonode:inundacion_enero', searchField: 'NombreMuni'});
            layers.push({key: 'riada_ene_20', label: 'Riada', url: 'http://sigedv2.ine.gob.bo/geoserver/geonode/', layer: 'geonode:riada_enero', searchField: 'NombreMuni'});

            // Segundo grupo: Eventos VIDECI
            layers.push({id: 'mapa_1', group: 'pasado', label: 'Desbordes', url: geoserver_url, layer: 'geonode:vw_ipp_eve_granizo_ene_20'});
            layers.push({id: 'mapa_2', group: 'pasado', label: 'Deslizamientos', url: geoserver_url, layer: 'geonode:vw_ipp_eve_inunda_ene_20'});
            layers.push({id: 'mapa_3', group: 'pasado', label: 'Granizadas', url: geoserver_url, layer: 'geonode:vw_ipp_eve_granizo_ene_20'});
            layers.push({id: 'mapa_4', group: 'pasado', label: 'Inundaciones', url: geoserver_url, layer: 'geonode:vw_ipp_eve_inunda_ene_20'});
            layers.push({id: 'mapa_5', group: 'pasado', label: 'Riadas', url: geoserver_url, layer: 'geonode:vw_ipp_eve_granizo_ene_20'});

            // Create Map
            var map = domain.objects.mapa({layers: layers});
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

            // set combos
            /*layers.forEach(function (item, index) {
                var opt = document.createElement('option');
                opt.value = item.id;
                opt.innerHTML = item.label;

                if (item.group === 'pasado') {
                    _cpasado.append(opt);
                }

                var nl = domain.objects.addDataLayer(item);
                map.addLayer(nl);
                layers[index].idLayer = nl.id;
            });*/

            _cpasado.change(function () {
                var s = $(this).val();
                var g = _gestion.val();
                var m = _mes.val();
                //selectExtraMap(s);
                //console.log(s);
                $.ajax({
                        url: '<c:url value="/datos/selected"/>',
                        type: "GET",
                        data: {gestion: g, mes: m, id_dato: s},
                        success: function (data) {
                            if (data.success) {
                                // console.log(data.data);
                                var ids = new Array();
                                data.data.forEach(function (item, index) {
                                    var f = domain.objects.featureFromText(item.geom);
                                    domain.objects.selectFeature(map, f, false);
                                    ids.push(item.idmanzana);
                                });
                            }
                        }
                    });
            });


            function selectExtraMap(s) {
                layers.forEach(function (item, index) {
                    var layer = map.getLayersBy('id', item.idLayer)[0];
                    if (item.id === s) {
                        layer.setVisibility(true);
                    } else {
                        layer.setVisibility(false);
                    }
                });
            }


            $('input:radio[name="_extra"]').change(function () {
                var s = $(this).val();
                selectExtraMap(s);
            });

            $('input:checkbox[name="_data_master"]').change(function () {
                //domain.objects.selectedInfo();
                var s = $(this).val();
                layers.forEach(function (item, index) {
                    var layer = map.getLayersBy('id', item.idLayer)[0];
                    if (item.key === s) {
                        layer.setVisibility(true);
                    } else {
                        layer.setVisibility(false);
                    }
                });

                //var layer = map.getLayersBy('id', s)[0];  
                //layer.setVisibility(true);
            });

            $('select[name="_data_radio"]').change(function () {
                domain.objects.radio = $(this).val();
                if (domain.objects.radio) {
                    domain.objects.selectedInfo();
                }
            });
        });

    </script>
</html>



