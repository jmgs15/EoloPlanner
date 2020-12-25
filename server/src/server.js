var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var expressWs = require('express-ws')(app);
const producer = require('./producer');
const PORT = 3000;

app.use(express.static('public'));
app.use(bodyParser.json());

app.get('/test', function (req, res, next) {
    console.log('/test endpoint executed');
    res.json({message:"Test response"});
    res.end();
});

app.post('/eolicPlant', function (req, res) {
    console.log('POST /eolicPlant executed: ' + req.body);
    let id = req.body.id;
    let city = req.body.city;
    console.log('POST /eolicPlant executed: ' + id + ", " + city);
    producer.sendMessage(`{id: ${id}, city: ${city}}`);
    res.json(req.body);
});


app.ws('/plantNotifications', function (ws, req) {

    console.log('User connected');

    ws.on('message', function (msg) {
        producer.sendMessage(msg);
        console.log('Message received:' + msg);
    });

    // setInterval(()=>{

    //     ws.send("Browser message");

    // },1000);

});

app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));