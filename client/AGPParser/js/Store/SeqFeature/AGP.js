define( [
    'dojo/_base/declare',
    'dojo/_base/lang',
    'dojo/_base/array',
    'dojo/Deferred',
    'JBrowse/Model/SimpleFeature',
    'JBrowse/Store/SeqFeature',
    'JBrowse/Store/DeferredFeaturesMixin',
    'JBrowse/Store/DeferredStatsMixin',
    'JBrowse/Store/SeqFeature/GlobalStatsEstimationMixin',
    'JBrowse/Model/XHRBlob',
    './AGP/Parser'
],
function(
    declare,
    lang,
    array,
    Deferred,
    SimpleFeature,
    SeqFeatureStore,
    DeferredFeatures,
    DeferredStats,
    GlobalStatsEstimationMixin,
    XHRBlob,
    Parser
) {

    return declare([ SeqFeatureStore, DeferredFeatures, DeferredStats, GlobalStatsEstimationMixin ], {
        constructor: function( args ) {
            this.data = args.blob ||
            new XHRBlob( this.resolveUrl(this._evalConf(args.urlTemplate)));
            this.features = [];
            this._loadFeatures();
        },

        _loadFeatures: function() {
            var thisB = this;
            var features = this.bareFeatures = [];

            var featuresSorted = true;
            var seenRefs = this.refSeqs = {};
            var parser = new Parser({
                featureCallback: function(fs) {
                    array.forEach( fs, function( feature ) {
                        var prevFeature = features[ features.length - 1 ];
                        var regRefName = thisB.browser.regularizeReferenceName( feature.seq_id );
                        if ( regRefName in seenRefs && prevFeature && prevFeature.seq_id != feature.seq_id )
                            featuresSorted = false;
                        if ( prevFeature && prevFeature.seq_id == feature.seq_id && feature.start < prevFeature.start )
                            featuresSorted = false;

                        if ( !( regRefName in seenRefs ))
                            seenRefs[ regRefName ] = features.length;

                        features.push( feature );
                    });
                },
                endCallback: function()  {
                    if ( !featuresSorted ) {
                        features.sort( thisB._compareFeatureData );
                        thisB._rebuildRefSeqs( features );
                    }

                    thisB._estimateGlobalStats()
                         .then( function( stats ) {
                             thisB.globalStats = stats;
                             thisB._deferred.stats.resolve();
                         });

                    thisB._deferred.features.resolve( features );
                }
            });
            var fail = lang.hitch( this, '_failAllDeferred' );
            this.data.fetchLines(
                function( line ) {
                    try {
                        parser.addLine(line);
                    } catch (e) {
                        fail('Error parsing AGP.');
                        throw e;
                    }
                },
                lang.hitch( parser, 'finish' ),
                fail
            );
        },

        _rebuildRefSeqs: function( features ) {
            var refs = {};
            for ( var i = 0; i < features.length; i++ ) {
                var regRefName = this.browser.regularizeReferenceName( features[i].seq_id );

                if ( !( regRefName in refs ) )
                    refs[regRefName] = i;
            }
            this.refSeqs = refs;
        },

        _compareFeatureData: function( a, b ) {
            if ( a.seq_id < b.seq_id )
                return -1;
            else if ( a.seq_id > b.seq_id )
                return 1;

            return a.start - b.start;
        },

        _getFeatures: function( query, featureCallback, finishedCallback, errorCallback ) {
            var thisB = this;
            thisB._deferred.features.then( function() {
                thisB._search( query, featureCallback, finishedCallback, errorCallback );
            });
        },

        _search: function( query, featureCallback, finishCallback, errorCallback ) {
            var bare = this.bareFeatures;
            var converted = this.features;

            var refName = this.browser.regularizeReferenceName( query.ref );

            var i = this.refSeqs[ refName ];
            if ( !( i >= 0 )) {
                finishCallback();
                return;
            }

            var checkEnd = 'start' in query
                ? function(f) { return f.get('end') >= query.start; }
                : function() { return true; };

            for ( ; i < bare.length; i++ ) {
                var f = converted[i] ||
                ( converted[i] = function(b, i) {
                    bare[i] = false;
                    return this._formatFeature( b );
                }.call( this, bare[i], i )
                );
                if ( f._reg_seq_id != refName || f.get('start') > query.end )
                    break;

                if ( checkEnd( f ) ) {
                    featureCallback( f );
                }
            }

            finishCallback();
        },

        _formatFeature: function( data ) {
            var f = new SimpleFeature({
                data: this._featureData( data ),
                id: (data.ID || [])[0]
            });
            f._reg_seq_id = this.browser.regularizeReferenceName( data.seq_id );
            return f;
        },

        _featureData: function( data ) {
            var f = lang.mixin( {}, data );
            f.start -= 1; // convert to interbase
            return f;
        },

        hasRefSeq: function( seqName, callback, errorCallback ) {
            var thisB = this;
            this._deferred.features.then( function() {
                callback( thisB.browser.regularizeReferenceName( seqName ) in thisB.refSeqs );
            });
        },

        saveStore: function() {
            return {
                urlTemplate: this.config.blob.url
            };
        }
    });
});
