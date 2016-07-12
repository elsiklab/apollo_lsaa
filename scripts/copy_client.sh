#!/bin/bash

rm -rf ../../web-app/jbrowse/plugins/LSAA
cp -R client/LSAA ../../web-app/jbrowse/plugins/LSAA
rm -rf ../../jbrowse-download/plugins/PairedReadViewer
cp -R client/LSAA ../../jbrowse-download/plugins/PairedReadViewer
