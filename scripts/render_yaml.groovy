#!/usr/bin/env groovy

//@Grab(group = 'org.ho.yaml', module = 'Yaml', version = '1.3')
@Grab('org.jyaml:jyaml:1.3')

import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException


println Yaml.dump([[sequence: [start: 100, end: 10000, source: "GK00015.2"]],[sequence:[start: 200, end: 20000, source: "GK000015.2"]]])
