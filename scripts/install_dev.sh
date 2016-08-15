#!/bin/bash

cd web-app
git clone --depth 1 https://github.com/gmod/jbrowse
cd jbrowse
bower install
cd ../../
rm -rf web-app/jbrowse/data
rm -rf web-app/jbrowse/data_transformed
cp -R test/integration/data/pyu_data web-app/jbrowse/data
cp -R test/integration/data/pyu_data_lsaa web-app/jbrowse/data_transformed
rm -rf web-app/jbrowse/plugins/LSAA
rm -rf web-app/jbrowse/plugins/PairedReadViewer
rm -rf web-app/jbrowse/plugins/AGPParser
cp -R client/LSAA web-app/jbrowse/plugins/LSAA
cp -R client/PairedReadViewer web-app/jbrowse/plugins/PairedReadViewer
cp -R client/AGPParser web-app/jbrowse/plugins/AGPParser
