define([
            'dojo/_base/declare',
            'dojo/dom-construct',
            'dijit/focus',
            'dijit/form/TextBox',
            'dijit/form/SimpleTextarea',
            'dojo/on',
            'dojo/request',
            'dijit/form/Button',
            'JBrowse/View/Dialog/WithActionBar',
            'JBrowse/Model/Location'
       ],
       function (
            declare,
            dom,
            focus,
            TextBox,
            TextArea,
            on,
            request,
            Button,
            ActionBarDialog,
            Location
       ) {


return declare( ActionBarDialog,
{
    autofocus: false,
    title: 'Create alternative loci',

    constructor: function (args) {
        this.browser = args.browser;
        this.setCallback    = args.setCallback || function () {};
        this.cancelCallback = args.cancelCallback || function () {};
        this.contextPath = args.contextPath || "..";
    },

    _fillActionBar: function (actionBar) {
        var thisB = this;
        new Button({
            label: 'Cancel',
            onClick: function () {
                thisB.cancelCallback && thisB.cancelCallback();
                thisB.hide();
            }
        }).placeAt( actionBar );
        new Button({
            label: 'OK',
            onClick: function () {
                request( thisB.contextPath + '/alternativeLoci/addLoci', {
                    data: {
                        start: thisB.start.get('value'),
                        end: thisB.end.get('value'),
                        sequence: thisB.sequence.get('value'),
                        description: thisB.description.get('value'),
                        sequencedata: thisB.sequencedata.get('value')
                    },
                    handleAs: "json",
                    method: "post"
                }).then(function (response) {
                    thisB.hide();
                    thisB.browser.clearHighlight();
                    thisB.browser.view.redrawRegion(
                        new Location(thisB.sequence.get('value') + ":" + thisB.start.get('value') + ".." + thisB.end.get('value'))
                    );
                }, function (error) { alert(error) });
            }
        }).placeAt( actionBar );

        new Button({
            iconClass: 'dijitIconFilter',
            label: 'Get coordinates from highlighted region',
            onClick: function () {
                var highlight = thisB.browser.getHighlight();
                if (highlight) {
                    thisB.start.set("value", highlight.start);
                    thisB.end.set("value", highlight.end);
                    thisB.sequence.set("value", highlight.ref);
                } else {
                    alert('No highlight set');
                }
            }
        }).placeAt( actionBar );
    },

    show: function ( callback ) {
        var thisB = this;

        dojo.addClass( this.domNode, 'setLSAA' );

        this.sequence = new TextBox({id: 'lsaa_name'});
        this.start = new TextBox({id: 'lsaa_start'});
        this.end = new TextBox({id: 'lsaa_end'});
        this.description = new TextArea({id: 'description',style: "height: 60px;"});
        this.sequencedata = new TextArea({id: 'sequencedata',style: "height: 60px;"});
        var br = function () { return dom.create('br') };

        this.set('content', [
                     dom.create('p', { innerHTML: 'Provide coordinate, details, and optional alternate sequences. See the <a href="http://genomes.missouri.edu/help.html">Help Guide</a> for additional information.' } ), br(),
                     dom.create('label', { "for": 'lsaa_name', innerHTML: 'Reference sequence: ' } ), this.sequence.domNode, br(),
                     dom.create('label', { "for": 'lsaa_start', innerHTML: 'Start: ' } ), this.start.domNode, br(),
                     dom.create('label', { "for": 'lsaa_end', innerHTML: 'End: ' } ), this.end.domNode, br(),
                     dom.create('label', { "for": 'description', innerHTML: 'Description: ' } ), this.description.domNode, br(),
                     dom.create('label', { "for": 'description', innerHTML: 'Sequence data (optional): ' } ), this.sequencedata.domNode, br()
                 ] );



        this.inherited( arguments );
    },

    hide: function () {
        this.inherited(arguments);
        window.setTimeout( dojo.hitch( this, 'destroyRecursive' ), 500 );
    }
});
});
