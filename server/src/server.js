//https://tomasmalio.medium.com/node-js-express-y-mysql-con-sequelize-ec0a7c0ae292
//https://www.youtube.com/watch?v=bOHysWYMZM0

let express = require('express');
let bodyParser = require('body-parser');
let app = express();
let expressWs = require('express-ws')(app);
const PORT = 3000;

const db = require('./config/database');
const socket = require('./config/socket');
const queue = require('./config/queue');

let wss = expressWs.getWss('/notifications');

db.initialize(app);
socket.initialize(app, wss);
queue.initialize(wss)

app.use(express.static('public'));
app.use(bodyParser.json());

app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));