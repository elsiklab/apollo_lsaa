#!/bin/bash

cd web-app
wget --quiet jbrowse.org/releases/JBrowse-1.12.1.zip
unzip JBrowse-1.12.1.zip
mv JBrowse-1.12.1 jbrowse
cd -
rm -rf web-app/jbrowse/data
rm -rf web-app/jbrowse/data_transformed
cp -R test/integration/data/pyu_data web-app/jbrowse/data
cp -R test/integration/data/pyu_data_lsaa web-app/jbrowse/data_transformed
rm -rf web-app/jbrowse/plugins/LSAA
rm -rf web-app/jbrowse/plugins/PairedReadViewer
cp -R client/LSAA web-app/jbrowse/plugins/LSAA
cp -R client/PairedReadViewer web-app/jbrowse/plugins/PairedReadViewer
