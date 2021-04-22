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
        var feature = new OpenLayers.Feature.Vector(OpenLayers.Geometry.fromWKT(the_geom), attrs);        
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
    }),
    eventStyle: new OpenLayers.Style(null, {
            rules: [
                new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.EQUAL_TO,
                        property: "evento",
                        value: "Inundación"
                    }),
                    symbolizer: {
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillOpacity: 0.5,
                            fillColor: "#25d9dc",
                            strokeColor: "#25d9dc"
                        }
                    }
                }),
                new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.EQUAL_TO,
                        property: "evento",
                        value: "Desborde"
                    }),
                    symbolizer: {
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillOpacity: 0.5,
                            fillColor: "#56b056",
                            strokeColor: "#56b056"
                        }
                    }
                }),
                new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.EQUAL_TO,
                        property: "evento",
                        value: "Deslizamiento"
                    }),
                    symbolizer: {
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillOpacity: 0.5,
                            fillColor: "#f56d2d",
                            strokeColor: "#f56d2d"
                        }
                    }
                }),
                new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.EQUAL_TO,
                        property: "evento",
                        value: "Granizada"
                    }),
                    symbolizer: {
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillOpacity: 0.5,
                            fillColor: "#ec2df5",
                            strokeColor: "#ec2df5"
                        }
                    }
                }),
                new OpenLayers.Rule({
                    filter: new OpenLayers.Filter.Comparison({
                        type: OpenLayers.Filter.Comparison.EQUAL_TO,
                        property: "evento",
                        value: "Riada"
                    }),
                    symbolizer: {
                        "Polygon": {
                            strokeWidth: 2,
                            strokeOpacity: 1,
                            fillOpacity: 0.5,
                            fillColor: "#f2f52d",
                            strokeColor: "#f2f52d"
                        }
                    }
                })
            ]
        }) 
};

// Glove Info
domain.objects.popup = function (feature, map) {
    var predioDetails = '<div class="card-content">';    
    for (var key in feature.data) {
        if(key !== 'geom') {
           predioDetails += '<strong>' + key + ': ' + feature.data[key] + '</strong><br>';
        }   
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

// return a workable RGB int array [r,g,b] from hex representation
function processHEX(val) {
    //does the hex contain extra char?
    var hex = (val.length > 6) ? val.substr(1, val.length - 1) : val;
    // is it a six character hex?
    if (hex.length > 3) {
        //scrape out the numerics
        var r = hex.substr(0, 2);
        var g = hex.substr(2, 2);
        var b = hex.substr(4, 2);

        // if not six character hex,
        // then work as if its a three character hex
    } else {
        // just concat the pieces with themselves
        var r = hex.substr(0, 1) + hex.substr(0, 1);
        var g = hex.substr(1, 1) + hex.substr(1, 1);
        var b = hex.substr(2, 1) + hex.substr(2, 1);
    }
    // return our clean values
    return [
        parseInt(r, 16),
        parseInt(g, 16),
        parseInt(b, 16)
    ];
}

/**
 * custom 5 gradient in javascript
 * By @johngnu
 * @param {type} hexcolor
 * @returns {Array}
 */
function updateSpitter(hexcolor) {
    
    var val1RGB = processHEX(hexcolor);
    var val2RGB = processHEX("#FFFFFF");
    var colors = [
        // somewhere to dump gradient
    ];

    //the number of steps in the gradient
    var stepsInt = 5;
    //the percentage representation of the step
    var stepsPerc = 100 / (stepsInt + 1);

    // diffs between two values 
    var valClampRGB = [
        val2RGB[0] - val1RGB[0],
        val2RGB[1] - val1RGB[1],
        val2RGB[2] - val1RGB[2]
    ];

    // build the color array out with color steps
    for (var i = 0; i < stepsInt; i++) {
        var clampedR = (valClampRGB[0] > 0)
                ? pad((Math.round(valClampRGB[0] / 100 * (stepsPerc * (i + 1)))).toString(16), 2)
                : pad((Math.round((val1RGB[0] + (valClampRGB[0]) / 100 * (stepsPerc * (i + 1))))).toString(16), 2);

        var clampedG = (valClampRGB[1] > 0)
                ? pad((Math.round(valClampRGB[1] / 100 * (stepsPerc * (i + 1)))).toString(16), 2)
                : pad((Math.round((val1RGB[1] + (valClampRGB[1]) / 100 * (stepsPerc * (i + 1))))).toString(16), 2);

        var clampedB = (valClampRGB[2] > 0)
                ? pad((Math.round(valClampRGB[2] / 100 * (stepsPerc * (i + 1)))).toString(16), 2)
                : pad((Math.round((val1RGB[2] + (valClampRGB[2]) / 100 * (stepsPerc * (i + 1))))).toString(16), 2);
        colors[i] = [
            '#',
            clampedR,
            clampedG,
            clampedB
        ].join('');
    }
    
    return colors;
}

/**
 * padding function:
 * cba to roll my own, thanks Pointy!
 * ==================================
 * source: http://stackoverflow.com/questions/10073699/pad-a-number-with-leading-zeros-in-javascript
 * @param {type} n
 * @param {type} width
 * @param {type} z
 * @returns {String}
 */
function pad(n, width, z) {
    z = z || '0';
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
}