define([
    'dojo/_base/array',
    'dojo/_base/lang'
],
function(
    array,
    lang
) {
    var fieldNames = 'seq_id start end part_number component_type gap_length gap_type linkage linkage_evidence'.split(' ');

    return {

        parse_feature: function( pline ) {
            var line = pline.replace(/(\r\n|\n|\r)/gm, '');
            var f = array.map( line.split('\t'), function(a) {
                if ( a == '.' ) {
                    return null;
                }
                return a;
            });

            // unescape only the ref and source columns
            f[0] = this.unescape( f[0] );
            f[1] = this.unescape( f[1] );

            var parsed = {};
            for ( var i = 0; i < fieldNames.length; i++ ) {
                parsed[ fieldNames[i] ] = f[i] == '.' ? null : f[i];
            }
            if ( parsed.start !== null ) {
                parsed.start = parseInt( parsed.start, 10 );
            }
            if ( parsed.end !== null ) {
                parsed.end = parseInt( parsed.end, 10 );
            }
            if ( parsed.strand != null ) {
                parsed.strand = {'+': 1, '-': -1}[parsed.strand] || 0;
            }
            return parsed;
        },

        parse_directive: function( line ) {
            var match = /^\s*\#\#\s*(\S+)\s*(.*)/.exec( line );
            if ( !match )
                return null;
            var name = match[1], contents = match[2];

            var parsed = { directive: name };
            if ( contents.length ) {
                contents = contents.replace( /\r?\n$/, '' );
                parsed.value = contents;
            }

            return parsed;
        },

        unescape: function( s ) {
            if ( s === null )
                return null;

            return s.replace( /%([0-9A-Fa-f]{2})/g, function( match, seq ) {
                return String.fromCharCode( parseInt( seq, 16 ) );
            });
        },

        escape: function( s ) {
            return s.replace( /[\n\r\t;=%&,\x00-\x1f\x7f-\xff]/g, function( ch ) {
                var hex = ch.charCodeAt(0).toString(16).toUpperCase();
                if ( hex.length < 2 ) // lol, apparently there's no native function for fixed-width hex output
                    hex = '0' + hex;
                return '%' + hex;
            });
        },

        format_feature: function( f ) {
            var attrString =
              f.attributes === null || typeof f.attributes == 'undefined'
               ? '.' : this.format_attributes( f.attributes );

            var translate_strand = ['-', '.', '+'];
            var fields = [];
            for ( var i = 0; i < 8; i++ ) {
                var val = f[ fieldNames[i] ];
                if (i == 6) {
                    fields[i] = val === null || val === undefined ? '.' : translate_strand[val + 1];
                }
                else {
                    fields[i] = val === null || val === undefined ? '.' : this.escape( '' + val );
                }
            }
            fields[8] = attrString;

            return fields.join('\t') + '\n';
        },

        format_attributes: function( attrs ) {
            var attrOrder = [];
            for ( var tag in attrs ) {
                var val = attrs[tag];
                var valstring = val.hasOwnProperty( 'toString' )
                         ? this.escape( val.toString() ) :
                     lang.isArray(val.values)
                         ? function(val) {
                             return lang.isArray(val)
                                 ? array.map( val, this.escape ).join(',')
                                 : this.escape( val );
                         }.call(this, val.values) :
                     lang.isArray(val)
                         ? array.map( val, this.escape ).join(',')
                         : this.escape( val );
                attrOrder.push( this.escape( tag ) + '=' + valstring);
            }
            return attrOrder.length ? attrOrder.join(';') : '.';
        }
    };
});
