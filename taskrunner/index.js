var express = require('express');
var exec = require('child_process').exec;
var config = require('./config.json');

var app = express();

// allow CORS
app.use(function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
    next();
});

// retrieve results
app.get('/', function(req, res) {
    exec(`scaffolder sequence ${config.yamlfile} ${config.fastafile}`, function(error, stdout, stderr) {
        res.setHeader('Content-disposition', 'attachment; filename=result.fasta');
        res.contentType('application/octet-stream');
        res.send(stdout);
    });
});

app.listen(process.env.EXPRESS_PORT || 4730);
