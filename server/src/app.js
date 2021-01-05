const express = require('express');
const app = express();
require('express-ws')(app);

const { webSocketRouter } = require('./routes/socket');
const { plantRouter } = require('./routes/plant');

app.use(express.static(__dirname + '/public'));
app.use(express.json());

app.use(plantRouter);
app.use(webSocketRouter);

module.exports =  {
    app
};