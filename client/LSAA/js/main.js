define([
    'dojo/_base/declare',
    'dojo/_base/lang',
    'JBrowse/Plugin',
    'dijit/MenuItem',
    'dijit/MenuSeparator',
    'dijit/registry',
    'LSAA/View/Dialog/LSAA',
    'LSAA/View/Dialog/Reverse'
],
function(
    declare,
    lang,
    JBrowsePlugin,
    MenuItem,
    MenuSeparator,
    registry,
    LSAADialog,
    ReverseDialog
) {
    return declare(JBrowsePlugin, {
        constructor: function(args) {
            console.log('LSAA plugin starting');
            var browser = args.browser;
            var thisB = this;
            this.contextPath = browser.config.contextPath || '..';


            browser.afterMilestone('completely initialized', function() {
                if (!registry.byId('dropdownmenu_tools')) {
                    setTimeout(1000, function() {
                        browser.renderGlobalMenu('tools', { text: 'Tools' }, browser.menuBar);
                    });
                }

                browser.addGlobalMenuItem('tools', new MenuItem({
                    label: 'LSAA - annotate correction',
                    iconClass: 'dijitIconBookmark',
                    onClick: lang.hitch(thisB, 'createLSAA')
                }));

                browser.addGlobalMenuItem('tools', new MenuItem({
                    label: 'LSAA - annotate inversion',
                    iconClass: 'dijitIconUndo',
                    onClick: lang.hitch(thisB, 'createReverse')
                }));
                browser.addGlobalMenuItem('tools', new MenuItem({
                    label: 'LSAA - View report',
                    iconClass: 'dijitIconTable',
                    onClick: function() { window.open(thisB.contextPath + '/alternativeLoci'); }
                }));

                browser.addGlobalMenuItem('tools', new MenuSeparator());
            });
        },
        createLSAA: function() {
            var dialog = new LSAADialog({ browser: this.browser, contextPath: this.contextPath });
            dialog.show();
        },
        createReverse: function() {
            var dialog = new ReverseDialog({ browser: this.browser, contextPath: this.contextPath });
            dialog.show();
        }
    });
});
