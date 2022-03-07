var PROTO_PATH = __dirname + '/../grpc-android/app/src/main/proto/ChatService.proto';

var parseArgs = require('minimist');
var grpc = require('@grpc/grpc-js');
var protoLoader = require('@grpc/proto-loader');
var packageDefinition = protoLoader.loadSync(
    PROTO_PATH,
    {keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true
    }
);

var chatProto = grpc.loadPackageDefinition(packageDefinition).com.thornton.grpc.api;


var argv = parseArgs(process.argv.slice(2), {
    string: 'target'
  });
  var target;
  if (argv.target) {
    target = argv.target;
  } else {
    target = 'localhost:50051';
  }
var client = new chatProto.ChatService(target, grpc.credentials.createInsecure());

function main() {
  var call = client.chat();
  call.on('data', function(chat) {
      console.log(chat)
    //   console.log('Got message "' + chat.message + '" from ' + chat.author);
  })

  call.write({
      "message": "hey there!",
      "author": "Jon"
  })
}

main();