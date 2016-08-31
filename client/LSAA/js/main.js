define([
    'dojo/_base/declare',
    'dojo/_base/lang',
    'JBrowse/Plugin',
    'dijit/MenuItem',
    'dijit/MenuSeparator',
    'LSAA/View/Dialog/LSAA',
    'LSAA/View/Dialog/Reverse',
    'LSAA/View/Dialog/SequenceSearch'
],
function(
    declare,
    lang,
    JBrowsePlugin,
    MenuItem,
    MenuSeparator,
    LSAADialog,
    ReverseDialog,
    SequenceSearchDialog
) {
    return declare(JBrowsePlugin, {
        constructor: function(args) {
            console.log('LSAA plugin starting');
            var browser = args.browser;
            var thisB = this;
            this.contextPath = browser.config.contextPath || '..';

            browser.afterMilestone('initView', function() {
                browser.renderGlobalMenu('lsaa', { text: 'LSAA' }, browser.menuBar);

                browser.addGlobalMenuItem('lsaa', new MenuItem({
                    label: 'Annotate correction',
                    iconClass: 'dijitIconBookmark',
                    onClick: function() {
                        new LSAADialog({ browser: thisB.browser, contextPath: thisB.contextPath }).show();
                    }
                }));

                browser.addGlobalMenuItem('lsaa', new MenuItem({
                    label: 'Annotate inversion',
                    iconClass: 'dijitIconUndo',
                    onClick: function() {
                        new ReverseDialog({ browser: thisB.browser, contextPath: thisB.contextPath }).show();
                    }
                }));
                browser.addGlobalMenuItem('lsaa', new MenuItem({
                    label: 'Search sequence',
                    iconClass: 'dijitIconSearch',
                    onClick: function() {
                        console.log('wtf');
                        new SequenceSearchDialog({
                            browser: thisB.browser,
                            contextPath: thisB.contextPath,
                            refseq: thisB.browser.refSeq.name,
                            successCallback: function(id, fmin, fmax) {
                                console.log('here');
                                var locobj = {
                                    ref: id,
                                    start: fmin,
                                    end: fmax
                                };
                                var highlightSearchedRegions = thisB.browser.config.highlightSearchedRegions;
                                thisB.browser.config.highlightSearchedRegions = true;
                                thisB.browser.showRegionWithHighlight(locobj);
                                thisB.browser.config.highlightSearchedRegions = highlightSearchedRegions;
                            },
                            errorCallback: function(response) {
                                console.log('here');
                                console.error(response);
                            }
                        }).show();
                    }
                }));
                browser.addGlobalMenuItem('lsaa', new MenuItem({
                    label: 'View report',
                    iconClass: 'dijitIconTable',
                    onClick: function() {
                        window.open(thisB.contextPath + '/alternativeLoci');
                    }
                }));
            });
        }
    });
});
