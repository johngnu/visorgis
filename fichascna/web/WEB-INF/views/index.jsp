<%-- 
    Document   : amanzanado
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

        <title>Ficha CNPV</title>

        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
        <meta name="viewport" content="width=device-width" />

        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

        <link rel="stylesheet" href="css/module.css">
        <link rel="stylesheet" href="css/custom-modal.css">

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

            .modal-content-pre {
                /*background-color: #337ab721;*/
                background-color: #fff;
            }
        </style>
    </head>

    <body>
        <!--<button href="#" class="btn btn-primary topbutton" data-toggle="modal" data-target="#moreinfo">MÁS INFORMACIÓN</button>-->
        <div class="bcontainer">
            <div class="btn-group">                
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#moreinfo">Detalles</button>
                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                    Opciones <span class="caret"></span></button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" id="_draw_select">Seleccionar</a></li>
                    <!--<li role="separator" class="divider"></li>
                    <li><a href="#" id="_draw_polygon">Polígono</a></li>
                    <li><a href="#" id="_draw_point">Punto</a></li>
                    <li><a href="#" id="_draw_line">Línea</a></li>-->
                    <li role="separator" class="divider"></li>
                    <li><a href="#" id="_clear_all">Limpiar todo</a></li>
                </ul>                                
                <button type="button" class="btn btn-primary" id="_unselect">Limpiar</button>
            </div> 
        </div>    

        <div id="map"></div> 

        <div class="mcontainer">
            <div class="modal-content">
                <div class="modal-header">
                    <form id="search-form"> 
                        <div class="input-group">
                            <input name="txtSearch" type="text" class="form-control" placeholder="Buscar por ID..."/>
                            <div class="input-group-btn">
                                <button class="btn btn-primary" type="submit">
                                    <span class="glyphicon glyphicon-search"></span>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-body">
                    <h4>Capas <strong>Base</strong></h4>
                    <div id="_base_layers"></div>
                    <hr>
                    <h4> <strong>Capas</strong></h4>
                    <div id="_data_layers"></div>
                </div>
            </div>
        </div>

        <div class="modal right fade" id="moreinfo" tabindex="-1" role="dialog" aria-labelledby="moreinfo" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="myModalLabel">Detalles</h4>
                    </div>
                    <div class="modal-body">                    
                        <div id="docContent"></div>
                        <figure class="highcharts-figure">
                            <div id="container"></div>
                            <p class="highcharts-description">
                                POBLACIÓN EMPADRONADA POR SEXO, SEGÚN GRUPO DE EDAD
                            </p>
                        </figure>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>                        
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="notFoundDialog" tabindex="-1" role="dialog" aria-labelledby="notFoundDialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="myModalLabel">Aviso</h4>
                    </div>
                    <div class="modal-body">                        
                        <p class="text-muted">Ningún objeto se ha encontrado con este criterio.</p>                        
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Aceptar</button>                        
                    </div>
                </div>
            </div>
        </div> 
        
        <div class="modal fade" id="nFeaturesDialog" tabindex="-1" role="dialog" aria-labelledby="nFeaturesDialog" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="myModalLabel">Aviso</h4>
                    </div>
                    <div class="modal-body">                        
                        <p class="text-muted">Más de un objeto encontrado.</p>   
                        <div id="_nresults_"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Aceptar</button>                        
                    </div>
                </div>
            </div>
        </div> 
    </body>

    <!-- Core JS Files   -->
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <!-- GIS UI libraries 2015 --> 
    <script src="openlayers/OpenLayers.js"></script>
    <script src="openlayers/DynamicMeasure.js"></script>

    <!-- Google Maps API_KEY -->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdSJNaTQvzzlSZIAUsHEfCci2cAIRukP8" type="text/javascript"></script>
    <!-- Highcharts -->
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/highcharts-3d.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>
    <script src="https://code.highcharts.com/modules/export-data.js"></script>
    <script src="https://code.highcharts.com/modules/accessibility.js"></script>

    <!-- Custom page scrip -->
    <script src="js/setup.js"></script>

    <script type="text/javascript">
        // Glove Info
        domain.objects.popup = function (feature, map) {
            var predioDetails = '<div class="card-content">';
            predioDetails += '<strong>depto_estadistico: ' + feature.data.departamento + '</strong><br>';
            predioDetails += '<strong>prov_estadistico: ' + feature.data.provincia + '</strong><br>';
            predioDetails += '<strong>mun_estadistico: ' + feature.data.municipio + '</strong><br>';
            predioDetails += '<strong>nombreciudad: ' + feature.data.nombreciudad + '</strong><br>';
            predioDetails += '<strong>pob_empadronada: ' + feature.data.pob_edad_tot + '</strong><br>';
            predioDetails += '<strong>viviendas: ' + feature.data.viv_vivpart + '</strong><br>';
            predioDetails += '<strong>pob_60años_mas: ' + feature.data.pob_edad_60mas + '</strong><br>';
            predioDetails += '<strong>cod_ine: ' + feature.data.idmanzana + '</strong><br>';

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
        // Create Overlay
        domain.objects.addDataLayer = function (data) {
            // Create layer
            var ly = new OpenLayers.Layer.WMS(data.label, data.url + 'wms', {
                layers: data.layer,
                transparent: true,
                format: 'image/png'
            }, {
                buffer: 0,
                displayOutsideMaxExtent: true,
                isBaseLayer: false,
                visibility: false,
                singleTile: true,
                yx: {'EPSG:4326': true}
            });
            return ly;
        };
        // Draw controls acitivate
        domain.objects.switchControl = function (element) {
            for (var key in domain.objects.drawControls) {
                var control = domain.objects.drawControls[key];
                if (element === key) {
                    control.activate();
                } else {
                    control.deactivate();
                }
            }
        };
        // Contect Control
        domain.objects.newContextControl = function (layer, options) {
            if (domain.objects.control) {
                domain.objects.imap.removeControl(domain.objects.control);
                domain.objects.control.destroy();
            }
            // WMSGetFeatureInfo Control for Parcela
            var control = new OpenLayers.Control.WMSGetFeatureInfo({
                url: options.url + "wms",
                title: 'Identify features by clicking',
                autoActivate: true,
                infoFormat: "application/vnd.ogc.gml",
                maxFeatures: 1,
                layers: [layer]
            });

            control.events.on({
                getfeatureinfo: function (e) {
                    var feature = e.features[0];
                    if (feature) {
                        // Get IdFeature
                        var s = feature.fid;
                        s = s.substring(s.indexOf('.') + 1);
                        // Select Result Feature 
                        domain.objects.selectFeature(domain.objects.imap, feature, false);
                        // Open Popup Window
                        domain.objects.popup(feature, domain.objects.imap);
                    } else {
                        // No features found
                        console.log('notfound feature');
                    }
                }
            });

            domain.objects.imap.addControl(control);
            domain.objects.control = control;
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

            // Data Layers
            options.layers.forEach(function (item, index) {
                if (index === 0) {
                    domain.objects.activeSLayer = item;
                }
                var nl = domain.objects.addDataLayer(item);
                map.addLayer(nl);
                options.layers[index].idLayer = nl.id;
            });

            map.addControl(new OpenLayers.Control.MousePosition());

            // Work layers
            this.canvas = new OpenLayers.Layer.Vector("canvas", {
                displayInLayerSwitcher: false//,
                        //styleMap: domain.objects.styles
            });
            map.addLayer(this.canvas);
            domain.objects.canvas = this.canvas;

            this.dynamicMeasure = new OpenLayers.Control.DynamicMeasure(OpenLayers.Handler.Polygon, {
                persist: true,
                drawingLayer: this.canvas,
                geodesic: true,
                maxSegments: null,
                keep: true
            });
            this.canvas.events.on({
                featureadded: function (e) {
                    var feature = e.feature;
                    feature.geometry.transform(
                            new OpenLayers.Projection("EPSG:900913"),
                            new OpenLayers.Projection("EPSG:4326")
                            );
                    $.ajax({
                        url: domain.objects.activeSLayer.endPoint + '/ficha/selected',
                        type: "GET",
                        data: {geom: feature.geometry.toString()},
                        success: function (data) {
                            if (data.success) {
                                // console.log(data.data);
                                var ids = new Array();
                                data.data.forEach(function (item, index) {
                                    var f = domain.objects.featureFromText(item.geom);
                                    domain.objects.selectFeature(map, f, false);
                                    ids.push(item[domain.objects.activeSLayer.searchField]);
                                });

                                $('#moreinfo').modal('toggle');
                                $.ajax({
                                    url: domain.objects.activeSLayer.endPoint + '/ficha/data',
                                    type: "POST",
                                    data: {ids: JSON.stringify(ids)},
                                    dataType: 'json',
                                    success: function (data) {

                                        var datas = data.data;
                                        var serie_data = [
                                            datas.pob_edad_0003,
                                            datas.pob_edad_0405,
                                            datas.pob_edad_0619,
                                            datas.pob_edad_2039,
                                            datas.pob_edad_4059,
                                            datas.pob_edad_60mas
                                        ];

                                        var time = new Date().getTime();
                                        $("#docContent").html('<embed src="'+ domain.objects.activeSLayer.endPoint +'/ficha/pdf?time=' + time + '" width="100%" height="200"><br/><a class="btn btn-primary btn-sm" href="'+ domain.objects.activeSLayer.endPoint + '/ficha/pdf?download=true">Descargar</a>')
                                        Highcharts.chart('container', {
                                            chart: {
                                                type: 'column',
                                                options3d: {
                                                    enabled: true,
                                                    alpha: 10,
                                                    beta: 25,
                                                    depth: 70
                                                }
                                            },
                                            title: {
                                                text: 'Resumen'
                                            },
                                            subtitle: {
                                                text: 'POBLACIÓN EMPADRONADA POR SEXO, SEGÚN GRUPO DE EDAD'
                                            },
                                            plotOptions: {
                                                column: {
                                                    depth: 25
                                                }
                                            },
                                            xAxis: {
                                                categories: ["0-3", "4-5", "6-19", "20-39", "40-59", "60 y más"],
                                                labels: {
                                                    skew3d: true,
                                                    style: {
                                                        fontSize: '16px'
                                                    }
                                                }
                                            },
                                            yAxis: {
                                                title: {
                                                    text: null
                                                }
                                            },
                                            series: [{
                                                    name: 'Edad',
                                                    data: serie_data
                                                }]
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            });
            // Draw control
            var drawControls = {
                point: new OpenLayers.Control.DrawFeature(this.canvas,
                        OpenLayers.Handler.Point),
                line: new OpenLayers.Control.DrawFeature(this.canvas,
                        OpenLayers.Handler.Path),
                polygon: new OpenLayers.Control.DrawFeature(this.canvas,
                        OpenLayers.Handler.Polygon),
                select: this.dynamicMeasure
            };

            for (var key in drawControls) {
                map.addControl(drawControls[key]);
            }
            domain.objects.drawControls = drawControls;

            // Object selected layer
            this.objectselected = new OpenLayers.Layer.Vector("objectselected", {
                displayInLayerSwitcher: false
                        //styleMap: domain.objects.styles
            });
            map.addLayer(this.objectselected);

            // this.control = control;
            map.events.register("changebaselayer", this, function (obj) {
                // console.log(obj.layer.name);
            });

            if (!map.getCenter()) {
                map.zoomToMaxExtent();
            }
            map.zoomToExtent(Ext.geoBounds);
            return map;
        };
        
        domain.objects.focus = function (fid) {
            var f = domain.objects.objectselected.getFeatureById(fid);
            domain.objects.imap.zoomToExtent(f.geometry.getBounds());
        };

        $(document).ready(function () {
            // N-Layers Array
            var layers = new Array();
            layers.push({label: 'Manzanas', url: 'http://sigedv2.ine.gob.bo/geoserver/siged/', layer: 'siged:v_fichamanzana', searchField: 'idmanzana', endPoint: '<c:url value="/amanzanado"/>'});
            layers.push({label: 'Disperso', url: 'http://sigedv2.ine.gob.bo/geoserver/siged/', layer: 'siged:v_fichadisperso', searchField: 'idcomunidad', endPoint: '<c:url value="/disperso"/>'});

            // Create Map
            var map = domain.objects.mapa({layers: layers});
            domain.objects.imap = map;

            var controls = map.getControlsByClass('OpenLayers.Control.LayerSwitcher');

            if (controls.length === 1) {
                var swcontrol = controls[0];
                var shtml = '';
                swcontrol.baseLayers.forEach(function (item, index) {
                    if (index === 0) {
                        shtml += '<div class="radio"><label><input checked type="radio" name="_base_layers" value="' + item.layer.id + '">' + item.layer.name + '</label></div>';
                    } else {
                        shtml += '<div class="radio"><label><input type="radio" name="_base_layers" value="' + item.layer.id + '">' + item.layer.name + '</label></div>';
                    }
                });
                $('#_base_layers').html(shtml);
                $('input:radio[name="_base_layers"]').change(function () {
                    var layer = map.getLayersBy('id', $(this).val())[0];
                    map.setBaseLayer(layer);
                });

                var bhtml = '';
                swcontrol.dataLayers.forEach(function (item, index) {
                    if (index === 0) {
                        bhtml += '<div class="radio"><label><input checked type="radio" name="_data_layers" value="' + item.layer.id + '">' + item.layer.name + '</label></div>';
                        item.layer.setVisibility(true);
                        domain.objects.newContextControl(item.layer, domain.objects.activeSLayer);
                    } else {
                        bhtml += '<div class="radio"><label><input type="radio" name="_data_layers" value="' + item.layer.id + '">' + item.layer.name + '</label></div>';
                    }
                });
                $('#_data_layers').html(bhtml);
                $('input:radio[name="_data_layers"]').change(function () {
                    domain.objects.objectselected.removeAllFeatures();
                    var lid = $(this).val();
                    swcontrol.dataLayers.forEach(function (item, index) {
                        if (item.layer.id === lid) {
                            item.layer.setVisibility(true);
                            layers.forEach(function (slayer, index) {
                                if (slayer.idLayer === lid) {
                                    domain.objects.newContextControl(item.layer, slayer);
                                    domain.objects.activeSLayer = slayer;
                                }
                            });
                        } else {
                            item.layer.setVisibility(false);
                        }
                    });
                });

                $('#search-form').submit(function () {
                    var search = $(this).find('input[name="txtSearch"]').val();
                    //console.log(domain.objects.activeSLayer);
                    if(domain.objects.activeSLayer.label === 'Manzanas') {
                        $.ajax({
                            method: 'POST',
                            url: Ext.localProxy + 'http://sigedv2.ine.gob.bo:80/geoserver/siged/wfs',
                            data: {
                                "service": "WFS",
                                "request": "GetFeature",
                                "typename": 'siged:v_perimetros',
                                "outputFormat": "application/json",
                                "srsname": "EPSG:4326",
                                "maxFeatures": 50,
                                "CQL_FILTER": "strToLowerCase(nombreciudad) like '%" + search.toLowerCase() + "%'"
                            },
                            success: function (response, status, xhr) {
                                if (xhr.getResponseHeader('Content-Type') === 'application/json') {
                                    var geojson_format = new OpenLayers.Format.GeoJSON();
                                    var features = geojson_format.read(response, "FeatureCollection");
                                    if (features.length > 0) {
                                        //var feature = features[0];
                                        //domain.objects.selectFeature(map, feature, true);
                                        //domain.objects.popup(feature, map);
                                        domain.objects.objectselected.removeAllFeatures();
                                        features.forEach(function (feature, index) {
                                            domain.objects.selectFeature(map, feature, false);                                       
                                        });
                                        map.zoomToExtent(domain.objects.objectselected.getDataExtent());
                                        if(features.length > 1) {
                                            $('#nFeaturesDialog').modal('toggle');
                                            var html = '<table class="table table-bordered"><thead><tr><th>Cod. Municipio</th><th>Nombre ciudad</th><th>Opción</th></tr></thead><tbody>';
                                            domain.objects.objectselected.features.forEach(function (feature, index) {
                                                // console.log(feature);
                                                html = html + '<tr><td>' + feature.data.codigomunicipio + '</td>';
                                                html = html + '<td>' + feature.data.nombreciudad + '</td>';
                                                html = html + '<td><button class="btn focusf" value="'+feature.id+'">ver</button></td></tr>';
                                            });    
                                            html = html + '</tbody></table>';
                                            $('#_nresults_').html(html);
                                            $('.focusf').on('click', function () {                                            
                                                domain.objects.focus($(this).val());
                                                $('#nFeaturesDialog').modal('toggle');
                                            });
                                        }
                                    } else {
                                        $('#notFoundDialog').modal('toggle');                                    
                                    }
                                } else {
                                    $('#notFoundDialog').modal('toggle');
                                }
                            },
                            fail: function (jqXHR, textStatus) {
                                console.log("Request failed: " + textStatus);
                            }
                        });
                    }
                    if(domain.objects.activeSLayer.label === 'Disperso') {
                        $.ajax({
                            method: 'POST',
                            url: Ext.localProxy + 'http://sigedv2.ine.gob.bo:80/geoserver/siged/wfs',
                            data: {
                                "service": "WFS",
                                "request": "GetFeature",
                                "typename": 'siged:v_fichadisperso',
                                "outputFormat": "application/json",
                                "srsname": "EPSG:4326",
                                "maxFeatures": 50,
                                "CQL_FILTER": "strToLowerCase(nombreciudad) like '%" + search.toLowerCase() + "%'"
                            },
                            success: function (response, status, xhr) {
                                if (xhr.getResponseHeader('Content-Type') === 'application/json') {
                                    var geojson_format = new OpenLayers.Format.GeoJSON();
                                    var features = geojson_format.read(response, "FeatureCollection");
                                    if (features.length > 0) {
                                        //var feature = features[0];
                                        //domain.objects.selectFeature(map, feature, true);
                                        //domain.objects.popup(feature, map);
                                        domain.objects.objectselected.removeAllFeatures();
                                        features.forEach(function (feature, index) {
                                            domain.objects.selectFeature(map, feature, false);                                       
                                        });
                                        map.zoomToExtent(domain.objects.objectselected.getDataExtent());
                                        if(features.length > 1) {
                                            $('#nFeaturesDialog').modal('toggle');
                                            var html = '<table class="table table-bordered"><thead><tr><th>Municipio</th><th>Nombre ciudad</th><th>Opción</th></tr></thead><tbody>';
                                            domain.objects.objectselected.features.forEach(function (feature, index) {
                                                // console.log(feature);
                                                html = html + '<tr><td>' + feature.data.municipio + '</td>';
                                                html = html + '<td>' + feature.data.nombreciudad + '</td>';
                                                html = html + '<td><button class="btn focusf" value="'+feature.id+'">ver</button></td></tr>';
                                            });    
                                            html = html + '</tbody></table>';
                                            $('#_nresults_').html(html);
                                            $('.focusf').on('click', function () {                                            
                                                //domain.objects.focus($(this).val());
                                                $('#nFeaturesDialog').modal('toggle');
                                            });
                                        }
                                    } else {
                                        $('#notFoundDialog').modal('toggle');                                    
                                    }
                                } else {
                                    $('#notFoundDialog').modal('toggle');
                                }
                            },
                            fail: function (jqXHR, textStatus) {
                                console.log("Request failed: " + textStatus);
                            }
                        });
                    }    
                    return false;
                });
            }

            // reset all
            $('#_clear_all').on('click', function () {
                domain.objects.canvas.removeAllFeatures();
                domain.objects.switchControl('clear');
                domain.objects.dynamicMeasure.emptyKeeped();
            });

            // Draw buttons
            $('#_draw_polygon').on('click', function () {
                domain.objects.switchControl('polygon');
            });
            $('#_draw_point').on('click', function () {
                domain.objects.switchControl('point');
            });
            $('#_draw_line').on('click', function () {
                domain.objects.switchControl('line');
            });
            $('#_unselect').on('click', function () {
                domain.objects.objectselected.removeAllFeatures();
                //
                domain.objects.canvas.removeAllFeatures();
                domain.objects.switchControl('clear');
                domain.objects.dynamicMeasure.emptyKeeped();
            });
            $('#_draw_select').click(function () {
                domain.objects.switchControl('select');
            });
            
        });
    </script>
</html>


