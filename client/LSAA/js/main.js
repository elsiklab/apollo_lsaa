define([
            'dojo/_base/declare',
            'dojo/_base/lang',
            'JBrowse/Plugin',
            'dijit/MenuItem',
            'dijit/MenuSeparator',
            'dijit/registry',
            'LSAA/View/Dialog/LSAA',
            'LSAA/View/Dialog/LSAARegion'
       ],
       function (
            declare,
            lang,
            JBrowsePlugin,
            MenuItem,
            MenuSeparator,
            registry,
            LSAADialog,
            LSAARegionDialog
       ) {
return declare( JBrowsePlugin,
{
    constructor: function (args) {
        console.log("LSAA plugin starting");
        var browser = args.browser;
        var thisB = this;
        this.contextPath = browser.config.contextPath || "..";


        browser.afterMilestone('completely initialized', function () {
            // use hack for adding to existing menu
            if (!registry.byId("dropdownmenu_tools")) {
                setTimeout(1000, function () {
                    browser.renderGlobalMenu('tools', { text: 'Tools' }, browser.menuBar);
                });
            }

            browser.addGlobalMenuItem('tools', new MenuItem({
                label: 'Create LSAA correction',
                iconClass: 'dijitIconBookmark',
                onClick: lang.hitch(thisB, 'createLSAA')
            }));
            browser.addGlobalMenuItem('tools', new MenuItem({
                label: 'Create LSAA region',
                iconClass: 'dijitIconBookmark',
                onClick: lang.hitch(thisB, 'createLSAARegion')
            }));
            browser.addGlobalMenuItem('tools', new MenuItem({
                label: 'View LSAA report',
                iconClass: 'dijitIconTable',
                onClick: function () { window.open( thisB.contextPath + '/alternativeLoci' ); }
            }));

            browser.addGlobalMenuItem('tools', new MenuSeparator());
        });
    },
    createLSAA: function () {
        var dialog = new LSAADialog({ browser: this.browser, contextPath: this.contextPath });
        dialog.show();
    },
    createLSAARegion: function () {
        var dialog = new LSAARegionDialog({ browser: this.browser, contextPath: this.contextPath });
        dialog.show();
    }
});

});
