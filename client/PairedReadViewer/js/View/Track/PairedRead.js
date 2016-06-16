define([
    'dojo/_base/declare',
    'dojo/_base/array',
    'dojo/_base/lang',
    'JBrowse/View/Track/CanvasFeatures',
    'JBrowse/Util'
],
function(
    declare,
    array,
    lang,
    CanvasFeatures,
    Util
) {
    return declare(CanvasFeatures, {
        _defaultConfig: function() {
            return Util.deepUpdate(lang.clone(this.inherited(arguments)), {
                glyph: 'PairedReadViewer/View/FeatureGlyph/PairedArc',
                maxFeatureScreenDensity: 60,
                chunkSizeLimit: 50000000,
                usePairedArc: true,
                showLabels: false
            });
        },

        // override getLayout to access addRect method
        _getLayout: function() {
            var layout = this.inherited(arguments);
            return declare.safeMixin(layout, {
                addRect: function(/* id, left, right, height, feature */) {
                    this.pTotalHeight = this.maxHeight;
                    return 0;
                }
            });
        }
    });
});

