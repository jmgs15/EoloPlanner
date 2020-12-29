const amqp = require('amqplib/callback_api');
const CONN_URL = 'amqp://guest:guest@localhost';
const createPlantRequestQueue = 'eoloplantCreationRequests';
const notificationsQueue = 'eoloplantCreationProgressNotifications';

let notificationChannel = null;
let creationChannel = null;

async function initialize(wss) {
    amqp.connect(CONN_URL, async function (err, conn) {

        notificationChannel = await conn.createChannel(function(error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(notificationsQueue, {
                durable: false
            });

            channel.consume(notificationsQueue, function (msg) {

                    console.log("Message:", msg.content.toString());
                    wss.clients.forEach(function (client) {
                        console.log('Client:' + client);
                        client.send(msg.content.toString());
                    });

                }, { noAck: true }
            );
        });

        creationChannel = await conn.createChannel(function(error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(createPlantRequestQueue, {
                durable: false
            });
        });
    });
}

process.on('exit', (code) => {
    notificationChannel.close();
    creationChannel.close();
    console.log(`Closing rabbitmq channel`);
});



const sendMessage = (message) => {
	
	console.log("publishToQueue: '" + message + "'");
    creationChannel.sendToQueue(createPlantRequestQueue, Buffer.from(message));
};

module.exports.initialize = initialize;
module.exports.sendMessage = sendMessage;