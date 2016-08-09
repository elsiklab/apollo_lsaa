#!/bin/bash

cd web-app
git clone --depth 1 https://github.com/gmod/jbrowse
cd jbrowse
bower install
cd ../../
cp -R test/integration/data/pyu_data web-app/jbrowse/data
rm -rf web-app/jbrowse/plugins/LSAA
rm -rf web-app/jbrowse/plugins/PairedReadViewer
cp -R client/LSAA web-app/jbrowse/plugins/LSAA
cp -R client/PairedReadViewer web-app/jbrowse/plugins/PairedReadViewer
