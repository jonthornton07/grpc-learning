var PROTO_PATH = __dirname + '/../grpc-android/app/src/main/proto/ChatService.proto';

var grpc = require('@grpc/grpc-js');
var protoLoader = require('@grpc/proto-loader');
var packageDefinition = protoLoader.loadSync(
    PROTO_PATH,
    {keepCase: true,
     longs: String,
     enums: String,
     defaults: true,
     oneofs: true
    });

var chatProto = grpc.loadPackageDefinition(packageDefinition).com.thornton.grpc.api;

/**
 * Implements the SayHello RPC method.
 */
function chat(call) {
  // callback(null, {message: 'Hello ' + call.request.name});
  call.on('data', function(chat) {
    console.log('chat ' + chat)
    console.log(chat.message + '-' + chat.author)
    call.write(chat);
    call.write(chat);
  });
  call.on('end', function() {
    call.end();
  });
}

/**
 * Starts an RPC server that receives requests for the Greeter service at the
 * sample server port
 */
function main() {
  console.log('starting')
  var server = new grpc.Server();
  server.addService(chatProto.ChatService.service, {chat: chat});
  server.bindAsync('0.0.0.0:50051', grpc.ServerCredentials.createInsecure(), () => {
    server.start();
    console.log('started')
  });
}

main();