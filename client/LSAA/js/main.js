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

            browser.afterMilestone('initView', function() {
                if (!registry.byId('dropdownmenu_lsaa')) {
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
                        label: 'View report',
                        iconClass: 'dijitIconTable',
                        onClick: function() {
                            window.open(thisB.contextPath + '/alternativeLoci');
                        }
                    }));
                    this.browser.afterMilestone('initView', function() {
                        var toolsMenu = dijit.byId('dropdownbutton_lsaa');
                        var helpMenu = dijit.byId('dropdownbutton_help');
                        domConstruct.place(toolsMenu.domNode, helpMenu.domNode, 'before');
                    }); 
                }
            });
        }
    });
});
