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
var callMap = new Map()

function chat(call) {
  call.on('data', function(chat) {
    if (callMap.get(chat.author) === undefined) {
      callMap.set(chat.author, call)
    }
    console.log('Chat received ' + chat.message + ' - ' + chat.author)
    for(let [author, call] of callMap) {
      if (author !== chat.author) {
        console.log(author)
        call.write(chat)
      }
    }
  });
}

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