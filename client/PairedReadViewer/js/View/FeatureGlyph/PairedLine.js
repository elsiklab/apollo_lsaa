define([
           'dojo/_base/declare',
           'dojo/_base/array',
           'dojo/_base/lang',
           'JBrowse/View/FeatureGlyph/Box'
       ],
       function (
           declare,
           array,
           lang,
           FeatureGlyph
       ) {

return declare( FeatureGlyph, {

    constructor: function () {

    },

    _defaultConfig: function () {
        return this._mergeConfigs(
            dojo.clone( this.inherited(arguments) ),
            {
                style: {
                    color: function (feature) { return feature.get('strand') == -1 ? 'blue' : 'red';},
                    color_fwd_strand_not_proper: '#ECC8C8',
                    color_rev_strand_not_proper: '#BEBED8',
                    color_fwd_strand: '#EC8B8B',
                    color_rev_strand: '#8F8FD8',
                    color_fwd_missing_mate: '#D11919',
                    color_rev_missing_mate: '#1919D1',
                    color_fwd_diff_chr: '#000000',
                    color_rev_diff_chr: '#969696',
                    color_nostrand: '#999999',
                    connectorColor: 'black',
                    connectorThickness: 1,
                    strandArrow: false,

                    height: 7,
                    marginBottom: 1,
                    showMismatches: true,
                    mismatchFont: 'bold 10px Courier New,monospace'
                },
                scaleFactor: 5,
            }
        );
    },

    renderConnector: function ( context, fRect ) {
        var n = fRect.f.get('next_segment_position');
        var chr = fRect.f.get('seq_id');
        var block = fRect.viewInfo.block;
        if (!n) {
            return;
        }
        var s = fRect.f.get('start');
        var e = fRect.f.get('end');
        var pos = parseInt(n.split(':')[1], 10);
        var chrm = n.split(':')[0];
        var style = lang.hitch(this, 'getStyle');

        if (chr != chrm) {
            return;
        }
        var connectorColor = style( fRect.f, 'connectorColor' );
        if (connectorColor) {
            context.fillStyle = connectorColor;
            var connectorThickness = style( fRect.f, 'connectorThickness' );
            if (pos > e) {
                context.fillRect(
                    fRect.rect.l + fRect.rect.w, // right
                    Math.round(fRect.t + (fRect.rect.h - connectorThickness) / 2), // top
                    (pos - e) * fRect.viewInfo.scale, // width
                    connectorThickness
                );
            } else {
                context.fillRect(
                    fRect.rect.l - (s - pos) * fRect.viewInfo.scale, // left
                    Math.round(fRect.t + (fRect.rect.h - connectorThickness) / 2), // top
                    (s - pos - fRect.f.get('length')) * fRect.viewInfo.scale,
                    connectorThickness
                );
            }
        }

    },



    renderFeature: function ( context, fRect ) {
        /*if (this.track.displayMode != 'collapsed') {
            context.clearRect( Math.floor(fRect.l), fRect.t, Math.ceil(fRect.w - Math.floor(fRect.l) + fRect.l), fRect.h );
        }*/

        this.renderBox( context, fRect.viewInfo, fRect.f, fRect.t, fRect.rect.h, fRect.f );
        this.renderConnector( context, fRect );
    },

    layoutFeature: function ( viewArgs, layout, feature ) {
        var rect = this.inherited( arguments );
        if (!rect) {
            return rect;
        }
        var n = feature.get('next_segment_position');
        var chr = feature.get('seq_id');
        if (!n) {
            return rect;
        }
        var s = feature.get('start');
        var pos = n.split(':')[1];
        var chrm = n.split(':')[0];
        if (chr != chrm) {
            return rect;
        }
        var pos = parseInt(n.split(':')[1], 10);
        var t = Math.abs(pos - s);

        // need to set the top of the inner rect
        rect.rect.t = t / this.config.scaleFactor;
        rect.t = t / this.config.scaleFactor;

        return rect;
    }
});
});

