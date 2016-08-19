define([
    'dojo/_base/declare',
    'dojo/dom-construct',
    'dijit/focus',
    'dijit/form/TextBox',
    'dojo/on',
    'dojo/request',
    'dijit/form/Button',
    'JBrowse/View/Dialog/WithActionBar',
    'JBrowse/Model/Location'
],
function(
    declare,
    dom,
    focus,
    TextBox,
    on,
    request,
    Button,
    ActionBarDialog,
    Location
) {
    return declare(ActionBarDialog, {
        autofocus: false,
        title: 'Create alternative loci',

        constructor: function(args) {
            this.browser = args.browser;
            this.setCallback    = args.setCallback || function() {};
            this.cancelCallback = args.cancelCallback || function() {};
            this.contextPath = args.contextPath || '..';
        },

        _fillActionBar: function(actionBar) {
            var thisB = this;
            new Button({
                label: 'Cancel',
                onClick: function() {
                    if (thisB.cancelCallback) {
                        thisB.cancelCallback();
                    }
                    thisB.hide();
                }
            }).placeAt(actionBar);
            new Button({
                label: 'OK',
                onClick: function() {
                    request(thisB.contextPath + '/editScaffolds/createReversal', {
                        data: {
                            start: thisB.start.get('value'),
                            end: thisB.end.get('value'),
                            sequence: thisB.sequence.get('value'),
                            description: thisB.description.get('value'),
                            sequencedata: thisB.sequencedata.get('value'),
                            organism: thisB.browser.config.dataset_id
                        },
                        handleAs: 'json',
                        method: 'post'
                    }).then(function() {
                        thisB.hide();
                        thisB.browser.clearHighlight();
                        thisB.browser.view.redrawRegion(new Location(thisB.sequence.get('value') + ':' + thisB.start.get('value') + '..' + thisB.end.get('value')));
                    }, function(error) {
                        thisB.error.innerHTML = error.message + '<br>' + ((error.response || {}).data || {}).error;
                        console.error(error);
                    });
                }
            }).placeAt(actionBar);

            new Button({
                iconClass: 'dijitIconFilter',
                label: 'Get coordinates from highlighted region',
                onClick: function() {
                    var highlight = thisB.browser.getHighlight();
                    if (highlight) {
                        thisB.start.set('value', highlight.start);
                        thisB.end.set('value', highlight.end);
                        thisB.sequence.set('value', highlight.ref);
                    } else {
                        console.error('No highlight set');
                    }
                }
            }).placeAt(actionBar);
        },

        show: function() {
            dojo.addClass(this.domNode, 'setLSAA');

            this.sequence = new TextBox({id: 'lsaa_name'});
            this.start = new TextBox({id: 'lsaa_start'});
            this.end = new TextBox({id: 'lsaa_end'});
            this.description = new TextBox({id: 'lsaa_description'});
            this.sequencedata = dom.create('textarea', { style: { height: '60px', width: '100%' }, id: 'sequencedata' });
            this.error = dom.create('div', { 'id': 'error', 'class': 'errormsg' });
            var br = function() { return dom.create('br'); };

            this.set('content', [
                dom.create('p', { innerHTML: 'Provide coordinate and alternate sequences for assembly fix. See the <a href="../help">help guide</a> for additional information.' }), br(),
                dom.create('label', { 'for': 'lsaa_name', innerHTML: 'Reference sequence: ' }), this.sequence.domNode, br(),
                dom.create('label', { 'for': 'lsaa_start', innerHTML: 'Start: ' }), this.start.domNode, br(),
                dom.create('label', { 'for': 'lsaa_end', innerHTML: 'End: ' }), this.end.domNode, br(),
                dom.create('label', { 'for': 'lsaa_description', innerHTML: 'Description: ' }), this.description.domNode, br(),
                dom.create('label', { 'for': 'sequencedata', innerHTML: 'Sequence data: ' }), this.sequencedata, br(),
                this.error, br()
            ]);

            this.inherited(arguments);
        },

        hide: function() {
            this.inherited(arguments);
            window.setTimeout(dojo.hitch(this, 'destroyRecursive'), 500);
        }
    });
});
