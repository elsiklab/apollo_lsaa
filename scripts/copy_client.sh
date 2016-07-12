#!/bin/bash

rm -rf ../../web-app/jbrowse/plugins/LSAA
cp -R client/LSAA ../../web-app/jbrowse/plugins/LSAA
rm -rf ../../web-app/jbrowse/plugins/PairedReadViewer
cp -R client/PairedReadViewer ../../web-app/jbrowse/plugins/PairedReadViewer
