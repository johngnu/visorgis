/**
 * Principal GIS Server params config
 * Author: John Castillo Valencia @johngnu
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