//https://tomasmalio.medium.com/node-js-express-y-mysql-con-sequelize-ec0a7c0ae292
//https://www.youtube.com/watch?v=bOHysWYMZM0
const { app, expressWs } = require('./app');
const PORT = 3000;

const db = require('./config/database');
const socket = require('./config/socket');
const queue = require('./config/queue');

let wss = expressWs.getWss('/notifications');

db.initialize()
    .then(() => queue.initialize(wss))
    .then(() => app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`)));