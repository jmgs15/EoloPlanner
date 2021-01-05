//https://tomasmalio.medium.com/node-js-express-y-mysql-con-sequelize-ec0a7c0ae292
//https://www.youtube.com/watch?v=bOHysWYMZM0

let express = require('express');
let bodyParser = require('body-parser');
let app = express();
let expressWs = require('express-ws')(app);

const { plantNotificationWebSocket } = require('./config/socket');
const { findAllPlants, createPlant, deletePlant, getPlant } = require('./routes/plant');

app.use(express.static(__dirname + '/public'));
app.use(bodyParser.json());

app.ws('/plantNotifications', plantNotificationWebSocket);
app.get("/eolicplants/", findAllPlants);
app.get("/eolicplants/:id", getPlant);
app.post("/eolicplants/", createPlant);
app.delete("/eolicplants/:city", deletePlant);

module.exports =  {
    app,
    expressWs
};