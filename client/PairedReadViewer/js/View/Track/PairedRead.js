define([
            'dojo/_base/declare',
            'dojo/_base/array',
            'dojo/_base/lang',
            'JBrowse/View/Track/CanvasFeatures',
            'JBrowse/Util'
       ],
       function (
            declare,
            array,
            lang,
            CanvasFeatures,
            Util
       ) {

return declare( CanvasFeatures, {

    _defaultConfig: function () {
        return Util.deepUpdate(
            lang.clone( this.inherited(arguments) ), {
                glyph: "PairedReadViewer/View/FeatureGlyph/PairedArc",
                maxFeatureScreenDensity: 60,
                chunkSizeLimit: 50000000,
                usePairedArc: true,
                showLabels: false
            }
        );
    },
    /*_trackMenuOptions: function () {
        var track = this;
        var options = this.inherited(arguments);
        options.pop();
        options.push({
            label: 'Enable PairedArc',
            type: 'dijit/CheckedMenuItem',
            checked: !!track.config.usePairedArc,
            onClick: function (event) {
                track.config.usePairedArc = this.get('checked');
                if ( track.config.usePairedArc ) {
                    track.config.glyph = "PairedEndViewer/PairedArc";
                } else {
                    track.config.glyph = "JBrowse/View/FeatureGlyph/Alignment";
                }
                track.browser.publish('/jbrowse/v1/v/tracks/replace', [track.config]);
            }
        });
        return options;
    },*/
    // override getLayout to access addRect method
    _getLayout: function () {
        var thisB = this;
        var browser = this.browser;
        var layout = this.inherited(arguments);
        var clabel = this.name + "-collapsed";
        return declare.safeMixin(layout, {
            addRect: function (id, left, right, height, data) {
                this.pTotalHeight = this.maxHeight;
                return 0;
            }
        });
    }

});
});

