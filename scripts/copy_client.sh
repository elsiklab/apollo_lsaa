#!/bin/bash

rm -rf ../../jbrowse-download/plugins/LSAA
rm -rf ../../jbrowse-download/plugins/PairedReadViewer
rm -rf ../../jbrowse-download/plugins/AGPParser
cp -R client/LSAA ../../jbrowse-download/plugins/LSAA
cp -R client/PairedReadViewer ../../jbrowse-download/plugins/PairedReadViewer
cp -R client/AGPParser ../../jbrowse-download/plugins/AGPParser
