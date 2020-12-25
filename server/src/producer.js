const amqp = require('amqplib/callback_api');
const CONN_URL = 'amqp://guest:guest@localhost';
const createPlantRequestQueue = 'eoloplantCreationRequests';
const notificationsQueue = 'eoloplantCreationProgressNotifications';

let ch = null;

process.on('exit', (code) => {
    ch.close();
    console.log(`Closing rabbitmq channel`);
});

amqp.connect(CONN_URL, async function (err, conn) {

    ch = await conn.createChannel();

    ch.consume(notificationsQueue, function (msg) {

    console.log("Message:", msg.content.toString());

        }, { noAck: true }
    );
});


const sendMessage = (message) => {
	
	console.log("publishToQueue: '" + message + "'");
	ch.sendToQueue(createPlantRequestQueue, Buffer.from(message));
};

exports.sendMessage = sendMessage;