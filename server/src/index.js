//https://tomasmalio.medium.com/node-js-express-y-mysql-con-sequelize-ec0a7c0ae292
//https://www.youtube.com/watch?v=bOHysWYMZM0

let express = require('express');
let bodyParser = require('body-parser');
let app = express();
let expressWs = require('express-ws')(app);
const producer = require('./producer');
const PORT = 3000;

const db = require('./config/database');

db.initialize(app);

app.use(express.static('public'));
app.use(bodyParser.json());

app.ws('/plantNotifications', function (ws, req) {

    console.log('User connected');

    ws.on('message', function (msg) {
        producer.sendMessage(msg);
        console.log('Message received:' + msg);
    });
});

app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));