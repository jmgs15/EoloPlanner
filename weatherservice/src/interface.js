const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');

let packageDefinition = protoLoader.loadSync(__dirname + '/../WeatherService.proto',
    {
        keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true
    });

let weatherServiceProto = grpc.loadPackageDefinition(packageDefinition);

module.exports = weatherServiceProto.com.urjc.planner.WeatherService;