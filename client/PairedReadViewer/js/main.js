define([
    'dojo/_base/declare',
    'dojo/_base/lang',
    'JBrowse/Plugin'
],
       function(
           declare,
           lang,
           JBrowsePlugin
       ) {
           return declare(JBrowsePlugin,
               {
                   constructor: function(args) {
                       var browser = args.browser;

        // do anything you need to initialize your plugin here
                       console.log('PairedReadViewer plugin starting');

                       browser.registerTrackType({
                           label: 'PairedRead Arcs',
                           type: 'PairedReadViewer/View/Track/PairedRead'
                       });
                   }
               });
       });
