/**
 * Principal GIS Server params config
 */
Ext = new Object();
Ext.geoProjection = "EPSG:4326";
Ext.geoBounds = new OpenLayers.Bounds(
        -8128496.9907945, -2675785.1418022,
        -5675174.1312949, -1208194.1989312
        );
Ext.geoOptions = {
    maxExtent: Ext.geoBounds,
    maxResolution: 4.777314267158508,
    displayProjection: new OpenLayers.Projection("EPSG:4326"),
    units: 'm'
};
// web proxy for GetFeatureInfo
Ext.localProxy = '/http_proxy/proxy?url=';
var domain = new Object();
domain.objects = {
    proxy: Ext.localProxy,
    geoserverUrl: Ext.geoserverUrl,
    options: Ext.geoOptions,
    projection: Ext.geoProjection,
    featureFromText: function (the_geom, attrs) {
        var feature = new OpenLayers.Feature.Vector(OpenLayers.Geometry.fromWKT(the_geom));
        return feature;
    },
    selectFeature: function (map, feature, zoomto) {
        var geographic = new OpenLayers.Projection("EPSG:4326");
        var mercator = new OpenLayers.Projection("EPSG:900913");
        feature.geometry.transform(geographic, mercator);
        // this.objectselected.removeAllFeatures();
        this.objectselected.addFeatures([feature]);
        if (zoomto) {
            map.zoomToExtent(feature.geometry.getBounds());
        }
    },
    styles: new OpenLayers.StyleMap({
        "default": new OpenLayers.Style(null, {
            rules: [
                new OpenLayers.Rule({
                    symbolizer: {
                        "Point": {
                            pointRadius: 7,
                            fillColor: "#ffffff",
                            fillOpacity: 0.25,
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            strokeColor: "#ababab"
                        },
                        "Line": {
                            strokeWidth: 3,
                            strokeOpacity: 0.7,
                            strokeColor: "#ababab"
                        },
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillOpacity: 0.25,
                            fillColor: "#ffffff",
                            strokeColor: "#ababab"
                        }
                    }
                })
            ]
        }),
        "select": new OpenLayers.Style(null, {
            rules: [
                new OpenLayers.Rule({
                    symbolizer: {
                        "Point": {
                            pointRadius: 5,
                            graphicName: "square",
                            fillColor: "red",
                            fillOpacity: 0.25,
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            strokeColor: "#0000ff"
                        },
                        "Line": {
                            strokeWidth: 3,
                            strokeOpacity: 1,
                            strokeColor: "#0000ff"
                        },
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillColor: "#0000ff",
                            strokeColor: "#0000ff"
                        }
                    }
                })
            ]
        }),
        "temporary": new OpenLayers.Style(null, {
            rules: [
                new OpenLayers.Rule({
                    symbolizer: {
                        "Point": {
                            //graphicName: "square",
                            pointRadius: 5,
                            fillColor: "#ff0000",
                            fillOpacity: 0.25,
                            strokeWidth: 2,
                            strokeColor: "#ff0000"
                        },
                        "Line": {
                            strokeWidth: 3,
                            strokeOpacity: 1,
                            strokeColor: "#ff0000"
                        },
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            strokeColor: "#ff0000",
                            fillColor: "#ff0000"
                        }
                    }
                })
            ]
        })
    })
};

// Glove Info
domain.objects.popup = function (feature, map) {
    var predioDetails = '<div class="card-content">';
    for (var key in feature.data) {
        predioDetails += '<strong>' + key + ': ' + feature.data[key] + '</strong><br>';
    }

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

domain.objects.addDataLayer = function (data) {
    // Create layer
    var ly = new OpenLayers.Layer.WMS(data.label, data.url + 'wms', {
        layers: data.layer,
        transparent: true,
        format: 'image/png'
    }, {
        displayInLayerSwitcher: true,
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

                domain.objects.selectedInfo();
            } else {
                // No features found
                console.log('notfound feature');
            }
        }
    });

    domain.objects.imap.addControl(control);
    domain.objects.control = control;
};